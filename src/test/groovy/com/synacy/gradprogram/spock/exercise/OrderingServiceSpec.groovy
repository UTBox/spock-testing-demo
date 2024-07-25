package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {
    OrderingService orderingService

    void setup(){
        orderingService = new OrderingService()
    }

    def "cartContainsFoodItem should return #expectedHasFood when cart contains #description"() {
        when:
        boolean actualHasFood = orderingService.cartContainsFoodItem(cart)

        then:
        expectedHasFood == actualHasFood

        where:
        cart                            | expectedHasFood   | description
        createGadgetCartWith2Items(1)   | false             | "non-food items"
        createFoodCartWith2Items(1)     | true              | "food items"
        createMixedCartWith2Items(1)    | true              | "at least one food item"
    }

    def "isCartEligibleForDiscount should return #expectedEligibility when the total cost of cart #costDesc and # of items #itemsDesc"() {
        when:
        boolean actualEligibility = orderingService.isCartEligibleForDiscount(cart)

        then:
        expectedEligibility == actualEligibility

        where:
        cart                                | expectedEligibility   | costDesc | itemsDesc
        createFoodCartWith2Items(1)         | false                 | "<= 50"  | "< 6"
        createFoodCartWith2Items(26)        | false                 | "> 50"   | "< 6"
        createFoodCartWith6Items(1)         | false                 | "<= 50"  | "> 5"
        createFoodCartWith6Items(50)        | true                  | "> 50"   | "> 5"
    }

    def "calculateTotalCostOfCart should return the total cost of the items in the cart"() {
        given:
        Cart cart = createMixedCartWith2Items(cost)

        when:
        double actualTotal = orderingService.calculateTotalCostOfCart(cart)

        then:
        expectedTotal == actualTotal

        where:
        cost   | expectedTotal
        1d     | 2d
        50d    | 100d
    }

    def "applyDiscountToCartItems should #behaviorDesc when the total cost of cart #costDesc and # of items #itemsDesc\""() {
        when:
        orderingService.applyDiscountToCartItems(cart)

        then:
        expectedCost == cart.getItems()[0].getCost()
        expectedCost == cart.getItems()[1].getCost()

        where:
        cart                                | expectedCost   | behaviorDesc         | costDesc | itemsDesc
        createFoodCartWith2Items(1)         | 1d             | "not apply discount" | "<= 50"  | "< 6"
        createFoodCartWith2Items(26)        | 26d            | "not apply discount" | "> 50"   | "< 6"
        createFoodCartWith6Items(1)         | 1d             | "not apply discount" | "<= 50"  | "> 5"
        createFoodCartWith6Items(50)        | 45d            | "apply discount"     | "> 50"   | "> 5"
    }

    def "createAnOrder should return an order if order #desc"() {
        given:
        String recipientName = "Juan"
        String recipientAddress = "Lahug"
        double totalCost = 2
        OrderStatus status = OrderStatus.PENDING

        when:
        Order order = orderingService.createAnOrder(cart, recipientName, recipientAddress, canContainFood)

        then:
        totalCost == order.totalCost
        recipientName == order.recipientName
        recipientAddress == order.recipientAddress
        status == order.status

        where:
        cart                                | canContainFood    | desc
        createFoodCartWith2Items(1)         | true              | "can contain food and only has food items"
        createMixedCartWith2Items(1)        | true              | "can contain food and has at least one food item"
        createGadgetCartWith2Items(1)       | true              | "can contain food but does not have a food item"
        createGadgetCartWith2Items(1)       | false             | "cannot contain food and does not have a food item"
    }

    def "createAnOrder should throw an UnableToCreateOrderException if order cannot contain food but has a food item"(){
        given:
        Cart cartWithFood = createFoodCartWith2Items(1)
        String recipientName = "Juan"
        String recipientAddress = "Lahug"

        when:
        orderingService.createAnOrder(cartWithFood, recipientName, recipientAddress, false)

        then:
        thrown(UnableToCreateOrderException)
    }

    Cart createFoodCartWith2Items(double cost){
        UUID uuid = UUID.randomUUID()
        return new Cart(uuid, [
            new Item("Cabbage", cost, ItemType.FOOD ),
            new Item("Cabbage", cost, ItemType.FOOD )
        ])
    }

    Cart createFoodCartWith6Items(double cost){
        UUID uuid = UUID.randomUUID()
        return new Cart(uuid, [
            new Item("Cabbage", cost, ItemType.FOOD ),
            new Item("Cabbage", cost, ItemType.FOOD ),
            new Item("Cabbage", cost, ItemType.FOOD ),
            new Item("Cabbage", cost, ItemType.FOOD ),
            new Item("Cabbage", cost, ItemType.FOOD ),
            new Item("Cabbage", cost, ItemType.FOOD )
        ])
    }

    Cart createGadgetCartWith2Items(double cost){
        UUID uuid = UUID.randomUUID()
        return new Cart(uuid, [
            new Item("Mouse", cost, ItemType.GADGET),
            new Item("Mouse", cost, ItemType.GADGET)
        ])
    }

    Cart createMixedCartWith2Items(double cost){
        UUID uuid = UUID.randomUUID()
        return new Cart(uuid, [
            new Item("Cabbage", cost, ItemType.FOOD ),
            new Item("Mouse", cost, ItemType.GADGET)
        ])
    }
}
