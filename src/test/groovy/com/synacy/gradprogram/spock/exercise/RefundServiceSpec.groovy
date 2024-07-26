package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {
    RefundService refundService

    def setup(){
        refundService = new RefundService()
    }

    def "createAndSaveRefundRequest should create a refund request using the recipient name and order ID"(){
        given:
        String recipientName = "John Cena"
        UUID uuid = UUID.randomUUID()
        BigDecimal refundAmount = 100

        when:
        refundService.createAndSaveRefundRequest(recipientName, uuid)

        then:
        1 * refundService.createRefundRequest(recipientName, uuid) >> { RefundRequest refundRequest ->
            assert recipientName == refundRequest.getRecipientName()
            assert uuid == refundRequest.getOrderId()
            assert refundAmount == refundRequest.getRefundAmount()
            assert RefundRequestStatus.TO_PROCESS == refundRequest.getStatus()
        }
    }
}
