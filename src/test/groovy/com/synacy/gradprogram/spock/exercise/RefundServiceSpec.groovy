package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

import java.time.LocalDateTime

class RefundServiceSpec extends Specification {

    RefundService service
    RefundRepository refundRepository = Mock(refundRepository)


    void setup() {
        service = new RefundService(refundRepository)

    }

    def "calculateRefund Should calculate the refund give full refund due to cancel reason is damaged"() {

        given:
        CancelReason cancelReason = CancelReason.DAMAGED
        Order order = new Order(totalCost: 200, dateOrdered: new Date())


        when:
        BigDecimal refundAmount = service.calculateRefund(cancelReason, order)

        then:
        refundAmount == BigDecimal.valueOf(200)
    }


    def "calculateRefund Should give full refund if the order was cancelled within 3 days"() {
        given:
        CancelReason cancelReason = CancelReason.DAMAGED
        Order order = new Order()
        order.setTotalCost(new BigDecimal(200))
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3)
        order.setDateOrdered(Date.from(threeDaysAgo.atZone(ZoneId.systemDefault()).toInstant()))
        boolean isWithinThreeDays = LocalDateTime.ofInstant(order.getDateOrdered().toInstant(), ZoneId.systemDefault()).isAfter(LocalDateTime.now().minusDays(3))

        when:
        BigDecimal refundAmount = service.calculateRefund(cancelReason, order)

        then:
        isWithinThreeDays
        refundAmount == BigDecimal.valueOf(200)

    }

    def "calculateRefund should calculate half refund more than 3 days after the order"() {
        given:
        CancelReason cancelReason = CancelReason.DAMAGED
        Order order = new Order()
        order.setTotalCost(new BigDecimal(200))
        LocalDateTime fourDaysAgo = LocalDateTime.now().minusDays(4)
        order.setDateOrdered(Date.from(fourDaysAgo.atZone(ZoneId.systemDefault()).toInstant()))

        when:
        BigDecimal refundAmount = service.calculateRefund(cancelReason, order)

        then:
        refundAmount == BigDecimal.valueOf(100)
    }


    def "CreateAndSaveRefundRequest Should be save the refund request with the correct details"() {
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
}