package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {

    String foodItemName
    String applianceItemName
    String clothingItemName
    String gadgetItemName
    double foodItemCost
    double applianceItemCost
    double clothingItemCost
    double gadgetItemCost
    ItemType foodItemType
    ItemType applianceItemType
    ItemType clothingItemType
    ItemType gadgetItemType

    Item foodItem
    Item applianceItem
    Item clothingItem
    Item gadgetItem

    UUID uuid

    OrderingService orderingService

    void setup() {
        orderingService = new OrderingService()

        foodItemName = "food"
        applianceItemName = "appliance"
        clothingItemName = "clothing"
        gadgetItemName = "gadget"
        foodItemCost = 10.00
        applianceItemCost = 1500.00
        clothingItemCost = 999.00
        gadgetItemCost = 3000.00
        foodItemType = ItemType.FOOD
        applianceItemType = ItemType.APPLIANCE
        clothingItemType = ItemType.CLOTHING
        gadgetItemType = ItemType.GADGET

        foodItem = new Item(foodItemName, foodItemCost, foodItemType)
        applianceItem = new Item(applianceItemName, applianceItemCost, applianceItemType)
        clothingItem = new Item(clothingItemName, clothingItemCost, clothingItemType)
        gadgetItem = new Item(gadgetItemName, gadgetItemCost, gadgetItemType)

        uuid = UUID.randomUUID()
    }

    // TODO: Use "where:" to combine cartContainsFoodItem tests and createAnOrder tests into singular tests for each respective methods
    def "CartContainsFoodItem should return true if has food item"() {
        given:
        List<Item> items = new ArrayList<>()
        items.add(foodItem)
        Cart cart = new Cart(uuid, items)
        cart.addItem(foodItem)

        boolean expected = true

        when:
        boolean actual = orderingService.cartContainsFoodItem(cart)

        then:
        expected == actual
    }

    def "CartContainsFoodItem should return false if no food item"() {
        given:
        List<Item> items = new ArrayList<>()
        items.add(applianceItem)
        Cart cart = new Cart(null, items)
        cart.addItem(applianceItem)

        boolean expected = false

        when:
        boolean actual = orderingService.cartContainsFoodItem(cart)

        then:
        expected == actual
    }

    def "CalculateTotalCostOfCart should return total cost of items in cart"() {
        given:
        List<Item> items = new ArrayList<>()
        items.add(foodItem)
        items.add(applianceItem)
        Cart cart = new Cart(uuid, items)

        double expected = 1510.00

        when:
        double actual = orderingService.calculateTotalCostOfCart(cart)

        then:
        expected == actual
    }

    def "IsCartEligibleForDiscount should return true if cart total amount greater than discount amount threshold and number of items greater than 5"() {
        given:
        List<Item> items = new ArrayList<>()
        items.add(foodItem)
        items.add(foodItem)
        items.add(foodItem)
        items.add(applianceItem)
        items.add(gadgetItem)
        items.add(clothingItem)
        Cart cart = new Cart(uuid, items)

        boolean expected = true

        when:
        boolean actual = orderingService.isCartEligibleForDiscount(cart)

        then:
        expected == actual
    }

    def "ApplyDiscountToCartItems should apply 10% discount to items in cart if IsCartEligibleForDiscount returns true"() {
        given:
        List<Item> items = new ArrayList<>()
        items.add(new Item(foodItemName, foodItemCost, foodItemType))
        items.add(new Item(foodItemName, foodItemCost, foodItemType))
        items.add(new Item(foodItemName, foodItemCost, foodItemType))
        items.add(new Item(foodItemName, foodItemCost, foodItemType))
        items.add(new Item(foodItemName, foodItemCost, foodItemType))
        items.add(new Item(foodItemName, foodItemCost, foodItemType))
        Cart cart = new Cart(uuid, items)

        double expected = 9

        when:
        orderingService.applyDiscountToCartItems(cart)
        double actual = cart.items.get(0).getCost()

        then:
        expected == actual
    }

    def "CreateAnOrder returns Order object if canContainFood is true"() {
        given:
        double totalCost = 5509
        String recipientName = "John Doe"
        String recipientAddress = "Rhode Island"
        boolean canContainFood = true

        List<Item> items = new ArrayList<>()
        items.add(foodItem)
        items.add(applianceItem)
        items.add(gadgetItem)
        items.add(clothingItem)
        Cart cart = new Cart(uuid, items)

        when:
        def order = orderingService.createAnOrder(cart, recipientName, recipientAddress, canContainFood)

        then:
        order.totalCost == totalCost
        order.recipientName == recipientName
        order.recipientAddress == recipientAddress
        order.status == OrderStatus.PENDING
    }

    def "CreateAnOrder throws UnableToCreateOrderException if canContainFood is false"() {
        given:
        String recipientName = "John Doe"
        String recipientAddress = "Rhode Island"
        boolean canContainFood = false

        List<Item> items = new ArrayList<>()
        items.add(foodItem)
        items.add(applianceItem)
        items.add(gadgetItem)
        items.add(clothingItem)
        Cart cart = new Cart(uuid, items)

        when:
        orderingService.createAnOrder(cart, recipientName, recipientAddress, canContainFood)

        then:
        thrown(UnableToCreateOrderException)
    }
}
