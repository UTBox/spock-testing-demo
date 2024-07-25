package com.synacy.gradprogram.spock.exercise;

import java.util.UUID;

// TODO: Add unit tests in this service's methods
public class ShoppingService {

  private final OrderingService orderingService;
  private final DeliveryService deliveryService;
  private final OrderRepository orderRepository;
  private final DeliveryRequestRepository deliveryRequestRepository;

  public ShoppingService(OrderingService orderingService, DeliveryService deliveryService,
      OrderRepository orderRepository, DeliveryRequestRepository deliveryRequestRepository) {
    this.orderingService = orderingService;
    this.deliveryService = deliveryService;
    this.orderRepository = orderRepository;
    this.deliveryRequestRepository = deliveryRequestRepository;
  }

  public void buyNonSpoilingItemsInCart(Cart cart, User user) {
    orderingService.applyDiscountToCartItems(cart);

    Order order = orderingService.createAnOrder(cart, user.getFullName(), user.getAddress(), false);

    deliveryService.createDelivery(order);
  }

  public OrderSummary getOrderSummary(UUID orderId) {
    Order order = orderRepository.fetchOrderById(orderId);
    DeliveryRequest deliveryRequest = deliveryRequestRepository.fetchDeliveryRequestByOrderId(orderId);

    return new OrderSummary(order.getTotalCost(), order.getStatus(),
        deliveryRequest.getDeliveryDate(), deliveryRequest.getCourier());
  }
}
