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

    def "createAndSaveRefundRequest Should save if the Refund Request Status is TO_PROCESS"(){
        given:
        RefundRequest refundRequest = new RefundRequest()
        refundRequest.setStatus(RefundRequestStatus.TO_PROCESS)

        when:
        refundService.createAndSaveRefundRequest(refundRequest)

        then:
        1 * refundRepository.saveRefundRequest(refundRequest)
    }

    def "createAndSaveRefundRequest Should not save if the Refund Request Status is not TO_PROCESS"(){
        given:
        RefundRequest refundRequest = new RefundRequest()
        refundRequest.setStatus(RefundRequestStatus.PROCESSED)

        when:
        refundService.createAndSaveRefundRequest(refundRequest)

        then:
        0 * refundRepository.saveRefundRequest(_)
    }

}
