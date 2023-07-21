package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class DeliveryServiceSpec extends Specification {

    DeliveryService service

    DateUtils dateUtils = Mock(DateUtils)
    DeliveryRequestRepository deliveryRequestRepository = Mock(DeliveryRequestRepository)

    def setup() {
        service = new DeliveryService(dateUtils, deliveryRequestRepository)
    }

    def "createDelivery should be able to save the created delivery request."(){
        given:
        Order order = new Order()

        UUID id = order.getId()
        DateUtils deliveryDate = dateUtils.getCurrentDate()
        Courier courier = Courier.JRS

        when:
        service.createDelivery(order)

        then:
        1 * deliveryRequestRepository.saveDeliveryRequest(_) >> { DeliveryRequest deliveryRequest ->
            assert id == deliveryRequest.getOrderId()
            assert deliveryDate == deliveryRequest.getDeliveryDate()
            assert courier == deliveryRequest.getCourier()
        }
    }

    def "determineCourier should be able to determine on which Courier the order will be assigned."(){
        when:
        Courier actualCourier = service.determineCourier(orderTotalCost)

        then:
        expectedCourier == actualCourier

        where:
        orderTotalCost  | expectedCourier
        10              | Courier.JRS
        22              | Courier.GRAB
        54              | Courier.LBC


    }
}
