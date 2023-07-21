package com.synacy.gradprogram.spock.exercise

import com.synacy.gradprogram.spock.demo.User
import org.apache.tools.ant.taskdefs.condition.Or
import spock.lang.Specification


class ShoppingServiceSpec extends Specification {

    ShoppingService shoppingService
    OrderingService orderingService = Mock(OrderingService)
    DeliveryService deliveryService = Mock(DeliveryService)
    OrderRepository orderRepository = Mock(OrderRepository)

    DeliveryRequestRepository deliveryRequestRepository = Mock(DeliveryRequestRepository)

    void setup() {
        shoppingService = new ShoppingService(orderingService, deliveryService, orderRepository, deliveryRequestRepository)
    }

    def "BuyNonSpoilingItemsInCart should create an Order Delivery"() {
        given:
        Cart cart =Mock(Cart)
        Order order = Mock(Order)
        User user = new User(firstName: "Ernest", lastName: "Pogi")


        when:
        shoppingService.buyNonSpoilingItemsInCart(cart,user)

        then:

        1 * orderingService.applyDiscountToCartItems(cart)
        1 * orderingService.createAnOrder(_,_,_,_) >> new Order()
        1 * deliveryService.createDelivery(_ as Order)
    }

   /* def "Should return a Order Summary"(){
        given:
        Order order = Mock(Order)
        UUID orderId = UUID.randomUUID()

        when:
        deliveryRequestRepository.fetchDeliveryRequestByOrderId(orderId: orderId)
    }*/
}