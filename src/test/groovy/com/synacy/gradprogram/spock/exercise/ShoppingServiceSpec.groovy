package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class ShoppingServiceSpec extends Specification {
    OrderingService orderingService = Mock(OrderingService)
    DeliveryService deliveryService = Mock(DeliveryService)
    OrderRepository orderRepository = Mock(OrderRepository)
    DeliveryRequestRepository deliveryRequestRepository = Mock(DeliveryRequestRepository)

    ShoppingService shoppingService

    void setup() {
        shoppingService = new ShoppingService(orderingService, deliveryService,
                orderRepository, deliveryRequestRepository)
    }

    def "buyNonSpoilingItemsInCart should create an order for the given cart and user details"() {
        given:
        Cart cart = new Cart(UUID.randomUUID(), [new Item("phone", 10.0, ItemType.GADGET)])
        User user = new User(firstName: "Sean", lastName: "Kibutsuji", address: "BGC")

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * orderingService.createAnOrder(cart, "Sean Kibutsuji", "BGC", false)
    }

    def "buyNonSpoilingItemsInCart should create delivery order for given cart and user details"() {
        given:
        Cart cart = Mock(Cart)
        User user = Mock(User)
        user.firstName >> "Muzan"
        user.lastName >> "Kibutsuji"
        user.address >> "BGC"

        Order order = Mock(Order)
        orderingService.createAnOrder(cart, "Muzan Kibutsuji", "BGC", false) >> order

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * deliveryService.createDelivery(order)
    }

    def "getOrderSummary return new OrderSummary using valid order and deliveryRequest details"() {
        given:
        Order order = Mock(Order)
        UUID id = UUID.randomUUID()
        order.id >> id
        order.totalCost >> 20.0
        order.status >> OrderStatus.PENDING
        orderRepository.fetchOrderById(id) >> order

        DeliveryRequest deliveryRequest = Mock(DeliveryRequest)
        deliveryRequest.deliveryDate >> new Date()
        deliveryRequest.courier >> Courier.JRS
        deliveryRequestRepository.fetchDeliveryRequestByOrderId(id) >> deliveryRequest

        when:
        OrderSummary orderSummary = shoppingService.getOrderSummary(id)

        then:
        20d == orderSummary.totalCost
        OrderStatus.PENDING == orderSummary.status
        deliveryRequest.deliveryDate == orderSummary.deliveryDate
        Courier.JRS == orderSummary.courier
    }
}
