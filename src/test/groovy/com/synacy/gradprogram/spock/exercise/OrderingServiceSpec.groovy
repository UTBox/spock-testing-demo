package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {
    OrderingService orderingService
    OrderRepository orderRepository = Mock(OrderRepository)

    def setup() {
        orderingService = new OrderingService(orderRepository)
    }

    def "cancelOrder should throw an UnableToCancelException when request order status is invalid"() {
        given:
        Order order = Mock(Order)

        UUID id = UUID.randomUUID()
        order.id >> id
        order.status >> orderStatus

        orderRepository.fetchOrderById(id) >> Optional.of(order)

        CancelOrderRequest request = Mock(CancelOrderRequest)
        request.orderId >> id

        when:
        orderingService.cancelOrder(request)

        then:
        thrown(UnableToCancelException)

        where:
        orderStatus << [OrderStatus.DELIVERED, OrderStatus.CANCELLED]
    }
}
