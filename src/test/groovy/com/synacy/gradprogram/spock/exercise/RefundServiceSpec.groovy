package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {

    RefundService refundService
    DateUtils dateUtils

    RefundRepository refundRepository = Mock(RefundRepository)
    OrderRepository orderRepository = Mock(OrderRepository)

    void setup() {
        dateUtils = new DateUtils()
        refundService = new RefundService(dateUtils, refundRepository, orderRepository)
    }

    def "createAndSaveRefundRequest should create RefundRequest, #outputDescription, and save RefundRequest"() {
        given:
        Date elapsedOrderDate = createElapsedOrderDate()
        UUID expectedOrderId = UUID.randomUUID()
        String expectedRecipientName = "John Doe"
        RefundRequestStatus expectedStatus = RefundRequestStatus.TO_PROCESS

        Order order = Mock(Order)
        CancelOrderRequest cancelOrderRequest = Mock(CancelOrderRequest)
        RefundRequest refundRequest = new RefundRequest()

        cancelOrderRequest.getOrderId() >> expectedOrderId
        cancelOrderRequest.getReason() >> cancelReason
        orderRepository.fetchOrderById(expectedOrderId) >> Optional.of(order)
        order.getRecipientName() >> expectedRecipientName
        order.getTotalCost() >> orderTotal
        order.getDateOrdered() >> elapsedOrderDate

        refundRequest.setOrderId(expectedOrderId)
        refundRequest.setRecipientName(expectedRecipientName)
        refundRequest.setStatus(expectedStatus)

        when:
        refundService.createAndSaveRefundRequest(cancelOrderRequest)

        then:
        1 * refundRepository.saveRefundRequest(_ as RefundRequest) >> { RefundRequest request ->
            assert expectedOrderId == request.orderId
            assert expectedRecipientName == request.recipientName
            assert expectedRefundAmount == request.refundAmount
            assert expectedStatus == request.status
        }

        where:
        cancelReason            |  orderTotal  | expectedRefundAmount | outputDescription
        CancelReason.DAMAGED    |     5000     |         5000         | "if cancelReason is DAMAGED, give full refund"
        CancelReason.WRONG_ITEM |     3000     |         1500         | "if cancelReason not DAMAGED, give half refund"
    }

    def "createAndSaveRefundRequest should create RefundRequest, #outputDescription, and save RefundRequest"() {
        given:
        UUID expectedOrderId = UUID.randomUUID()
        String recipientName = "John Doe"

        CancelReason cancelReason = CancelReason.WRONG_ITEM
        RefundRequestStatus expectedStatus = RefundRequestStatus.TO_PROCESS

        Order order = Mock(Order)
        CancelOrderRequest cancelOrderRequest = Mock(CancelOrderRequest)
        RefundRequest refundRequest = new RefundRequest()

        cancelOrderRequest.getOrderId() >> expectedOrderId
        cancelOrderRequest.getReason() >> cancelReason
        orderRepository.fetchOrderById(expectedOrderId) >> Optional.of(order)
        order.getRecipientName() >> recipientName
        order.getTotalCost() >> orderTotal
        order.getDateOrdered() >> dateOrdered

        refundRequest.setOrderId(expectedOrderId)
        refundRequest.setRecipientName(recipientName)
        refundRequest.setStatus(expectedStatus)

        when:
        refundService.createAndSaveRefundRequest(cancelOrderRequest)

        then:
        1 * refundRepository.saveRefundRequest(_ as RefundRequest) >> { RefundRequest request ->
            assert expectedOrderId == request.orderId
            assert recipientName == request.recipientName
            assert expectedRefundAmount == request.refundAmount
            assert expectedStatus == request.status
        }

        where:
        dateOrdered              | orderTotal | expectedRefundAmount | outputDescription
        createValidOrderDate()   |    5000    |         5000         | "if order date is within 3 days from cancellation date, give full refund"
        createElapsedOrderDate() |    3000    |         1500         | "if order date is beyond 3 days from cancellation date, give half refund"
    }

    def Date createValidOrderDate() {
        int daysToSubtract = 2
        Date date = new Date(System.currentTimeMillis() - daysToSubtract * 24 * 60 * 60 * 1000)
        return date
    }

    def Date createElapsedOrderDate() {
        int daysToSubtract = 4
        Date date = new Date(System.currentTimeMillis() - daysToSubtract * 24 * 60 * 60 * 1000)
        return date
    }
}
