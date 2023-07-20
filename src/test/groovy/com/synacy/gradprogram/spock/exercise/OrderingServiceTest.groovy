package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceTest extends Specification {

    OrderingService orderingService
    OrderRepository orderRepository

    Item foodItem = new Item("bread", 10.0, ItemType.FOOD)
    Item applianceItem = new Item("toaster", 500.0, ItemType.APPLIANCE)
    Item clothingItem = new Item("shirt", 200.0, ItemType.CLOTHING)
    Item gadgetItem = new Item("phone", 1000.0, ItemType.GADGET)

    Item foodItemLowCost = new Item("small bread", 1.0, ItemType.FOOD)
    Item applianceItemLowCost = new Item("small toaster", 5.0, ItemType.APPLIANCE)
    Item clothingItemLowCost = new Item("small shirt", 2.0, ItemType.CLOTHING)
    Item gadgetItemLowCost = new Item("small phone", 10.0, ItemType.GADGET)


    def setup() {
        orderRepository = new OrderRepository()
        orderingService = new OrderingService(orderRepository)
    }

    def "cartContainsFoodItem should return true if Cart has Item with type FOOD"() {
        given:
        List itemListWithEachType = [foodItem, applianceItem, clothingItem, gadgetItem]
        Cart cartWithEachItemType = new Cart(UUID.randomUUID(), itemListWithEachType)
        boolean hasFoodItemExpected = true

        when:
        boolean hasFoodItemActual = orderingService.cartContainsFoodItem(cartWithEachItemType)

        then:
        hasFoodItemExpected == hasFoodItemActual
    }

    def "cartContainsFoodItem should return false if Cart has no Item with type FOOD"() {
        given:
        List itemListWithoutFood = [applianceItem, clothingItem, gadgetItem]
        Cart cartWithoutFoodItem = new Cart(UUID.randomUUID(), itemListWithoutFood)
        boolean hasFoodItemExpected = false

        when:
        boolean hasFoodItemActual = orderingService.cartContainsFoodItem(cartWithoutFoodItem)

        then:
        hasFoodItemExpected == hasFoodItemActual
    }

    def "calculateTotalCostOfCart should return the sum of the cost of each item in cart"() {
        given:
        List itemListWithEachType = [foodItem, applianceItem, clothingItem, gadgetItem]
        Cart cartWithEachItemType = new Cart(UUID.randomUUID(), itemListWithEachType)
        double expectedTotalCost = 1710.0

        when:
        double actualTotalCost = orderingService.calculateTotalCostOfCart(cartWithEachItemType)

        then:
        expectedTotalCost == actualTotalCost
    }

    def "isCartEligibleForDiscount should return true if total cost of cart is greater than 50.0 and items in cart is greater than 5"() {
        given:
        List itemListForDiscount = [foodItem, applianceItem, clothingItem, gadgetItem, foodItemLowCost, applianceItemLowCost, gadgetItemLowCost]
        Cart cartForDiscount = new Cart(UUID.randomUUID(), itemListForDiscount)
        boolean eligibleForDiscountExpected = true

        when:
        boolean eligibleForDiscountActual = orderingService.isCartEligibleForDiscount(cartForDiscount)

        then:
        eligibleForDiscountExpected == eligibleForDiscountActual
    }

    def "isCartEligibleForDiscount should return false if total cost of cart is less than 50.0"() {
        given:
        List itemListWithLowCostManyItems = [foodItem, foodItemLowCost, clothingItemLowCost, applianceItemLowCost, gadgetItemLowCost, foodItemLowCost, foodItemLowCost]
        Cart cartWithLowCostManyItems = new Cart(UUID.randomUUID(), itemListWithLowCostManyItems)
        boolean eligibleForDiscountExpected = false

        when:
        boolean eligibleForDiscountActual = orderingService.isCartEligibleForDiscount(cartWithLowCostManyItems)

        then:
        eligibleForDiscountExpected == eligibleForDiscountActual
    }

    def "isCartEligibleForDiscount should return false if items in cart is less than 5"() {
        given:
        List itemListWithHighCostFewItems = [gadgetItem, applianceItem]
        Cart cartWithHighCostFewItems = new Cart(UUID.randomUUID(), itemListWithHighCostFewItems)
        boolean eligibleForDiscountExpected = false

        when:
        boolean eligibleForDiscountActual = orderingService.isCartEligibleForDiscount(cartWithHighCostFewItems)

        then:
        eligibleForDiscountExpected == eligibleForDiscountActual
    }

    def "applyDiscountToCartItems should set cost of each item in cart to 10 percent of original"() {
        given:
        Item itemForDiscount1 = new Item("cookie", 200.0, ItemType.FOOD)
        Item itemForDiscount2 = new Item("phone", 1000.0, ItemType.GADGET)
        Item itemForDiscount3 = new Item("blender", 5000.0, ItemType.APPLIANCE)
        Item itemForDiscount4 = new Item("jacket", 500.0, ItemType.CLOTHING)
        Item itemForDiscount5 = new Item("biscuit", 20.0, ItemType.FOOD)
        Item itemForDiscount6 = new Item("iphone", 10000.0, ItemType.GADGET)
        List itemListForDiscount = [itemForDiscount1, itemForDiscount2, itemForDiscount3,
                                    itemForDiscount4, itemForDiscount5, itemForDiscount6]
        List expectedCostOfItems = [20.0, 100.0, 500.0, 50.0, 2.0, 1000.0]
        List actualCostOfItems = []

        Cart cartForDiscount = new Cart(UUID.randomUUID(), itemListForDiscount)

        when:
        orderingService.applyDiscountToCartItems(cartForDiscount);
        for (Item discountedItem in cartForDiscount.getItems()) {
            actualCostOfItems.add(discountedItem.getCost())
        }

        then:
        expectedCostOfItems == actualCostOfItems
    }

    def "createAnOrder should throw UnableToCreateOrderException if canContainFood is false and cart contains food"() {
        given:
        List itemListWithEachType = [foodItem, applianceItem, clothingItem, gadgetItem]
        Cart cartForOrder = new Cart(UUID.randomUUID(), itemListWithEachType)
        def canContainFood = false

        when:
        Order order = orderingService.createAnOrder(cartForOrder, "recipientName",
                "recipientAddress", canContainFood)

        then:
        thrown(UnableToCreateOrderException)
    }

    def "createAnOrder should respond with an object Order with correct values"() {
        given:
        List itemListWithEachType = [foodItem, applianceItem, clothingItem, gadgetItem]
        Cart cartForOrder = new Cart(UUID.randomUUID(), itemListWithEachType)
        def canContainFood = true
        def expectedName = "recipientName"
        def expectedAddress = "recipientAddress"
        double expectedTotalCost = 1710.0

        when:
        Order order = orderingService.createAnOrder(cartForOrder, expectedName, expectedAddress, canContainFood)

        then:
        expectedName == order.getRecipientName()
        expectedAddress == order.getRecipientAddress()
        expectedTotalCost == order.getTotalCost()
        OrderStatus.PENDING == order.getStatus()
    }
}