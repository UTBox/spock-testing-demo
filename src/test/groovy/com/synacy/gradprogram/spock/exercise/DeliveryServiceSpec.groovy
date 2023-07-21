package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class DeliveryServiceSpec extends Specification {
    DeliveryService deliveryService
    DateUtils dateUtils = Mock(DateUtils)
    DeliveryRequestRepository deliveryRequestRepository = Mock(DeliveryRequestRepository)

    Order order = Mock(Order)
    void setup() {
        deliveryService = new DeliveryService(dateUtils, deliveryRequestRepository)
    }

    def "CreateDelivery should respond with delivery request and save order ID, courier, and current date to delivery repository"() {
        given:
        UUID id = UUID.randomUUID()
        order.getId() >> id
        Date deliveryDate = new Date()
        dateUtils.getCurrentDate() >> deliveryDate

        order.getTotalCost() >> totalCost

        when:
        deliveryService.createDelivery(order)

        then:
        1 * deliveryRequestRepository.saveDeliveryRequest(_) >> {DeliveryRequest deliveryRequest->
            assert id == deliveryRequest.getOrderId()
            assert deliveryDate == deliveryRequest.getDeliveryDate()
            assert courier == deliveryRequest.getCourier()
        }

        where:
        totalCost   | courier
        19          | Courier.JRS
        20          | Courier.GRAB
        31          | Courier.LBC

    }
}
