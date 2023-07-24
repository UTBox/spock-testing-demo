package com.synacy.gradprogram.spock.exercise

import com.synacy.gradprogram.spock.demo.User
import spock.lang.Specification

class ShoppingServiceSpec extends Specification {

    ShoppingService shoppingService
    Cart cart
    User user
    Order order
    OrderingService orderingService
    DeliveryService deliveryService
    OrderRepository orderRepository
    DeliveryRequestRepository deliveryRequestRepository

    void setup() {
        cart = Mock(Cart)
        user = Mock(User)
        order = Mock(Order)

        orderRepository = Mock(OrderRepository)
        deliveryRequestRepository = Mock(DeliveryRequestRepository)

        orderingService = Mock(OrderingService)
        deliveryService = Mock(DeliveryService)
        shoppingService = new ShoppingService(orderingService, deliveryService,
                orderRepository, deliveryRequestRepository)
    }

    def "BuyNonSpoilingItemsInCart should check if discount applies to cart"() {
        given:
        user.getFirstName() >> "John"
        user.getLastName() >> "Doe"
        user.getAddress() >> "Philippines"

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * orderingService.applyDiscountToCartItems(cart)
    }

    def "BuyNonSpoilingItemsInCart should create an order based on cart"() {
        given:
        user.getFirstName() >> "John"
        user.getLastName() >> "Doe"
        user.getAddress() >> "Philippines"

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * orderingService.createAnOrder(_,_,_,_)
    }

    def "BuyNonSpoilingItemsInCart should save a delivery request based on order"() {
        given:
        user.getFirstName() >> "John"
        user.getLastName() >> "Doe"
        user.getAddress() >> "Philippines"

        orderingService.createAnOrder(_,_,_,_) >> order

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * deliveryService.createDelivery(order)
    }

    def "GetOrderSummary should return an order summary based on order ID"() {
        given:
        Order order = Mock(Order)
        UUID uuid = order.getId()
        DeliveryRequest deliveryRequest = Mock(DeliveryRequest)

        orderRepository.fetchOrderById(uuid) >> order
        deliveryRequestRepository.fetchDeliveryRequestByOrderId(uuid) >> deliveryRequest

        when:
        OrderSummary orderSummary = shoppingService.getOrderSummary(uuid)

        then:
        order.getTotalCost() == orderSummary.getTotalCost()
        order.getStatus() == orderSummary.getStatus()
        deliveryRequest.getDeliveryDate() == orderSummary.getDeliveryDate()
        deliveryRequest.getCourier() == orderSummary.getCourier()
    }

}
