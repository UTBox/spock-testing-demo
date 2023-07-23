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
        Item nuggets = new Item("Nuggets", 50.0, ItemType.FOOD)
        Item ham = new Item("Ham", 80.0, ItemType.FOOD)
        Item biscuit = new Item("Biscuit", 50.0, ItemType.GADGET)
        Item appleJuice = new Item("Apple Juice", 150.0, ItemType.FOOD)
        Item milk = new Item("Milk", 100.0, ItemType.FOOD)
        Item chocolate = new Item("Chocolate", 300.0, ItemType.FOOD)

        List<Item> items = [nuggets, ham, biscuit, appleJuice, milk, chocolate]

        Cart itemsInTheCart = new Cart(UUID.randomUUID(), items)

        User user = new User()

        user.setFirstName("Clark")
        user.setLastName("Tabar")
        String address = "Cebu"



        //boolean containsFoodItems =  orderingService.cartContainsFoodItem(false)

        String recipient = user.getFirstName().concat(" ").concat(user.getLastName())
        Order createOrder = orderingService.createAnOrder (itemsInTheCart, recipient, address, false)



        when:
        service.buyNonSpoilingItemsInCart(itemsInTheCart, user)

        then:
        1 * deliveryService.createDelivery(_){ DeliveryRequestRepository deliveryRequestRepository ->
            assert createOrder == orderingService.createAnOrder()
            assert recipient == createOrder.getRecipientName()

        }

    }




}
