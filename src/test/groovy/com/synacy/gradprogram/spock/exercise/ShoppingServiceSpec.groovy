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

    def "BuyNonSpoilingItemsInCart should check if discount applies to cart and save a delivery request"() {
        given:
        user.getFirstName() >> "John"
        user.getLastName() >> "Doe"
        user.getAddress() >> "Philippines"

        orderingService.createAnOrder(_,_,_,_) >> order

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * orderingService.applyDiscountToCartItems(cart)

        1 * deliveryService.createDelivery(order)
    }

    def "GetOrderSummary should return an order summary based on UUID"() {
        given:
        Order testOrder = new Order(totalCost: 10, status: OrderStatus.PENDING)
        UUID testUuid = testOrder.getId()

        DeliveryRequest testDeliveryRequest = new DeliveryRequest(orderId: testUuid,
                deliveryDate: new Date(), courier: Courier.JRS)

        when:
        OrderSummary orderSummary = shoppingService.getOrderSummary(testUuid)

        then:
        testOrder.getTotalCost() == orderSummary.getTotalCost()
        testOrder.getStatus() == orderSummary.getStatus()
        testDeliveryRequest.getDeliveryDate() == orderSummary.getDeliveryDate()
        testDeliveryRequest.getCourier() == orderSummary.getCourier()
    }

}
