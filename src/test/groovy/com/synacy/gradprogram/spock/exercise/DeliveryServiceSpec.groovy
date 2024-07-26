package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class DeliveryServiceSpec extends Specification {
    DeliveryService deliveryService

    DateUtils dateUtils = Mock(DateUtils)
    DeliveryRequestRepository deliveryRequestRepository = Mock(DeliveryRequestRepository)

    void setup() {
        deliveryService = new DeliveryService(dateUtils, deliveryRequestRepository)
    }

    def "CreateDelivery should save delivery request using the given details and determineCourier should return #expectedCourier based on #totalCost"() {
        given:
        Order order = new Order()
        order.setTotalCost(totalCost)
        UUID expectedOrderId = order.getId()

        when:
        DeliveryRequest actual = deliveryService.createDelivery(order)

        then:
        1 * deliveryRequestRepository.saveDeliveryRequest(_ as DeliveryRequest)
        expectedOrderId == actual.getOrderId()
        expectedCourier == actual.getCourier()

        where:
        totalCost   |   expectedCourier | description
        15          |   Courier.JRS     | "if #totalCost < 20 then #expectedCourier is JRS"
        25          |   Courier.GRAB    | "#totalCost >= 20 and <= 30 then #expectedCourier is GRAB"
        35          |   Courier.LBC     | "#totalCost > 30 then #expectedCourier is LBC"
    }
}
