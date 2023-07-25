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
        shoppingService = new ShoppingService(orderingService, deliveryService, orderRepository, deliveryRequestRepository,)
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

 def "OrderSummary Should return a Order Summary"(){
     given:
     UUID orderId = UUID.randomUUID()
     DateUtils dateUtils = Mock(DateUtils)
     Order order = new Order(id: orderId, totalCost: 100.00, status: OrderStatus.PENDING)
     DeliveryRequest deliveryRequest = new DeliveryRequest(orderId: orderId, deliveryDate: new Date(), courier: Courier.LBC)
     dateUtils.getCurrentDate() >> new Date()


     1 * orderRepository.fetchOrderById(orderId) >> order
     1 * deliveryRequestRepository.fetchDeliveryRequestByOrderId(orderId) >> deliveryRequest

     when:
     OrderSummary result = shoppingService.getOrderSummary(orderId)

     then:
     result.totalCost == 100.00
     result.status == OrderStatus.PENDING
     result.courier == Courier.LBC

    }

}
