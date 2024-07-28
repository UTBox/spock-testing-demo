package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {
    RefundService refundService

    OrderRepository orderRepository = Mock(OrderRepository)

    void setup() {
        refundService = new RefundService(orderRepository)
    }

    def "calculateRefund should return full refund amount when cancel reason is due to damaged item "() {
        given:
        RefundRequest refundRequest = Mock()
        CancelOrderRequest cancelOrderRequest = Mock()

        BigDecimal expectedRefundAmount = BigDecimal.valueOf(100)
        refundRequest.refundAmount >> BigDecimal.valueOf(100)
        cancelOrderRequest.reason >> CancelReason.DAMAGED

        when:
        BigDecimal actualRefundAmount = refundService.calculateRefund(refundRequest, cancelOrderRequest)

        then:
        expectedRefundAmount == actualRefundAmount
    }

    def "calculateRefund should return full refund amount when cancel reason is wrong item and order is cancelled within 3 days of order date"() {
        given:
        UUID orderId = UUID.randomUUID()
        Order order = Mock(Order)
        order.id >> orderId

        and:
        Calendar calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, - 3)
        Date modifiedOrderDate = calendar.getTime()
        order.getDateOrdered() >> modifiedOrderDate

        orderRepository.fetchOrderById(orderId) >> Optional.of(order)

        and:
        RefundRequest refundRequest = Mock(RefundRequest)
        refundRequest.orderId >> orderId
        BigDecimal expectedRefundAmount = BigDecimal.valueOf(100)
        refundRequest.refundAmount >> BigDecimal.valueOf(100)

        and:
        CancelOrderRequest cancelOrderRequest = Mock(CancelOrderRequest)
        cancelOrderRequest.reason >> CancelReason.WRONG_ITEM
        Date cancelDate = new Date()
        cancelOrderRequest.dateCancelled >> cancelDate

        when:
        BigDecimal actualRefundAmount = refundService.calculateRefund(refundRequest, cancelOrderRequest)

        then:
        expectedRefundAmount == actualRefundAmount
    }

    def "calculateRefund should return half of the refund amount when cancel reason is wrong item and order is cancelled after 3 days of order date"() {
        given:
        UUID orderId = UUID.randomUUID()
        Order order = Mock(Order)
        order.id >> orderId

        and:
        Calendar calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, - 4)
        Date modifiedOrderDate = calendar.getTime()
        order.getDateOrdered() >> modifiedOrderDate

        orderRepository.fetchOrderById(orderId) >> Optional.of(order)

        and:
        RefundRequest refundRequest = Mock(RefundRequest)
        refundRequest.orderId >> orderId
        BigDecimal expectedRefundAmount = BigDecimal.valueOf(50)
        refundRequest.refundAmount >> BigDecimal.valueOf(100)

        and:
        CancelOrderRequest cancelOrderRequest = Mock(CancelOrderRequest)
        cancelOrderRequest.reason >> CancelReason.WRONG_ITEM
        Date cancelDate = new Date()
        cancelOrderRequest.dateCancelled >> cancelDate

        when:
        BigDecimal actualRefundAmount = refundService.calculateRefund(refundRequest, cancelOrderRequest)

        then:
        expectedRefundAmount == actualRefundAmount
    }
}
