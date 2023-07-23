package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {

    RefundService service
    RefundRequest refundRequest
    RefundRepository refundRepository = Mock(RefundRepository)
    void setup() {
        service = new RefundService(refundRepository)
        refundRequest = new RefundRequest()
    }

    def "calculateRefund should give full refund due to damage item cancel reason"() {
        given:
        CancelReason cancelReason = CancelReason.DAMAGED
        Order order = new Order(totalCost: 100.0, dateOrdered: new Date())

        when:
        BigDecimal result = service.calculateRefund(cancelReason, order, refundRequest)

        then:
        result == BigDecimal.valueOf(100.0)
        refundRequest.refundAmount == BigDecimal.valueOf(100.0)
    }

    def "calculateRefund should give full refund when cancel within 3 days"() {
        given:
        CancelReason cancelReason = CancelReason.DAMAGED
        Order order = new Order(totalCost: 100.0, dateOrdered: DateUtils.subtractDays(new Date(), 3))
        boolean isMoreThanThreeDays = DateUtils.isMoreThanThreeDaysAgo(order.dateOrdered)

        when:
        BigDecimal result = service.calculateRefund(cancelReason, order, refundRequest)

        then:
        isMoreThanThreeDays
        result == BigDecimal.valueOf(100.0)
        refundRequest.refundAmount == BigDecimal.valueOf(100.0)
    }

    def "calculateRefund should give half refund for other cancel reason"() {
        given:
        CancelReason cancelReason = CancelReason.WRONG_ITEM
        Order order = new Order(totalCost: 100.0, dateOrdered: DateUtils.subtractDays(new Date(), 5))

        when:
        BigDecimal result = service.calculateRefund(cancelReason, order, refundRequest)

        then:
        result == BigDecimal.valueOf(50.0)
        refundRequest.refundAmount == BigDecimal.valueOf(50.0)
    }

    def "createAndSaveRefundRequest should create and save refund request"() {
        given:
        UUID orderId = UUID.randomUUID()
        String recipientName = "Romeo"
        BigDecimal refundAmount = BigDecimal.valueOf(100.0)
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
}
