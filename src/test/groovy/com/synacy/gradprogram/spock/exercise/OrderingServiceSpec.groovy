package com.synacy.gradprogram.spock.exercise


import spock.lang.Specification
import spock.lang.Subject

class OrderingServiceSpec extends Specification {


    OrderingService orderingService
    Cart cart

    def setup() {
        orderingService = new OrderingService()
    }

    def "test if the Items will be stored, return true if all the items were stored"(){
     given:
        def items = [
                new Item("Charger", 100.00, ItemType.GADGET),
                new Item("Vanz Tshirt", 160.00, ItemType.CLOTHING),
                new Item("Oven", 800.00, ItemType.APPLIANCE),
                new Item("555 Tuna", 25.00, ItemType.FOOD),
                new Item("Pork Chop", 150.00, ItemType.FOOD)
        ]
        when:
        boolean ifcontainsFoodType = orderingService.cartContainsFoodItem(cart = new Cart(UUID.randomUUID(), items))

        then:
        ifcontainsFoodType
    }

    def "Should Determine the Total Cost of the Cart is true"(){
        given:
        def totalItems = [
                new Item("555 Tuna", 50.00, ItemType.FOOD),
                new Item("Pork Chop", 150.00, ItemType.FOOD),
                new Item("Charger", 100.00, ItemType.GADGET),
                new Item("Vanz Tshirt", 160.00, ItemType.CLOTHING),
                new Item("Oven", 800.00, ItemType.APPLIANCE)
        ]
        when:
        double totalPrice = orderingService.calculateTotalCostOfCart(cart = new Cart(UUID.randomUUID(),totalItems))

        then:
        println(totalPrice)
        totalPrice == 1260.00

    }


    def "Should return the items is Eligible for a Discount"(){
        given:
        def items = [
                new Item("555 Tuna", 50.00, ItemType.FOOD),
                new Item("Pork Chop", 150.00, ItemType.FOOD),
                new Item("Charger", 100.00, ItemType.GADGET),
                new Item("Vanz Tshirt", 160.00, ItemType.CLOTHING),
                new Item("Oven", 800.00, ItemType.APPLIANCE),
                new Item("Fan", 200.00, ItemType.APPLIANCE),
        ]
        when:
        boolean eligibleForDiscount = orderingService.isCartEligibleForDiscount(cart = new Cart(UUID.randomUUID(),items))


        then:
        println "Total Price of Cart: ${orderingService.calculateTotalCostOfCart(cart)}"
        println "Number of Items in Cart: ${cart.getItems().size()}"
        eligibleForDiscount
    }

    def "Apply discount if the Cart is Eligble for discount"(){
        given:
        def items = [
                new Item("555 Tuna", 50.00, ItemType.FOOD),
                new Item("Pork Chop", 150.00, ItemType.FOOD),
                new Item("Charger", 100.00, ItemType.GADGET),
                new Item("Vanz Tshirt", 160.00, ItemType.CLOTHING),
                new Item("Oven", 800.00, ItemType.APPLIANCE),
                new Item("Fan", 200.00, ItemType.APPLIANCE),
        ]
        when:
        double originalTotalCost = orderingService.calculateTotalCostOfCart(cart = new Cart(UUID.randomUUID(), items))
        orderingService.applyDiscountToCartItems(cart)

        then:
        orderingService.isCartEligibleForDiscount(cart) == true
        double discountedTotalCost = (originalTotalCost * 0.90)

        println "Total Price: $originalTotalCost"
        println("")
        println "Total Discounted Price: $discountedTotalCost"
    }

    def "Should not Apply discount if the Cart is not Eligble for discount"(){
        given:
        def items = [
                new Item("555 Tuna", 50.00, ItemType.FOOD),
                new Item("Pork Chop", 150.00, ItemType.FOOD),
                new Item("Charger", 100.00, ItemType.GADGET),
                new Item("Vanz Tshirt", 160.00, ItemType.CLOTHING),
                new Item("Oven", 800.00, ItemType.APPLIANCE)
        ]
        when:
        double originalTotalCost = orderingService.calculateTotalCostOfCart(cart = new Cart(UUID.randomUUID(), items))
        orderingService.applyDiscountToCartItems(cart)

        then:
        orderingService.isCartEligibleForDiscount(cart) == false
        double discountedTotalCost = originalTotalCost

        println "Total Price: $originalTotalCost"
        println("")
        println "Total Discounted Price: $discountedTotalCost"
    }

    def "Should create an order when the cart does not contain food items and canContainFood is true"(){
        given:
        def items = [
                new Item("555 Tuna", 50.00, ItemType.FOOD),
                new Item("Pork Chop", 150.00, ItemType.FOOD),
                new Item("Charger", 100.00, ItemType.GADGET),
                new Item("Vanz Tshirt", 160.00, ItemType.CLOTHING),
                new Item("Oven", 800.00, ItemType.APPLIANCE)
        ]
        cart = new Cart(UUID.randomUUID(), items)

        String recipientName = "Ernest Pogi"
        String recipientAddress = "Cebu City"
        boolean canContainFood = true

        when:
        Order order = orderingService.createAnOrder(cart, recipientName, recipientAddress, canContainFood)

        then:
        order != null
        order.getTotalCost() == orderingService.calculateTotalCostOfCart(cart)
        order.getRecipientName() == recipientName
        order.getRecipientAddress() == recipientAddress
        order.getStatus() == OrderStatus.PENDING

    }

    def "Should throw UnableToCreateOrderException when the cart contains food items and canContainFood is false"() {
        given:
        def items = [
                new Item("555 Tuna", 50.00, ItemType.FOOD),
                new Item("Pork Chop", 150.00, ItemType.FOOD),
                new Item("Charger", 100.00, ItemType.GADGET),
                new Item("Vanz Tshirt", 160.00, ItemType.CLOTHING),
                new Item("Oven", 800.00, ItemType.APPLIANCE)
        ]
        cart = new Cart(UUID.randomUUID(), items)
        String recipientName = "Ernest Dina Pogi"
        String recipientAddress = "Cebu City"
        boolean canContainFood = false


        when:
        orderingService.createAnOrder(cart, recipientName, recipientAddress, canContainFood)

        then:
        UnableToCreateOrderException exception = thrown(UnableToCreateOrderException)
        exception.message == "Cart should not contain FOOD items"
    }

}




