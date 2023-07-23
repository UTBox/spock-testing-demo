package com.synacy.gradprogram.spock.exercise;

import java.util.Date;
import java.util.Optional;
import java.math.BigDecimal;



public class OrderingService {

  private final OrderRepository orderRepository;
  private RefundRepository refundRepository;
  private RefundService refundService;


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

  public void cancelOrder(CancelOrderRequest request, OrderStatus orderStatus) {
    // TODO: Implement me. Cancels PENDING and FOR_DELIVERY orders and create a refund request saving it to the database.
    //  Else throws an UnableToCancelException
    if (orderStatus == OrderStatus.PENDING || orderStatus == OrderStatus.FOR_DELIVERY) {
      Optional<Order> optionalOrder = orderRepository.fetchOrderById(request.getOrderId());

      // Check if order exists in the Optional, otherwise throw an UnableToCancelException
      Order order = optionalOrder.orElseThrow(() -> new UnableToCancelException("Order not found."));

      RefundRequest refundRequest = new RefundRequest();
      refundRequest.setOrderId(order.getId());
      refundRequest.setRecipientName(order.getRecipientName());
      refundRequest.setRefundAmount(new BigDecimal(order.getTotalCost()));
      refundRequest.setStatus(RefundRequestStatus.TO_PROCESS);

      refundRepository.saveRefundRequest(refundRequest);
    } else {
      throw new UnableToCancelException("Order status is not eligible for cancellation.");
    }

  }
}
