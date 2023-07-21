package com.synacy.gradprogram.spock.exercise

import com.synacy.gradprogram.spock.demo.User
import spock.lang.Specification

class ShoppingServiceSpec extends Specification {

    ShoppingService shoppingService
    OrderingService orderingService = Mock(OrderingService)
    DeliveryService deliveryService = Mock(DeliveryService)
    OrderRepository orderRepository = Mock(OrderRepository)
    Cart cart = Mock(Cart)
    User user = Mock(User)
    DeliveryRequestRepository deliveryRequestRepository = Mock(DeliveryRequestRepository)

    void setup() {
        shoppingService = new ShoppingService(orderingService, deliveryService, orderRepository, deliveryRequestRepository)
    }

    def "BuyNonSpoilingItemsInCart should apply discount to cart items, create an order, and create delivery"() {
        given:
        String firstName = "Harry"
        String lastName = "Styles"
        user.getFirstName() >> firstName
        user.getLastName() >> lastName

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * deliveryService.createDelivery(_)

    }

    def "GetOrderSummary should return the total cost, status, delivery date, and courier based on the order ID"() {
//        given:
//        String name = "Harry Styles"
//        String address = "Cebu City"
//        double totalCost = 100.0
//        OrderStatus orderStatus = OrderStatus.PENDING
//        Date deliveryDate = new Date()
//
//        Courier courier = Courier.LBC
//        Order order = new Order(recipientName: name, recipientAddress: address, totalCost: totalCost)
//        UUID orderId = order.getId()
//
//        orderRepository.fetchOrderById(orderId) >> order
//        deliveryRequestRepository.fetchDeliveryRequestByOrderId(orderId) >> deliveryRequest
//
//        when:
//        OrderSummary result = shoppingService.getOrderSummary(orderId)
//
//        then:
//        1 * orderRepository.fetchOrderById(orderId)
//        1 * deliveryRequestRepository.fetchDeliveryRequestByOrderId(orderId)
    }
}
