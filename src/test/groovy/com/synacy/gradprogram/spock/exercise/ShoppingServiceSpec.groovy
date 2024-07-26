package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class ShoppingServiceSpec extends Specification {
    ShoppingService shoppingService

    OrderingService orderingService = Mock()
    DeliveryService deliveryService = Mock()
    OrderRepository orderRepository = Mock()
    DeliveryRequestRepository deliveryRequestRepository = Mock()

    def setup(){
        shoppingService = new ShoppingService(orderingService,
                deliveryService,
                orderRepository,
                deliveryRequestRepository
        )
    }

    def "buyNonSpoilingItemsInCart should #discountDesc when the cart is #eligibilityDesc"(){
        given:
        User user = Mock()
        Cart cart = Mock()

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * orderingService.applyDiscountToCartItems(cart)
    }

    def "buyNonSpoilingItemsInCart should create a non-Food order based on the cart with non-food items and recipient's name and address"(){
        given:
        String fullName = "John Wick"
        String address = "Hollywoo"

        User user = Mock()
        user.getFullName() >> fullName
        user.address >> address

        Cart cart = Mock()

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * orderingService.createAnOrder(cart, user.getFullName(), user.getAddress(), false)
    }

    def "buyNonSpoilingItemsInCart should create a delivery based on the non-food order"(){
        given:
        String fullName = "John Wick"
        String address = "Hollywoo"

        User user = Mock()
        user.getFullName() >> fullName
        user.address >> address

        Cart cart = Mock()
        Order order = Mock()

        orderingService.createAnOrder(cart, user.getFullName(), user.getAddress(), false) >> order

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * deliveryService.createDelivery(order)
    }

    def "getOrderSummary should return an order summary based on order and delivery request details."(){
        given:
        double totalCost = 0
        OrderStatus status = OrderStatus.PENDING

        Order order = new Order(totalCost: totalCost, status: status)
        UUID uuid = order.id

        orderRepository.fetchOrderById(uuid) >> order

        and:
        Date deliveryDate = new Date()
        Courier courier = Courier.JRS
        DeliveryRequest deliveryRequest = new DeliveryRequest(deliveryDate: deliveryDate, courier: courier)

        deliveryRequestRepository.fetchDeliveryRequestByOrderId(uuid) >> deliveryRequest

        when:
        OrderSummary orderSummary = shoppingService.getOrderSummary(uuid)

        then:
        totalCost == orderSummary.getTotalCost()
        status == orderSummary.getStatus()
        deliveryDate == orderSummary.getDeliveryDate()
        courier == orderSummary.getCourier()
    }
}
