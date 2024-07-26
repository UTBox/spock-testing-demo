package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {
    RefundService refundService

    RefundRepository refundRepository = Mock()

    def setup(){
        refundService = new RefundService(refundRepository)
    }

    def "createAndSaveRefundRequest should create a #refundPolicyDesc refund request when the order was cancelled #refundReasonDesc with the reason #cancelReason"(){
        given: "set test values"
        String recipientName = "John Cena"
        double orderTotalCost = 100

        UUID uuid = UUID.randomUUID()

        and: "initialize Order and CancelOrderRequest objects"
        Order order = Mock()
        order.getDateOrdered() >> orderDate
        order.getTotalCost() >> orderTotalCost

        CancelOrderRequest cancelOrderRequest = Mock()
        cancelOrderRequest.getDateCancelled() >> dateCancelled
        cancelOrderRequest.getReason() >> cancelReason

        when:
        refundService.createAndSaveRefundRequest(recipientName, uuid)

        then:
        1 * refundRepository.saveRefundRequest(_ as RefundRequest) >> {RefundRequest request ->
            assert recipientName == request.getRecipientName()
            assert uuid == request.getOrderId()
            assert refundAmount == request.getRefundAmount()
            assert RefundRequestStatus.TO_PROCESS == request.getStatus()
        }

        where:
        orderDate                                           | dateCancelled                                       | cancelReason            | refundAmount | refundPolicyDesc | refundReasonDesc
        new GregorianCalendar(2024, Calendar.FEBRUARY, 1)   | new GregorianCalendar(2024, Calendar.FEBRUARY, 2)   | CancelReason.DAMAGED    | 100          | "full"           | "within 3 days of order date"
        new GregorianCalendar(2024, Calendar.FEBRUARY, 1)   | new GregorianCalendar(2024, Calendar.FEBRUARY, 14)  | CancelReason.DAMAGED    | 100          | "full"           | "within 3 days of order date"
        new GregorianCalendar(2024, Calendar.FEBRUARY, 1)   | new GregorianCalendar(2024, Calendar.FEBRUARY, 2)   | CancelReason.WRONG_ITEM | 100          | "full"           | "within 3 days of order date"
        new GregorianCalendar(2024, Calendar.FEBRUARY, 1)   | new GregorianCalendar(2024, Calendar.FEBRUARY, 14)  | CancelReason.WRONG_ITEM | 50          | "half"           | "within 3 days of order date"
    }
}
