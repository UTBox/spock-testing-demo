package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class DeliveryServiceSpec extends Specification {
    DeliveryRequestRepository deliveryRequestRepository
    DateUtils dateUtils

    DeliveryService deliveryService

    void setup() {
        deliveryRequestRepository = Mock()
        dateUtils = Mock()
        deliveryService = new DeliveryService(dateUtils, deliveryRequestRepository)
    }

    def "createDeliveryRequest should save delivery request with the valid order details"() {
        given:
        def currentDate = new Date()
        dateUtils.currentDate >> currentDate

        def order = new Order()
        order.totalCost = expectedTotalCost

        when:
        deliveryService.createDelivery(order)

        then:
        1 * deliveryRequestRepository.saveDeliveryRequest(_ as DeliveryRequest) >> { DeliveryRequest passedRequest ->
            assert order.id == passedRequest.orderId
            assert currentDate == passedRequest.deliveryDate
            assert expectedCourier == passedRequest.courier
        }

        where:
        expectedCourier | expectedTotalCost | desc
        Courier.JRS     | 15                | "less than 20"
        Courier.GRAB    | 25                | "greater than or equal to 20 but less than or equal to 30"
        Courier.LBC     | 35                | "greater than 30"
    }
}
