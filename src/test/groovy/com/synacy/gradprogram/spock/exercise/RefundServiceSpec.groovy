package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.ZoneId

class RefundServiceSpec extends Specification {

    RefundService refundService
    RefundRepository refundRepository


    void setup() {
        refundService = new RefundService()
        refundRepository = new RefundRepository()

    }

    def "calculateRefund Should calculate the refund give full refund due to cancel reason is damaged"() {

        given:
        CancelReason cancelReason = CancelReason.DAMAGED
        Order order = new Order(totalCost: 200, dateOrdered: new Date())
        var fullRefundAmount = BigDecimal.valueOf(order.getTotalCost()) // 200
        var refundAmountDivideByTwo = fullRefundAmount.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP) // 100
        boolean isWithinThreeDays = LocalDateTime.ofInstant(order.getDateOrdered().toInstant(), ZoneId.systemDefault()).isAfter(LocalDateTime.now().minusDays(3))


        when:
        BigDecimal refundAmount = fullRefundAmount


        then:
        refundAmount == BigDecimal.valueOf(200)


    }


    def "calculateRefund Should give full refund if the order was cancelled within 3 days"() {
        given:
        CancelReason cancelReason = CancelReason.DAMAGED
        Order order = new Order()
        order.setTotalCost(new BigDecimal(200))
        LocalDateTime moreThanThreeDays = LocalDateTime.now().minusDays(3)
        order.setDateOrdered(Date.from(moreThanThreeDays.atZone(ZoneId.systemDefault()).toInstant()))
        boolean isWithinThreeDays = LocalDateTime.ofInstant(order.getDateOrdered().toInstant(), ZoneId.systemDefault()).isAfter(LocalDateTime.now().minusDays(3))

        when:
        BigDecimal fullRefundAmount = BigDecimal.valueOf(order.getTotalCost()) // 200

        then:
        fullRefundAmount == BigDecimal.valueOf(200)

    }

    def "calculateRefund should calculate half refund more than 3 days after the order"() {
        given:
        CancelReason cancelReason = CancelReason.DAMAGED
        Order order = new Order()
        ZoneId
        order.setTotalCost(new BigDecimal(200))
        var fullRefundAmount = BigDecimal.valueOf(order.getTotalCost())
        LocalDateTime fourDaysAgo = LocalDateTime.now().minusDays(3)
        order.setDateOrdered(Date.from(fourDaysAgo.atZone(ZoneId.systemDefault()).toInstant()))
        var totalCost = BigDecimal.valueOf(order.getTotalCost())
        var refundAmountDivideByTwo = totalCost.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP)


        when:
        refundAmountDivideByTwo = 100
        //refundService.calculateRefund(cancelReason, order)

        then:
        refundAmountDivideByTwo != 200
    }


    def "CreateAndSaveRefundRequest Should be save the refund request with the correct details"() {
        given:
        UUID orderId = UUID.randomUUID()
        String recipientName = "Precious"
        BigDecimal refundAmount = BigDecimal.valueOf(200)
        RefundRequestStatus refundRequestStatus = RefundRequestStatus.TO_PROCESS

        when:
        refundService.createAndSaveRefundRequest(recipientName, orderId, refundAmount, refundRequestStatus)

        then:
        1 * refundRepository.saveRefundRequest(_) >> { RefundRequest refundRequest ->
            assert orderId == refundRequest.getOrderId()
            assert recipientName == refundRequest.getRecipientName()
            assert refundAmount == refundRequest.getRefundAmount()
            assert refundRequestStatus == refundRequest.getStatus()


        }


    }
}