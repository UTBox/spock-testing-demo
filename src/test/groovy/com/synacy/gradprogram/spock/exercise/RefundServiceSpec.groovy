package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {

    RefundService service
    CancelOrderRequest cancelOrderRequest
    RefundRepository refundRepository
    Order order = Mock(order)


    def setup() {
        service = new RefundService()
        Order order = Mock(order)

    }

    def "calculateRefund Should calculate the refund and give full refund due to cancel reason is damaged"() {

        given:
        CancelReason cancelReason = CancelReason.DAMAGED
        Order order = new Order(totalCost: 200, dateOrdered: new Date())

        RefundRequestStatus refundRequestStatus = Mock(RefundRequestStatus)

        when:
        service.calculateRefund(cancelOrderRequest, order)

        then:
        refundRepository.saveRefundRequest(refund)
    }


    def "calculateRefund Should full refund if the order was cancelled within 3 days of order date"() {
        given:
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(UUID.randomUUID())
        Order order = new Order(totalCost: 200, dateOrdered: new Date())


        RefundRequestStatus refundRequestStatus = Mock(RefundRequestStatus)

        when:
        service.calculateRefund(cancelOrderRequest, order)

        then:
        refundRepository.saveRefundRequest(refund)
    }


    def  "CreateAndSaveRefundRequest Should be save the refund request with the correct details"() {
        given:
        UUID orderId = UUID.randomUUID()
        String recipientName = "Precious"
        BigDecimal refundAmount = BigDecimal.valueOf(200)
        RefundRequestStatus refundRequestStatus = RefundRequestStatus.TO_PROCESS

        when:
        service.createAndSaveRefundRequest(orderId, recipientName, refundAmount)

        then:
        1 * refundRepository.saveRefundRequest(_) >> { RefundRequest refundRequest ->
            assert orderId == refundRequest.getOrderId()
            assert recipientName == refundRequest.getRecipientName()
            assert refundAmount == refundRequest.getRefundAmount()
            assert refundRequestStatus == refundRequest.getStatus()


    }


}
