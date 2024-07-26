package com.synacy.gradprogram.spock.exercise;

import java.util.Date;
import java.util.UUID;

public class OrderingService {

  private final OrderRepository orderRepository;

  public OrderingService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
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
    // TODO: Implement me. Cancels PENDING and FOR_DELIVERY orders and create a refund request saving it to the database.
    //  Else throws an UnableToCancelException

    UUID orderId = request.getOrderId();
    Order order = orderRepository.fetchOrderById(orderId).get();

    OrderStatus orderStatus = order.getStatus();
    if(orderStatus != OrderStatus.PENDING && orderStatus != OrderStatus.FOR_DELIVERY){
      throw new UnableToCancelException("Order status is not eligible for cancellation");
    }

    order.setStatus(OrderStatus.CANCELLED);
  }
}
