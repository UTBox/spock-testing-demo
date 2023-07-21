package com.synacy.gradprogram.spock.exercise

import com.synacy.gradprogram.spock.demo.User
import spock.lang.Specification

import java.time.LocalDate

class ShoppingServiceSpec extends Specification {

    ShoppingService shoppingService
    OrderingService orderingService = Mock(OrderingService)
    DeliveryService deliveryService = Mock(DeliveryService)
    OrderRepository orderRepository = Mock(OrderRepository)
    DeliveryRequestRepository deliveryRequestRepository = Mock(DeliveryRequestRepository)

    void setup() {
        shoppingService = new ShoppingService(orderingService, deliveryService, orderRepository, deliveryRequestRepository)
    }

    def "buyNonSpoilingItemsInCart should create an order and a delivery"() {
        given:
        Cart cart = Mock(Cart)
        User user = new User(firstName: "Romeo", lastName: "Fern", address: "Cebu")

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * orderingService.applyDiscountToCartItems(cart)
        1 * orderingService.createAnOrder(cart, "Romeo Fern", "Cebu", false) >> new Order()
        1 * deliveryService.createDelivery(_ as Order)
    }

    def "buyNonSpoilingItemsInCart should concatenate first name and last name correctly"() {
        given:
        Cart cart = Mock(Cart)
        User user = new User(firstName: "Romeo", lastName: "Fern")

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        user.firstName + " " + user.lastName == "Romeo Fern"
    }

    def "getOrderSummary should return correct orderSummary"() {
        given:
        UUID orderId = UUID.randomUUID()
        Order order = Mock(Order)
        DeliveryRequest deliveryRequest = new DeliveryRequest(orderId: orderId, deliveryDate: LocalDate.of(2023, 7, 21), courier: Courier.GRAB  )

    }
}
