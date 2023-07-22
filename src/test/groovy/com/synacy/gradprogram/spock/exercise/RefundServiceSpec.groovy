package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {


    RefundRepository refundRepository = Mock(RefundRepository)
    RefundRequest refundRequest = Mock(RefundRequest)
    RefundRequestStatus refundRequestStatus = GroovyMock(RefundRequestStatus)
    RefundService refundService


    void setup() {
        refundService = new RefundService(refundRepository,refundRequestStatus)
    }

    def "Should return full refund if the item is Damaged"() {
        given:
        refundRequest.isDamagedItem() >> true
        refundService.daysBetween(_,_) >> 2
        BigDecimal totalCost = new BigDecimal("100")

        when:
        BigDecimal totalRefund = refundService.calculateRefund(new Date(), CancelReason.DAMAGED, totalCost)

        then:
        totalRefund == totalCost
    }

    def "Should return half refund for other cancellation reasons"() {
        given:
        refundRequest.isDamagedItem() >> false
        refundService.daysBetween(_, _) >> 4
        refundRequest.getCancelReason() >> CancelReason.WRONG_ITEM
        BigDecimal totalCost = new BigDecimal("50.00")
        BigDecimal expectedRefund = totalCost.divide(new BigDecimal("2"))


        when:
        BigDecimal totalRefund = refundService.calculateRefund(new Date(), CancelReason.WRONG_ITEM, totalCost).divide(new BigDecimal("2"))

        then:
        totalRefund == expectedRefund
    }

    def "createAndSaveRefundRequest Should return true if the Refund Request is TO_PROCESS"(){
        given:
        RefundRequest refundRequest = new RefundRequest()
        refundRequest.setStatus(RefundRequestStatus.PROCESSED)

        when:
        boolean result = refundService.createAndSaveRefundRequest(refundRequest)

        then:
        result == true
        1 * refundRepository.saveRefundRequest(refundRequest)
    }
}
