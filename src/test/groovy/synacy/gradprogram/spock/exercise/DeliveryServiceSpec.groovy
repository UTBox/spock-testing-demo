package synacy.gradprogram.spock.exercise

import com.synacy.gradprogram.spock.exercise.Courier
import com.synacy.gradprogram.spock.exercise.DateUtils
import com.synacy.gradprogram.spock.exercise.DeliveryRequest
import com.synacy.gradprogram.spock.exercise.DeliveryRequestRepository
import com.synacy.gradprogram.spock.exercise.DeliveryService
import com.synacy.gradprogram.spock.exercise.Order
import spock.lang.Specification

class DeliveryServiceSpec extends Specification {

    DeliveryService deliveryService

    DateUtils dateUtils = Mock(DateUtils)
    DeliveryRequestRepository deliveryRequestRepository = Mock(DeliveryRequestRepository)

    void setup() {
        deliveryService = new DeliveryService(dateUtils, deliveryRequestRepository)
    }


    def "createDelivery should save new delivery request given that orderTotalCost is below 20"() {
        given:
        def order = new Order()
        order.setTotalCost(15)

        def deliveryDate = new Date()

        when:
        deliveryService.createDelivery(order)

        then:
        1 * dateUtils.getCurrentDate() >> deliveryDate
        1 * deliveryRequestRepository.saveDeliveryRequest(_) >> { DeliveryRequest savedDeliveryRequest ->
            assert savedDeliveryRequest.getOrderId() == order.getId()
            assert savedDeliveryRequest.getDeliveryDate() == deliveryDate
            assert savedDeliveryRequest.getCourier() == Courier.JRS
        }
    }


    def "createDelivery should save new delivery request given that orderTotalCost is at least 20 but not over 30"() {
        given:
        def order = new Order()
        order.setTotalCost(25)

        def deliveryDate = new Date()

        when:
        deliveryService.createDelivery(order)

        then:
        1 * dateUtils.getCurrentDate() >> deliveryDate
        1 * deliveryRequestRepository.saveDeliveryRequest(_) >> { DeliveryRequest savedDeliveryRequest ->
            assert savedDeliveryRequest.getOrderId() == order.getId()
            assert savedDeliveryRequest.getDeliveryDate() == deliveryDate
            assert savedDeliveryRequest.getCourier() == Courier.GRAB
        }
    }


    def "createDelivery should save new delivery request given that orderTotalCost is over 30"() {
        given:
        def order = new Order()
        order.setTotalCost(40)

        def deliveryDate = new Date()

        when:
        deliveryService.createDelivery(order)

        then:
        1 * dateUtils.getCurrentDate() >> deliveryDate
        1 * deliveryRequestRepository.saveDeliveryRequest(_) >> { DeliveryRequest savedDeliveryRequest ->
            assert savedDeliveryRequest.getOrderId() == order.getId()
            assert savedDeliveryRequest.getDeliveryDate() == deliveryDate
            assert savedDeliveryRequest.getCourier() == Courier.LBC
        }
    }

}
