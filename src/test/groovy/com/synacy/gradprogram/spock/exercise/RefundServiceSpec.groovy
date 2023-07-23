package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {

    RefundService service
    RefundRequest refundRequest
    void setup() {
        service = new RefundService()
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
        Date threeDaysAgo = new DateUtils().currentDate
        Order order = new Order(totalCost: 100.0, dateOrdered: threeDaysAgo)

        when:
        BigDecimal result = service.calculateRefund(cancelReason, order, refundRequest)

        then:
        result == BigDecimal.valueOf(100.0)
        refundRequest.refundAmount == BigDecimal.valueOf(100.0)
    }
}
