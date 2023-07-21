package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification
import spock.lang.Unroll

class DeliveryServiceSpec extends Specification {

    DeliveryService service
    DateUtils dateUtils = Mock(DateUtils)
    DeliveryRequestRepository deliveryRequestRepository = Mock(DeliveryRequestRepository)

    void setup() {
        service = new DeliveryService(dateUtils, deliveryRequestRepository)
    }


    def "createDelivery should create delivery request"() {
        given:
        Order order = Mock(Order)

        Date currentDate = new Date()
        dateUtils.getCurrentDate() >> currentDate

        when:
        DeliveryRequest result = service.createDelivery(order)

        then:
        1 * dateUtils.getCurrentDate() >> currentDate
        1 * deliveryRequestRepository.saveDeliveryRequest(_)

        and:
        result.orderId == order.id
        result.courier != null
        result.deliveryDate != null
    }

    def "determineCourier should return the correct courier based on orderTotalCost"() {
        expect:
        service.determineCourier(orderTotalCost) == expectedCourier

        where:
        orderTotalCost  |   expectedCourier
        10.0            |   Courier.JRS
        25.0            |   Courier.GRAB
        40.0            |   Courier.LBC
    }

    @Unroll
    def "determineCourier should return GRAB for ordering totalCost between 20 and 30 (inclusive)"() {
        expect:
        service.determineCourier(orderTotalCost) == Courier.GRAB

        then:
        orderTotalCost << (20..30)
    }

    @Unroll
    def "determineCourier should return LBC for ordering totalCost greater than 30"() {
        expect:
        service.determineCourier(orderTotalCost) == Courier.LBC

        then:
        orderTotalCost << (30..50)
    }
}