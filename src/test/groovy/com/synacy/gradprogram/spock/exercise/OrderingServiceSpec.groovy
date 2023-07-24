package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {

    OrderingService orderingService
    OrderRepository orderRepository = Mock(OrderRepository)
    RefundService refundService = Mock(RefundService)

    void setup() {
        orderingService = new OrderingService(orderRepository, refundService)
    }

    def "cancelOrder should cancel PENDING and FOR_DELIVERY order and create refund amount and refund request"() {
        given:
        Order order = new Order(totalCost: 100.0d, recipientName: "Romeo", status: status)
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest()
        BigDecimal refundAmount = refundService.calculateRefund(cancelOrderRequest.getReason(), order)

        when:
        orderingService.cancelOrder(new CancelOrderRequest(), order)

        then:
        1 * refundService.createAndSaveRefundRequest(order.id, order.recipientName, refundAmount)
        1 * orderRepository.saveOrder(order)

        and:
        order.status == OrderStatus.CANCELLED

        where:
        status << [OrderStatus.PENDING, OrderStatus.FOR_DELIVERY]
    }

    def "cancelOrder should throw UnableToCancelException for other order status"() {
        given:
        Order order = new Order(status: status)

        when:
        orderingService.cancelOrder(new CancelOrderRequest(), order)

        then:
        UnableToCancelException unableToCancelException = thrown(UnableToCancelException)
        unableToCancelException.message == "Unable to cancel order"

        where:
        status << [OrderStatus.DELIVERED, OrderStatus.CANCELLED]
    }
}
