package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {
    RefundService refundService

    OrderRepository orderRepository = Mock(OrderRepository)
    RefundRepository refundRepository = Mock(RefundRepository)

    void setup() {
        refundService = new RefundService(orderRepository, refundRepository)
    }

    def "calculateRefund should return full refund amount when cancel reason is due to damaged item "() {
        given:
        RefundRequest refundRequest = Mock()
        refundRequest.refundAmount >> BigDecimal.valueOf(100)

        CancelOrderRequest cancelOrderRequest = Mock()
        cancelOrderRequest.reason >> CancelReason.DAMAGED

        BigDecimal expectedRefundAmount = BigDecimal.valueOf(100)

        when:
        BigDecimal actualRefundAmount = refundService.calculateRefund(refundRequest, cancelOrderRequest)

        then:
        expectedRefundAmount == actualRefundAmount
    }

    def "calculateRefund should return full refund amount when cancel reason is wrong item and order is cancelled within 3 days of order date"() {
        given:
        UUID orderId = UUID.randomUUID()
        Order order = Mock(Order)
        orderRepository.fetchOrderById(orderId) >> Optional.of(order)

        and:
        Calendar calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, - 3)
        Date modifiedOrderDate = calendar.getTime()
        order.getDateOrdered() >> modifiedOrderDate

        and:
        RefundRequest refundRequest = Mock(RefundRequest)
        refundRequest.orderId >> orderId
        refundRequest.refundAmount >> BigDecimal.valueOf(100)

        BigDecimal expectedRefundAmount = BigDecimal.valueOf(100)

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
        orderRepository.fetchOrderById(orderId) >> Optional.of(order)

        and:
        Calendar calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, - 4)
        Date modifiedOrderDate = calendar.getTime()
        order.getDateOrdered() >> modifiedOrderDate

        and:
        RefundRequest refundRequest = Mock(RefundRequest)
        refundRequest.orderId >> orderId
        refundRequest.refundAmount >> BigDecimal.valueOf(99)

        BigDecimal expectedRefundAmount = BigDecimal.valueOf(49.5)

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

    def "createAndSaveRefundRequest should create a refund request and saves the refund request to the database"() {
        given:
        UUID id = UUID.randomUUID()
        Order order = Mock(Order)
        order.recipientName >> "Muzan Kibutsuji"
        order.id >> id
        order.totalCost >> 100

        when:
        refundService.createAndSaveRefundRequest(order)

        then:
        1 * refundRepository.saveRefundRequest(_ as RefundRequest) >> { RefundRequest passedRequest ->
            assert "Muzan Kibutsuji" == passedRequest.recipientName
            assert id == passedRequest.orderId
            assert BigDecimal.valueOf(100) == passedRequest.refundAmount
            assert RefundRequestStatus.TO_PROCESS == passedRequest.status
        }
    }
}
