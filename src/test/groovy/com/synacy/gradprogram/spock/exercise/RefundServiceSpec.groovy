package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {
    RefundService refundService

    RefundRepository refundRepository = Mock()

    def setup(){
        refundService = new RefundService(refundRepository)
    }

    def "createAndSaveRefundRequest should create and save a refund request using the recipient name and order ID"(){
        given:
        String recipientName = "John Cena"
        UUID uuid = UUID.randomUUID()
        BigDecimal refundAmount = null

        when:
        refundService.createAndSaveRefundRequest(recipientName, uuid)

        then:
        1 * refundRepository.saveRefundRequest(_ as RefundRequest) >> {RefundRequest request ->
            assert recipientName == request.getRecipientName()
            assert uuid == request.getOrderId()
            assert refundAmount == request.getRefundAmount()
            assert RefundRequestStatus.TO_PROCESS == request.getStatus()
        }
    }
}
