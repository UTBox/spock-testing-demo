package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {
    OrderingService service

    void setup(){
        service = new OrderingService()
    }

    def "cartContainsFoodItem should return #expectedResult when cart #description"() {
        given:
        Cart cart = new Cart(UUID.randomUUID(),
                [new Item("some item", 10, itemType)])

        when:
        def actualResult = service.cartContainsFoodItem(cart)

        then:
        expectedResult == actualResult

        where:
        itemType        | expectedResult | description
        ItemType.FOOD   | true           | "contains food item"
        ItemType.GADGET | false          | "does not contain food item"
    }

    def "calculateTotalCostOfCart should return the total cost of items in cart"() {
        given:
        Cart cart = new Cart(UUID.randomUUID(),
                [new Item("food", 10, ItemType.FOOD),
                 new Item("gadget", 10, ItemType.GADGET)])

        def expectedTotal = 20 as double

        when:
        def actualTotal = service.calculateTotalCostOfCart(cart)

        then:
        expectedTotal == actualTotal
    }

    def "isCartEligibleForDiscount should return true when total cost and item count exceeds their respective thresholds"() {
        given:
        Cart cart = new Cart(UUID.randomUUID(),
                [new Item("food", 10, ItemType.FOOD),
                 new Item("gadget", 10, ItemType.GADGET),
                 new Item("clothing", 10, ItemType.CLOTHING),
                 new Item("appliance", 10, ItemType.APPLIANCE),
                 new Item("food2", 10, ItemType.FOOD),
                 new Item("gadget2", 10, ItemType.GADGET)],)

        expect:
        true == service.isCartEligibleForDiscount(cart)
    }

    def "isCartEligibleForDiscount should return false when total cost and item count is less than their respective thresholds"() {
        given:
        Cart cart = new Cart(UUID.randomUUID(),
                [new Item("food", 10, ItemType.FOOD),
                 new Item("gadget", 10, ItemType.GADGET),
                 new Item("clothing", 10, ItemType.CLOTHING)])

        def expectedResult = false

        when:
        def actualResult = service.isCartEligibleForDiscount(cart)

        then:
        expectedResult == actualResult
    }

    def "applyDiscountToCartItems should apply discount to each item in cart when cart is eligible for discount"() {
        given:
        Cart cart = new Cart(UUID.randomUUID(),
                [new Item("food", 10, ItemType.FOOD),
                 new Item("gadget", 20, ItemType.GADGET),
                 new Item("clothing", 30, ItemType.CLOTHING),
                 new Item("applicance", 40, ItemType.APPLIANCE),
                 new Item("food2", 50, ItemType.FOOD),
                 new Item("gadget2", 60, ItemType.GADGET)],)

        List<Double> expectedDiscountedCost = [9,18,27,36,45,54]

        when:
        service.applyDiscountToCartItems(cart)

        then:
        verifyAll(cart) {
            expectedDiscountedCost[0] == items[0].cost
            expectedDiscountedCost[1] == items[1].cost
            expectedDiscountedCost[2] == items[2].cost
            expectedDiscountedCost[3] == items[3].cost
            expectedDiscountedCost[4] == items[4].cost
            expectedDiscountedCost[5] == items[5].cost
        }
    }

    def "createAnOrder should throw UnableToCreateOrderException when cart contains food item and canContainFood is false"() {
        given:
        Cart cart = new Cart(UUID.randomUUID(),
                [new Item("food", 10, ItemType.FOOD),
                 new Item("gadget", 10, ItemType.GADGET)])

        when:
        service.createAnOrder(cart, "Sean", "Sogod", false)

        then:
        def exception = thrown(UnableToCreateOrderException)
        exception.message == "Cart should not contain FOOD items"
    }

    def "createAnOrder should return an Order with correct values, and canContainFood should be true if the cart contains a food item"() {
        given:
        Cart cart = new Cart(UUID.randomUUID(),
                [new Item("food", 10, ItemType.FOOD),
                 new Item("gadget", 10, ItemType.GADGET)])

        def expectedTotalCost = 20 as Double
        def expectedRecipientName = "Sean"
        def expectedRecipientAddress = "Sogod"
        def expectedStatus = OrderStatus.PENDING


        when:
        Order actualOrder = service.createAnOrder(cart, "Sean", "Sogod", true)

        then:
        verifyAll(actualOrder) {
            expectedTotalCost == totalCost
            expectedRecipientName == recipientName
            expectedRecipientAddress == recipientAddress
            expectedStatus == status
        }
    }
}
