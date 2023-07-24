package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification


class DeliveryServiceSpec extends Specification {

    DeliveryService deliveryService
    DeliveryRequestRepository deliveryRequestRepository = Mock(DeliveryRequestRepository)
    DateUtils dateUtils = Mock(DateUtils)

    void setup(){
        deliveryService = new DeliveryService(deliveryRequestRepository, dateUtils)
    }

    def "Should Create Delivery Request"() {
        given:
        Order order = new Order()
        dateUtils.getCurrentDate() >> new Date()

        when:
        DeliveryRequest result = deliveryService.createDelivery(order)
        then:
        1 * deliveryRequestRepository.saveDeliveryRequest(_)

        and:
        result.orderId == order.id
        result.courier == Courier.JRS
        result.deliveryDate == dateUtils.getCurrentDate()
    }
    def "Should return JRS for order total cost less than 20"(){
       when:
       Courier courier = deliveryService.determineCourier(15)

        then:
        courier == Courier.JRS
    }


    def "Should return LBC for order total cost greater than 30"() {
        when:
        Courier courier = deliveryService.determineCourier(40)

        then:
        courier == Courier.LBC
    }

    }