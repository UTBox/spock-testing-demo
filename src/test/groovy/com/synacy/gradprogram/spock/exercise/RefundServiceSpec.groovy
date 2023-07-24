package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {
    RefundService refundService
    RefundRepository refundRepository = Mock(RefundRepository)

    void setup() {
        refundService = new RefundService()
    }

    def "CalculateRefund should return full refund if order is a damaged item"() {
        given:
        Order order = new Order(status: OrderStatus.CANCELLED, totalCost: 100.0)
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(reason: CancelReason.DAMAGED)
        BigDecimal fullAmount = new BigDecimal("100")

        when:
        BigDecimal refund = refundService.calculateRefund(cancelOrderRequest, order)

        then:
        fullAmount == refund
    }


    def "CalculateRefund should return full refund if order was cancelled within 3 days of order date"() {
        given:
        Order order = new Order(status: OrderStatus.CANCELLED, totalCost: 100.0, dateOrdered: DateUtils.getDaysBeforeDate(-2))
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(reason: CancelReason.DAMAGED)
        BigDecimal fullAmount = new BigDecimal("100")

        when:
        BigDecimal refund = refundService.calculateRefund(cancelOrderRequest, order)

        then:
        fullAmount == refund
    }

    def "CalculateRefund should return half refund if order is not a damaged item"() {
        given:
        Order order = new Order(status: OrderStatus.CANCELLED, totalCost: 100.0)
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(reason: CancelReason.WRONG_ITEM)
        BigDecimal halfAmount = new BigDecimal("50")

        when:
        BigDecimal refund = refundService.calculateRefund(cancelOrderRequest, order)

        then:
        halfAmount == refund
    }

    def "CalculateRefund should return half refund if order was cancelled after 3 days of order date"() {
        given:
        Order order = new Order(status: OrderStatus.CANCELLED, totalCost: 100.0, dateOrdered: DateUtils.getDaysBeforeDate(-5))
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(reason: CancelReason.WRONG_ITEM)
        BigDecimal halfAmount = new BigDecimal(50)

        when:
        BigDecimal refund = refundService.calculateRefund(cancelOrderRequest, order)

        then:
        halfAmount == refund
    }


}
