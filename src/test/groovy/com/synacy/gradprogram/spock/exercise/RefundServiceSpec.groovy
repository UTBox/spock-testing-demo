package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {
    RefundService refundService

    RefundRepository refundRepository = Mock()
    DateUtils dateUtils = Mock()

    def setup(){
        refundService = new RefundService(refundRepository, dateUtils)
    }

    def "createAndSaveRefundRequest should create a #refundPolicyDesc refund request when the order was cancelled #refundReasonDesc with the reason #cancelReason"(){
        given: "set test values"
        String recipientName = "John Cena"
        double orderTotalCost = 100

        UUID orderId = UUID.randomUUID()

        and: "Stub necessary values"
        Order order = Mock()
        order.getId() >> orderId
        order.getTotalCost() >> orderTotalCost
        order.getRecipientName() >> recipientName
        order.getDateOrdered() >> new Date()

        CancelOrderRequest cancelOrderRequest = Mock()
        cancelOrderRequest.getReason() >> cancelReason
        cancelOrderRequest.getOrderId() >> orderId
        cancelOrderRequest.getDateCancelled() >> new Date()

        dateUtils.getDateDiffInDays(_ as Date, _ as Date) >> diffFromCancelToOrderDates

        when:
        refundService.createAndSaveRefundRequest(order, cancelOrderRequest)

        then:
        1 * refundRepository.saveRefundRequest(_ as RefundRequest) >> {RefundRequest request ->
            assert recipientName == request.getRecipientName()
            assert orderId == request.getOrderId()
            assert refundAmount == request.getRefundAmount()
            assert RefundRequestStatus.TO_PROCESS == request.getStatus()
        }

        where:
        diffFromCancelToOrderDates   | cancelReason            | refundAmount | refundPolicyDesc | refundReasonDesc
        2                            | CancelReason.DAMAGED    | 100          | "full"           | "within 3 days of order date"
        5                            | CancelReason.DAMAGED    | 100          | "full"           | "within 3 days of order date"
        2                            | CancelReason.WRONG_ITEM | 100          | "full"           | "within 3 days of order date"
        5                            | CancelReason.WRONG_ITEM | 50           | "half"           | "beyond 3 days of order date"
        3                            | CancelReason.WRONG_ITEM | 100          | "full"           | "within 3 days of order date"
    }
}
