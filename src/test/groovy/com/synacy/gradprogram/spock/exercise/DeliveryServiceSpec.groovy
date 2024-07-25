package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class DeliveryServiceSpec extends Specification {
    DeliveryService deliveryService

    DeliveryRequestRepository deliveryRequestRepository = Mock()
    DateUtils dateUtils = Mock()

    def setup(){
        deliveryService = new DeliveryService(dateUtils, deliveryRequestRepository)
    }

    def "createDelivery should save and return a delivery request based on the order ID and date"() {
        given:
        Order order = new Order(totalCost: 1)
        Date deliveryDate = new Date()
        dateUtils.getCurrentDate() >> deliveryDate

        when:
        deliveryService.createDelivery(order)

        then:
        1 * deliveryRequestRepository.saveDeliveryRequest(_ as DeliveryRequest) >> {DeliveryRequest deliveryRequest ->
            assert order.id == deliveryRequest.orderId
            assert deliveryDate == deliveryRequest.deliveryDate
        }
    }

    def "createDelivery should assign #courier since the total cost is #costDesc"() {
        given:
        Order order = new Order(totalCost: totalCost)
        Date deliveryDate = new Date()
        dateUtils.getCurrentDate() >> deliveryDate

        when:
        deliveryService.createDelivery(order)

        then:
        1 * deliveryRequestRepository.saveDeliveryRequest(_ as DeliveryRequest) >> {DeliveryRequest deliveryRequest ->
            assert courier == deliveryRequest.courier
        }

        where:
        totalCost   | courier      | costDesc
        5           | Courier.JRS  | "less than 20"
        25          | Courier.GRAB | "greater than or equal to 20 and less than or equal to 30"
        35          | Courier.LBC  | "greater than 30"
    }
}
