package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification
import spock.lang.Unroll

class DeliveryServiceSpec extends Specification {

    DeliveryRequestRepository deliveryRequestRepository
    Order order
    DeliveryService deliveryService
    DateUtils dateUtils

    void setup() {
        deliveryRequestRepository = Mock(DeliveryRequestRepository)
        order = Mock(Order)
        dateUtils = Mock(DateUtils)
        deliveryService = new DeliveryService(dateUtils, deliveryRequestRepository)
    }

    def "CreateDelivery should save OrderID, DeliveryDate and Courier to deliveryRequestRepository"() {
        given:
        UUID expectedId = UUID.randomUUID()
        order.getId() >> expectedId

        def expectedDeliveryDate = new Date()
        dateUtils.getCurrentDate() >> expectedDeliveryDate

        order.getTotalCost() >> cost

        when:
        deliveryService.createDelivery(order)

        then:
        1 * deliveryRequestRepository.saveDeliveryRequest(_) >> { DeliveryRequest deliveryRequest ->
            assert expectedId == deliveryRequest.getOrderId()
            assert expectedDeliveryDate == deliveryRequest.getDeliveryDate()
            assert expectedCourier == deliveryRequest.getCourier()
        }

        where:
        cost     | expectedCourier
        15       | Courier.JRS
        25       | Courier.GRAB
        35       | Courier.LBC
    }
}
