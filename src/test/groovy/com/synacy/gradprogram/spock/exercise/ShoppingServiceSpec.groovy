package com.synacy.gradprogram.spock.exercise

import com.synacy.gradprogram.spock.demo.User
import spock.lang.Specification

class ShoppingServiceSpec extends Specification {

    ShoppingService shoppingService
    OrderingService orderingService = Mock(OrderingService)
    DeliveryService deliveryService = Mock(DeliveryService)
    OrderRepository orderRepository = Mock(OrderRepository)
    DeliveryRequest deliveryRequest = Mock(DeliveryRequest)
    DeliveryRequestRepository deliveryRequestRepository = Mock(DeliveryRequestRepository)


    void setup() {
        shoppingService = new ShoppingService(orderingService, deliveryService, orderRepository, deliveryRequestRepository)
    }


    def "BuyNonSpoilingItemsInCart should apply discount to cart items"() {
        given:
        Cart cart = Mock(Cart)
        User user = Mock(User)
        String firstName = "Harry"
        String lastName = "Styles"
        user.getFirstName() >> firstName
        user.getLastName() >> lastName

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * orderingService.applyDiscountToCartItems(_)
    }

    def "BuyNonSpoilingItemsInCart should create delivery"() {
        given:
        Cart cart = Mock(Cart)
        User user = Mock(User)
        String firstName = "Harry"
        String lastName = "Styles"
        user.getFirstName() >> firstName
        user.getLastName() >> lastName

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * deliveryService.createDelivery(_)

    }

    def "BuyNonSpoilingItemsInCart should create an order"() {
        given:
        Cart cart = Mock(Cart)
        User user = Mock(User)
        String firstName = "Harry"
        String lastName = "Styles"
        user.getFirstName() >> firstName
        user.getLastName() >> lastName

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * orderingService.createAnOrder(_,_,_,_)

    }

    def "GetOrderSummary should return the total cost, status, delivery date, and courier based on the order ID"() {
        given:
        UUID id = UUID.randomUUID()
        Order order = Mock(Order)
        orderRepository.fetchOrderById(id)>>order
        deliveryRequestRepository.fetchDeliveryRequestByOrderId(id)>>deliveryRequest

        when:
        OrderSummary orderSummary = shoppingService.getOrderSummary(id)

        then:
        order.getTotalCost() == orderSummary.getTotalCost()
        order.getStatus() == orderSummary.getStatus()
        deliveryRequest.getDeliveryDate() == orderSummary.getDeliveryDate()
        deliveryRequest.getCourier() == orderSummary.getCourier()

    }
}
