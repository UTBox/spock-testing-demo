package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {

    RefundService refundService
    DateUtils dateUtils = new DateUtils()

    void setup() {
        refundService = new RefundService()

    }

    def "calculateRefund should return full total cost if cancel reason is DAMAGED"() {
        given:
        Order order = new Order(totalCost: 100, dateOrdered: dateUtils.getCurrentDate())
        CancelOrderRequest request = new CancelOrderRequest(reason: CancelReason.DAMAGED)

        when:
        BigDecimal refund = refundService.calculateRefund(request, order)

        then:
        order.getTotalCost() as BigDecimal == refund
    }

    def "calculateRefund should return full total cost if dateCancelled is within 3 days of dateOrdered"() {
        given:
        Date dateOrdered = dateUtils.getCurrentDate()
        Date dateLimit = new Date(dateOrdered.getTime() + (86400000 * 3) - 1)

        Order order = new Order(totalCost: 100, dateOrdered: dateOrdered)
        CancelOrderRequest request = new CancelOrderRequest(dateCancelled: dateLimit)

        when:
        BigDecimal refund = refundService.calculateRefund(request, order)

        then:
        order.getTotalCost() as BigDecimal == refund
    }

    def "calculateRefund should return half of the total cost for dateCancelled more than 3 days after dateOrdered"() {

        Date dateOrdered = dateUtils.getCurrentDate()
        Date dateLimit = new Date(dateOrdered.getTime() + (86400000 * 3) + 1)

        Order order = new Order(totalCost: 100, dateOrdered: dateOrdered)
        CancelOrderRequest request = new CancelOrderRequest(dateCancelled: dateLimit)

        when:
        BigDecimal refund = refundService.calculateRefund(request, order)

        then:
        order.getTotalCost()/2 as BigDecimal == refund
    }
}
