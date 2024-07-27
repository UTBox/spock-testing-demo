package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {
    OrderingService orderingService
    OrderRepository orderRepository = Mock(OrderRepository)
    RefundRepository refundRepository = Mock(RefundRepository)

    def setup() {
        orderingService = new OrderingService(orderRepository, refundRepository)
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

    def "cancelOrder should cancel order and create refund request when request order status is valid"() {
        given:
        Order order = Mock(Order)
        UUID id = UUID.randomUUID()
        order.id >> id
        order.status >> orderStatus

        orderRepository.fetchOrderById(id) >> Optional.of(order)

        CancelOrderRequest cancelOrderRequest = Mock(CancelOrderRequest)
        cancelOrderRequest.orderId >> id

        when:
        orderingService.cancelOrder(cancelOrderRequest)

        then:
        1 * refundRepository.saveRefundRequest(_ as RefundRequest)

        where:
        orderStatus << [OrderStatus.PENDING, OrderStatus.FOR_DELIVERY]
    }
}
