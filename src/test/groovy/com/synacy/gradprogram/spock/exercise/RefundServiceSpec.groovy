package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {

    RefundService refundService

    RefundRepository refundRepository = Mock(RefundRepository)

    void setup() {
        refundService = new RefundService()
    }

    def "createAndSaveRefundRequest should create a RefundRequest with #refundRequestStatus of TO_PROCESS and save it"() {
        given:
        UUID uuid = UUID.randomUUID()
        String recipientName = "John Doe"
        String refundAmountString = "2500"
        BigDecimal refundAmount = new BigDecimal(refundAmountString)

        CancelReason cancelReason = CancelReason.DAMAGED
        RefundRequestStatus expectedRefundRequestStatus = RefundRequestStatus.TO_PROCESS

        Order order = Mock(Order)
        CancelOrderRequest cancelOrderRequest = Mock(CancelOrderRequest)
        RefundRequest refundRequest = new RefundRequest()

        refundRequest.setOrderId(uuid)
        refundRequest.setRecipientName(recipientName)
        refundRequest.setStatus(expectedRefundRequestStatus)
        refundRequest.setRefundAmount(refundAmount)

        when:
        refundService.createAndSaveRefundRequest(cancelOrderRequest)

        then:
        1 * refundRepository.saveRefundRequest(refundRequest)
    }
}
