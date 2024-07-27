package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

public class OrderingService {

    private final OrderRepository orderRepository;
    private final RefundRepository refundRepository;

    public OrderingService(OrderRepository orderRepository, RefundRepository refundRepository) {
        this.orderRepository = orderRepository;
        this.refundRepository = refundRepository;
    }

    public boolean cartContainsFoodItem(Cart cart) {
        for (Item item : cart.getItems()) {
            if (item.getType() == ItemType.FOOD) {
                return true;
            }
        }

        return false;
    }

    public double calculateTotalCostOfCart(Cart cart) {
        double totalPrice = 0.0;

        for (Item item : cart.getItems()) {
            totalPrice += item.getCost();
        }

        return totalPrice;
    }

    public boolean isCartEligibleForDiscount(Cart cart) {
        double totalPrice = calculateTotalCostOfCart(cart);
        double discountTotalAmountThreshold = 50.0;
        int itemCountDiscountThreshold = 5;

        return totalPrice > discountTotalAmountThreshold
                && cart.getItems().size() > itemCountDiscountThreshold;
    }

    public void applyDiscountToCartItems(Cart cart) {
        double discountRate = 0.10;
        if (isCartEligibleForDiscount(cart)) {
            for (Item item : cart.getItems()) {
                double discountedCost = item.getCost() * discountRate;
                item.setCost(discountedCost);
            }
        }
    }

    public Order createAnOrder(Cart cart, String recipientName, String recipientAddress, boolean canContainFood) {
        if (!canContainFood && cartContainsFoodItem(cart)) {
            throw new UnableToCreateOrderException("Cart should not contain FOOD items");
        }

        Order order = new Order();
        order.setTotalCost(calculateTotalCostOfCart(cart));
        order.setRecipientName(recipientName);
        order.setRecipientAddress(recipientAddress);
        order.setStatus(OrderStatus.PENDING);
        order.setDateOrdered(new Date());

        orderRepository.saveOrder(order);

        return order;
    }

    public void cancelOrder(CancelOrderRequest request) {
        Order order = orderRepository.fetchOrderById(request.getOrderId()).get();
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.FOR_DELIVERY) {
            throw new UnableToCancelException("The order cannot be cancelled because it is already processed or shipped.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.saveOrder(order);

        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setRecipientName(order.getRecipientName());
        refundRequest.setOrderId(order.getId());
        refundRequest.setRefundAmount(BigDecimal.valueOf(order.getTotalCost()));
        refundRequest.setStatus(RefundRequestStatus.TO_PROCESS);

        refundRepository.saveRefundRequest(refundRequest);
    }
}
