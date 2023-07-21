package com.synacy.gradprogram.spock.exercise

import com.synacy.gradprogram.spock.demo.User
import com.synacy.gradprogram.spock.demo.UserValidatorService
import spock.lang.Specification

class ShoppingServiceSpec extends Specification {

    ShoppingService service

    OrderingService orderingService = Mock(OrderingService)
    DeliveryService deliveryService = Mock(DeliveryService)
    OrderRepository orderRepository = Mock(OrderRepository)
    DeliveryRequestRepository deliveryRequestRepository = Mock(DeliveryRequestRepository)



    def setup() {
        service = new ShoppingService()
    }

    def "buyNonSpoilingItemsInCart should be able to create delivery order"(){
        given:
            User user = new User()
            String firstname = user.setFirstName("Clark")
            String lastname = user.setLastName("Tabar")

            String recipient = firstname.concat("").concat(lastname)

            Order order


    }


}
