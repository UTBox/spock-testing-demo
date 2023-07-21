package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {

    OrderingService service
    OrderRepository orderRepository = Mock(OrderRepository)
    
    def setup() {

        service = new OrderingService(orderRepository)

    }

    def "cartContainsFoodItem should verify if the cart has food items."() {
        given:
            Item nuggets = new Item("Nuggets", 185, ItemType.FOOD)
            Item ham = new Item("Ham", 198, ItemType.FOOD)
            Item milk = new Item("Milk", 29990, ItemType.GADGET)
            Item appleJuice = new Item("Apple Juice", 174, ItemType.FOOD)

            List<Item> items = [nuggets, ham, milk, appleJuice]

            Cart itemsInTheCart = new Cart(UUID.randomUUID(), items)

        when:
            boolean hasFoodItems = service.cartContainsFoodItem(itemsInTheCart)

        then:
            hasFoodItems == true
    }

    def "calculateTotalCostOfCart should get the sum of totalPrice."() {
        given:
            Item nuggets = new Item("Nuggets", 100, ItemType.FOOD)
            Item ham = new Item("Ham", 100, ItemType.FOOD)
            Item biscuit = new Item("Biscuit", 50, ItemType.GADGET)
            Item appleJuice = new Item("Apple Juice", 50, ItemType.FOOD)

            List<Item> items = [nuggets, ham, biscuit, appleJuice]

            Cart itemsInTheCart = new Cart(UUID.randomUUID(), items)

        when:
            double actualTotalPrice = service.calculateTotalCostOfCart(itemsInTheCart)

        then:
            300 == actualTotalPrice

    }

    def "isCartEligibleForDiscount should validate if the total price from the cart is eligible for discount."() {
        given:
        Item nuggets = new Item("Nuggets", 185.0, ItemType.FOOD)
        Item ham = new Item("Ham", 198.0, ItemType.FOOD)
        Item biscuit = new Item("Biscuit", 49.0, ItemType.GADGET)
        Item appleJuice = new Item("Apple Juice", 174.0, ItemType.FOOD)
        Item milk = new Item("Milk", 98.0, ItemType.FOOD)
        Item chocolate = new Item("Chocolate", 357.0, ItemType.FOOD)

        List<Item> items = [nuggets, ham, biscuit, appleJuice, milk, chocolate]

        Cart itemsInTheCart = new Cart(UUID.randomUUID(), items)

        when:
        boolean eligibleForDiscount = service.isCartEligibleForDiscount(itemsInTheCart)

        then:
        eligibleForDiscount == true

    }

    def "applyDiscountToCartItems should apply discount when cart is eligible for discount."() {
        given:
        Item nuggets = new Item("Nuggets", 185.0, ItemType.FOOD)
        Item ham = new Item("Ham", 198.0, ItemType.FOOD)
        Item biscuit = new Item("Biscuit", 49.0, ItemType.GADGET)

        List<Item> items = [nuggets, ham, biscuit]

        Cart itemsInTheCart = new Cart(UUID.randomUUID(), items)

        when:
        boolean eligibleForDiscount = service.isCartEligibleForDiscount(itemsInTheCart)

        then:
        !eligibleForDiscount

    }

    def "createAnOrder should be able to save the created order."() {
        given:
        Item iphone = new Item("iPhone", 62000.0, ItemType.GADGET)
        Item macBook = new Item("MacBook", 78000.0, ItemType.GADGET)
        Item mouse = new Item("Mouse", 300.0, ItemType.GADGET)
        Item keyboard = new Item("Keyboard", 400.0, ItemType.GADGET)
        Item headphone = new Item("Headphone", 800.0, ItemType.GADGET)
        Item airpods = new Item("Airpods", 12999.0, ItemType.GADGET)
        Item ipad = new Item("iPad", 38999.0, ItemType.GADGET)

        List<Item> items = [iphone, macBook, mouse, keyboard, headphone, airpods, ipad]

        Cart itemsInTheCart = new Cart(UUID.randomUUID(), items)


        double totalCost = 193498.0
        String recipientName = "Clark"
        String recipientAddress = "Cebu"
        OrderStatus orderStatus = OrderStatus.PENDING
        boolean containsFood = true

        when:
        service.createAnOrder(itemsInTheCart, recipientName, recipientAddress, containsFood)

        then:
        1 * orderRepository.saveOrder(_) >> { Order order ->
            assert totalCost == order.getTotalCost()
            assert recipientName == order.getRecipientName()
            assert recipientAddress == order.getRecipientAddress()
            assert  orderStatus == order.getStatus()
        }
    }

}
