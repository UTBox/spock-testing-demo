package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class DeliveryServiceSpec extends Specification {
    DeliveryService deliveryService

    DeliveryRequestRepository deliveryRequestRepository = Mock();
    DateUtils dateUtils = Mock();

    void setup() {
        deliveryService = new DeliveryService(dateUtils, deliveryRequestRepository)
    }

    def "createDelivery should use #courier for total cost #desc "() {
        given:
        Date currentDate = new Date()
        dateUtils.currentDate >> currentDate

        Order order = new Order()
        order.totalCost = totalCost

        when:
        deliveryService.createDelivery(order)

        then:
        1 * deliveryRequestRepository.saveDeliveryRequest(_ as DeliveryRequest) >> { DeliveryRequest passedRequest ->
            assert courier == passedRequest.courier
        }

        where:
        courier      | totalCost | desc
        Courier.JRS  | 15        | "less than 20"
        Courier.GRAB | 25        | "greater than or equal to 20 but less than or equal to 30"
        Courier.LBC  | 35        | "greater than 30"
    }

    def "createDelivery should save delivery request with the correct details"() {
        given:
        Date currentDate = new Date()
        dateUtils.currentDate >> currentDate

        Order order = new Order()
        order.totalCost = 15

        when:
        deliveryService.createDelivery(order)

        then:
        1 * deliveryRequestRepository.saveDeliveryRequest(_ as DeliveryRequest) >> { DeliveryRequest passedRequest ->
            assert order.id == passedRequest.orderId
            assert currentDate == passedRequest.deliveryDate
            assert Courier.JRS == passedRequest.courier
        }
    }
}
