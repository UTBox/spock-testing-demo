package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {

    OrderingService orderingService

    OrderRepository orderRepository = Mock()

    def setup(){
        orderingService = new OrderingService(orderRepository)
    }

    def "cancelOrder should throw an UnableToCancelException for orders with #status status"(){
        given:
        UUID uuid = UUID.randomUUID()
        Order order = Mock()
        order.getStatus() >> status
        order.getId() >> uuid

        and:
        CancelOrderRequest request = Mock()
        request.getOrderId() >> uuid
        orderRepository.fetchOrderById(uuid) >> Optional.of(order)

        when:
        orderingService.cancelOrder(request)

        then:
        thrown(UnableToCancelException)

        where:
        status << [OrderStatus.CANCELLED, OrderStatus.DELIVERED]
    }

    def "cancelOrder should cancel #status orders"(){
        given:
        Order order = new Order()
        order.setStatus(status)
        UUID uuid = order.getId()

        and:
        CancelOrderRequest request = Mock()
        request.getOrderId() >> uuid
        orderRepository.fetchOrderById(uuid) >> Optional.of(order)

        when:
        orderingService.cancelOrder(request)

        then:
        OrderStatus.CANCELLED == order.getStatus()

        where:
        status << [OrderStatus.PENDING, OrderStatus.FOR_DELIVERY]
    }


}
