package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceTestSpec extends Specification {
    OrderingService orderingService
    RefundRequest refundRequest = Mock(RefundRequest)
    OrderRepository orderRepository = Mock(OrderRepository)
    RefundRepository refundRepository = Mock(RefundRepository)

    void setup() {
        orderingService = new OrderingService(orderRepository,refundRepository)
    }

    def "CancelOrder Should cancel PENDING and FOR_DELIVERY orders and create a refund request"() {
        given:
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest()
        cancelOrderRequest.setOrderId(UUID.randomUUID())
        cancelOrderRequest.setReason(CancelReason.DAMAGED)
        cancelOrderRequest.setDateCancelled(new Date())

        when:
        orderingService.cancelOrder(cancelOrderRequest)

        then:
        1 * refundRepository.saveRefundRequest(_)
    }
}
