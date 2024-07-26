package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class DeliveryServiceSpec extends Specification {
    DeliveryService deliveryService

    DeliveryRequestRepository deliveryRequestRepository = Mock()
    DateUtils dateUtils = Mock()

    def setup(){
        deliveryService = new DeliveryService(dateUtils, deliveryRequestRepository)
    }

    def "createDelivery should save a delivery request based on the order ID and date and assign #courier when the total cost is #costDesc"() {
        given:
        UUID id = UUID.randomUUID()

        Order order = Mock()
        order.getId() >> id
        order.getTotalCost() >> totalCost

        Date deliveryDate = new Date()
        dateUtils.getCurrentDate() >> deliveryDate

        when:
        deliveryService.createDelivery(order)

        then:
        1 * deliveryRequestRepository.saveDeliveryRequest(_ as DeliveryRequest) >> {DeliveryRequest deliveryRequest ->
            assert id == deliveryRequest.orderId
            assert deliveryDate == deliveryRequest.deliveryDate
            assert courier == deliveryRequest.courier
        }

        where:
        totalCost   | courier      | costDesc
        5           | Courier.JRS  | "less than 20"
        25          | Courier.GRAB | "greater than or equal to 20 and less than or equal to 30"
        35          | Courier.LBC  | "greater than 30"
    }
}
