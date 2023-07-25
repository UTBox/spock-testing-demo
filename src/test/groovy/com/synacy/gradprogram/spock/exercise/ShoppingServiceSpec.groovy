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
        service = new ShoppingService(orderingService, deliveryService,
                orderRepository, deliveryRequestRepository)
    }

    def "buyNonSpoilingItemsInCart should be able to create delivery order"(){
        given:
        Item nuggets = new Item("Nuggets", 50.0, ItemType.FOOD)
        Item ham = new Item("Ham", 80.0, ItemType.FOOD)
        Item biscuit = new Item("Biscuit", 50.0, ItemType.GADGET)
        Item appleJuice = new Item("Apple Juice", 150.0, ItemType.FOOD)
        Item milk = new Item("Milk", 100.0, ItemType.FOOD)
        Item chocolate = new Item("Chocolate", 300.0, ItemType.FOOD)

        List<Item> items = [nuggets, ham, biscuit, appleJuice, milk, chocolate]

        Cart itemsInTheCart = new Cart(UUID.randomUUID(), items)

        User user = Mock(User)
        user.getFirstName() >> "Clark"
        user.getLastName() >> "Tabar"
        user.getAddress() >> "Cebu"

        String recipientName = user.getFirstName().concat(" ").concat(user.getLastName())

        Order order = orderingService.createAnOrder(itemsInTheCart, recipientName, user.getAddress(), false)

        when:
        service.buyNonSpoilingItemsInCart(itemsInTheCart, user)

        then:
        1 * deliveryService.createDelivery(order)

    }

    def "getOrderSummary should get the summary of the order."(){
        given:
        UUID orderId = UUID.randomUUID()

        Order order = Mock(Order)
        order.getId() >> orderRepository.fetchOrderById(orderId)
        order.getTotalCost() >> 500.0
        order.getStatus() >> OrderStatus.PENDING

        DeliveryRequest deliveryRequest = Mock(DeliveryRequest)
        deliveryRequest.getOrderId() >> deliveryRequestRepository.fetchDeliveryRequestByOrderId(orderId)
        DateUtils dateNow = Mock (DateUtils)
        deliveryRequest.getDeliveryDate() >>  new Date()
        deliveryRequest.getCourier() >> Courier.LBC



        when:
        service.getOrderSummary(orderId)

        then:
        deliveryRequest.getCourier() == OrderSummary(deliveryRequest.getCourier())
    }



}
