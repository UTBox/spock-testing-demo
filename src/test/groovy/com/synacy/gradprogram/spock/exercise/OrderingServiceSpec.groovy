package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {
    OrderingService orderingService
    OrderRepository orderRepository

    void setup() {
        orderRepository = Mock(OrderRepository)
        orderingService = new OrderingService(orderRepository)
    }

    def "cancelOrder should set OrderStatus to CANCELLED for orders with status PENDING and FOR_DELIVERY"() {
        given:
        CancelOrderRequest request = Mock(CancelOrderRequest)

        when:
        orderingService.cancelOrder(request, order)

        then:
        expectedStatus == order.getStatus()

        where:
        order                                        |          expectedStatus
        new Order(status: OrderStatus.PENDING)       |   OrderStatus.CANCELLED
        new Order(status: OrderStatus.FOR_DELIVERY)  |   OrderStatus.CANCELLED
    }

    def "cancelOrder should throw UnableToCancelException if OrderStatus is not PENDING or FOR_DELIVERY"() {
        given:
        CancelOrderRequest request = Mock(CancelOrderRequest)

        when:
        orderingService.cancelOrder(request, order)

        then:
        thrown(expectedStatus)

        where:
        order                                     |            expectedStatus
        new Order(status: OrderStatus.CANCELLED)  |   UnableToCancelException
        new Order(status: OrderStatus.DELIVERED)  |   UnableToCancelException
    }
}
