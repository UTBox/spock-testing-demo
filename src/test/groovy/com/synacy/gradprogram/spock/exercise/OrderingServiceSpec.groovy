package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {
    OrderingService orderingService

    void setup(){
        orderingService = new OrderingService()
    }

    def "cartContainsFoodItem should return true when cart contains #description"() {
        given:
        Cart foodCart = createFoodCartWith2Items(1)
        Cart gadgetCart = createGadgetCartWith2Items(1)

        Cart mixedCart = new Cart(UUID.randomUUID(), new ArrayList<Item>())
        mixedCart.addItem(createFoodItem(1))
        mixedCart.addItem(createGadgetItem(1))

        when:
        boolean actualHasFood = orderingService.cartContainsFoodItem(cart)

        then:
        expectedHasFood == actualHasFood

        where:
        cart                            | expectedHasFood   | description
        createGadgetCartWith2Items(1)   | false             | "non-food items"
        createFoodCartWith2Items(1)     | true              | "food items"
        createMixedCartWith2Items(1)    | true              | "mixed type items"
    }

    def "isCartEligibleForDiscount should return #expectedEligibility when the total cost of cart #costDesc and # of items #itemsDesc"() {
        when:
        boolean actualEligibility = orderingService.isCartEligibleForDiscount(cart)

        then:
        expectedEligibility == actualEligibility

        where:
        cart                                | expectedEligibility   | costDesc | itemsDesc
        createFoodCartWith2Items(1)         | false                 | "<= 50"  |"< 6"
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
        cost | expectedTotal
        1     | 2
        50    | 100
    }

    def "applyDiscountToCartItems should #behaviorDesc to items if cart is #eligibilityDesc"() {
        when:
        orderingService.applyDiscountToCartItems(cart)

        then:
        println(cart.getItems()[0].getCost())
        expectedCost == cart.getItems()[0].getCost()
        expectedCost == cart.getItems()[1].getCost()

        where:
        cart                                | expectedCost  | behaviorDesc  | eligibilityDesc
        createFoodCartWith2Items(1)         | 1             | "not apply discount" | "not eligible"
        createFoodCartWith2Items(26)        | 26            | "not apply discount" | "not eligible"
        createFoodCartWith6Items(1)         | 1             | "not apply discount" | "not eligible"
        createFoodCartWith6Items(50)        | 45            | "apply discount"     | "eligible"
    }

    def "createAnOrder should return an order if order #desc"() {
        given:
        def recipientName = "Juan"
        def recipientAddress = "Lahug"
        def totalCost = 2
        def status = OrderStatus.PENDING

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
        def recipientName = "Juan"
        def recipientAddress = "Lahug"
        def totalCost = 2
        def status = OrderStatus.PENDING

        when:
        Order order = orderingService.createAnOrder(cartWithFood, recipientName, recipientAddress, false)

        then:
        thrown(UnableToCreateOrderException)
    }

    def Item createFoodItem(cost){
        return new Item("Cabbage", cost, ItemType.FOOD )
    }

    def Item createGadgetItem(cost){
        return new Item("Mouse", cost, ItemType.GADGET)
    }

    def Cart createFoodCartWith2Items(cost){
        UUID uuid = UUID.randomUUID()
        Cart cart = new Cart(uuid, new ArrayList<Item>())

        cart.addItem(createFoodItem(cost))
        cart.addItem(createFoodItem(cost))

        return cart
    }

    def Cart createFoodCartWith6Items(cost){
        UUID uuid = UUID.randomUUID()
        Cart cart = new Cart(uuid, new ArrayList<Item>())

        cart.addItem(createFoodItem(cost))
        cart.addItem(createFoodItem(cost))
        cart.addItem(createFoodItem(cost))
        cart.addItem(createFoodItem(cost))
        cart.addItem(createFoodItem(cost))
        cart.addItem(createFoodItem(cost))

        return cart
    }

    def Cart createGadgetCartWith2Items(cost){
        UUID uuid = UUID.randomUUID()
        Cart cart = new Cart(uuid, new ArrayList<Item>())

        cart.addItem(createGadgetItem(cost))
        cart.addItem(createGadgetItem(cost))

        return cart
    }

    def Cart createGadgetCartWith6Items(cost){
        UUID uuid = UUID.randomUUID()
        Cart cart = new Cart(uuid, new ArrayList<Item>())

        cart.addItem(createGadgetItem(cost))
        cart.addItem(createGadgetItem(cost))
        cart.addItem(createGadgetItem(cost))
        cart.addItem(createGadgetItem(cost))
        cart.addItem(createGadgetItem(cost))
        cart.addItem(createGadgetItem(cost))

        return cart
    }

    def Cart createMixedCartWith2Items(cost){
        UUID uuid = UUID.randomUUID()
        Cart cart = new Cart(uuid, new ArrayList<Item>())

        cart.addItem(createFoodItem(cost))
        cart.addItem(createGadgetItem(cost))

        return cart
    }
}
