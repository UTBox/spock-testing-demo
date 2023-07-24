package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {

    RefundService refundService
    RefundRepository refundRepository
    DateUtils dateUtils = new DateUtils()

    void setup() {
        refundRepository = Mock(RefundRepository)
        refundService = new RefundService(refundRepository)
    }

    def "calculateRefund should return full total cost if cancel reason is DAMAGED"() {
        given:
        Order order = new Order(totalCost: 100, dateOrdered: dateUtils.getCurrentDate())
        CancelOrderRequest request = new CancelOrderRequest(reason: CancelReason.DAMAGED)

        when:
        BigDecimal refund = refundService.calculateRefund(request, order)

        then:
        order.getTotalCost() as BigDecimal == refund
    }

    def "calculateRefund should return full total cost if dateCancelled is within 3 days of dateOrdered"() {
        given:
        Date dateOrdered = dateUtils.getCurrentDate()
        Long millisecondsInThreeDays = 86400000 * 3
        Long millisecondsBeforeCancelLimit = dateOrdered.getTime() + millisecondsInThreeDays - 1
        Date dateBeforeCancelLimit = new Date(millisecondsBeforeCancelLimit)

        Order order = new Order(totalCost: 100, dateOrdered: dateOrdered)
        CancelOrderRequest request = new CancelOrderRequest(dateCancelled: dateBeforeCancelLimit)

        when:
        BigDecimal refund = refundService.calculateRefund(request, order)

        then:
        order.getTotalCost() as BigDecimal == refund
    }

    def "calculateRefund should return half of the total cost for dateCancelled more than 3 days after dateOrdered"() {
        given:
        Date dateOrdered = dateUtils.getCurrentDate()
        Long millisecondsInThreeDays = 86400000 * 3
        Long millisecondsAfterCancelLimit = dateOrdered.getTime() + millisecondsInThreeDays + 1
        Date dateAfterCancelLimit = new Date(millisecondsAfterCancelLimit)

        Order order = new Order(totalCost: 100, dateOrdered: dateOrdered)
        CancelOrderRequest request = new CancelOrderRequest(dateCancelled: dateAfterCancelLimit)

        when:
        BigDecimal refund = refundService.calculateRefund(request, order)

        then:
        order.getTotalCost()/2 as BigDecimal == refund
    }

    def "createAndSaveRefundRequest should save a TO_PROCESS refund request with correct values"() {
        given:
        Order order = new Order(totalCost: 100, dateOrdered: new Date(), recipientName: "John Doe")
        BigDecimal refund = 100

        when:
        refundService.createAndSaveRefundRequest(order, refund)

        then:
        1 * refundRepository.saveRefundRequest(_) >> { RefundRequest refundRequest ->
            assert order.recipientName == refundRequest.getRecipientName()
            assert order.getId() == refundRequest.getOrderId()
            assert refund == refundRequest.getRefundAmount()
            assert RefundRequestStatus.TO_PROCESS == refundRequest.getStatus()
        }
    }
}
