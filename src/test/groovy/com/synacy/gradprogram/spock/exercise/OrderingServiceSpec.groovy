package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {
    OrderingService orderingService
    OrderRepository orderRepository = Mock(OrderRepository)
    RefundService refundService = Mock(RefundService)
    def setup() {
        orderingService = new OrderingService(orderRepository, refundService)
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

    def "cancelOrder should update the order status to cancelled and save the order when order status is valid"() {
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
        1 * orderRepository.saveOrder(_ as Order)

        where:
        orderStatus << [OrderStatus.PENDING, OrderStatus.FOR_DELIVERY]
    }

    def "cancelOrder should create a refund request when request order status is valid"() {
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
        1 * refundService.createAndSaveRefundRequest(_ as Order)

        where:
        orderStatus << [OrderStatus.PENDING, OrderStatus.FOR_DELIVERY]
    }
}
