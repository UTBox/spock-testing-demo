package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {

    // TODO: Remove global variables
    //       Utilize helper methods
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
    OrderRepository orderRepository = Mock(OrderRepository)

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
    }

    def "cartContainsFoodItem should return true if cart contains food item, false if not"() {
        when:
        boolean actual = orderingService.cartContainsFoodItem(cart)

        then:
        expected == actual

        where:
        cart                          | expected    | resultDescription
        createSimpleCartWithFood()    | true        | "if cart contains food item"
        createSimpleCartWithoutFood() | false       | "if cart does not contain food item"
    }

    def "calculateTotalCostOfCart should return total cost of items in cart"() {
        given:
        Cart cart = createAssortedCart()
        double expectedTotalCost = 18170

        when:
        double actualTotalCost = orderingService.calculateTotalCostOfCart(cart)

        then:
        expectedTotalCost == actualTotalCost
    }

    def "isCartEligibleForDiscount should return true if cart total amount greater than discount amount threshold and number of items greater than 5"() {
        given:
        Cart cart = createAssortedCart()

        boolean expected = true

        when:
        boolean actual = orderingService.isCartEligibleForDiscount(cart)

        then:
        expected == actual
    }

    def "applyDiscountToCartItems should apply 10% discount to items in cart if IsCartEligibleForDiscount returns true"() {
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

    def "createAnOrder returns Order object if canContainFood is true"() {
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

    def "createAnOrder throws UnableToCreateOrderException if canContainFood is false"() {
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

    def Cart createSimpleCartWithFood() {
        Cart cart = new Cart(UUID.randomUUID(),
                [new Item("food", 10, ItemType.FOOD),
                 new Item("gadget", 5000, ItemType.GADGET)])
        return cart
    }

    def Cart createSimpleCartWithoutFood() {
        Cart cart = new Cart(UUID.randomUUID(),
                [new Item("clothing", 500, ItemType.CLOTHING),
                 new Item("gadget", 5000, ItemType.GADGET)])
        return cart
    }

    def Cart createAssortedCart() {
        Cart cart = new Cart(UUID.randomUUID(),
                [new Item("clothing", 500, ItemType.CLOTHING),
                 new Item("gadget", 5000, ItemType.GADGET),
                 new Item("gadget", 2500, ItemType.GADGET),
                 new Item("appliance", 10000, ItemType.APPLIANCE),
                 new Item("food", 150, ItemType.FOOD),
                 new Item("food", 20, ItemType.FOOD)])
        return cart
    }
}
