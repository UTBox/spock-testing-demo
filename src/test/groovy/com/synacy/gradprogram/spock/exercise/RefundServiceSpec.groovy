package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {

    RefundService refundService
    CancelOrderRequest request

    void setup() {
        refundService = new RefundService()

    }

    def "calculateRefund should return full total cost if cancel reason is DAMAGED"() {
        given:
        Order order = new Order(totalCost: 100)
        request = new CancelOrderRequest(reason: CancelReason.DAMAGED)

        when:
        BigDecimal refund = refundService.calculateRefund(request, order)

        then:
        order.getTotalCost() as BigDecimal == refund
    }
}
