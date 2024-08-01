package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {
    OrderingService orderingService
    OrderRepository orderRepository = Mock(OrderRepository)

    void setup() {
        orderingService = new OrderingService(orderRepository)
    }

    def "cartContainsFoodItem should return #expectedResult when cart #description food item"() {
        given:
        Item item = Mock(Item)
        Cart cart = new Cart(UUID.randomUUID(), [item])
        item.type >> itemType

        expect:
        expectedResult == orderingService.cartContainsFoodItem(cart)

        where:
        expectedResult | itemType        | description
        true           | ItemType.FOOD   | "contains"
        false          | ItemType.GADGET | "does not contain"
    }

    def "calculateTotalCostOfCart should return the total cost of cart"() {
        given:
        Item item1 = Mock(Item)
        Item item2 = Mock(Item)
        Cart cart = new Cart(UUID.randomUUID(), [item1, item2])

        item1.cost >> 10
        item2.cost >> 20

        double expectedTotal = 30

        expect:
        expectedTotal == orderingService.calculateTotalCostOfCart(cart)
    }

    def "isCartEligibleForDiscount should return #expectedResult when total price #priceDesc and item count #countDesc"() {
        expect:
        expectedResult == orderingService.isCartEligibleForDiscount(cart)

        where:
        cart                                                    | expectedResult | priceDesc            | countDesc
        new Cart(UUID.randomUUID(),
                [new Item("food", 10, ItemType.FOOD),
                 new Item("gadget", 10, ItemType.GADGET),
                 new Item("clothing", 10, ItemType.CLOTHING),
                 new Item("appliance", 10, ItemType.APPLIANCE),
                 new Item("food2", 10, ItemType.FOOD),
                 new Item("gadget2", 10, ItemType.GADGET)])     | true           | "exceeds 50"         | "is greater than 5"
        new Cart(UUID.randomUUID(),
                [new Item("food", 10, ItemType.FOOD),
                 new Item("gadget", 10, ItemType.GADGET)])      | false          | "does not exceed 50" | "is less than 6"
        new Cart(UUID.randomUUID(),
                [new Item("food", 70, ItemType.FOOD)])          | false          | "exceeds 50"         | "is less than 6"
        new Cart(UUID.randomUUID(),
                [new Item("food", 1, ItemType.FOOD),
                 new Item("gadget", 1, ItemType.GADGET),
                 new Item("clothing", 1, ItemType.CLOTHING),
                 new Item("appliance", 1, ItemType.APPLIANCE),
                 new Item("food2", 1, ItemType.FOOD),
                 new Item("gadget2", 1, ItemType.GADGET)])      | false          | "does not exceed 50" | "is greater than 6"
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

        List<Double> expectedDiscountedCost = [9, 18, 27, 36, 45, 54]

        when:
        orderingService.applyDiscountToCartItems(cart)

        then:
        expectedDiscountedCost[0] == cart.items[0].cost
        expectedDiscountedCost[1] == cart.items[1].cost
        expectedDiscountedCost[2] == cart.items[2].cost
        expectedDiscountedCost[3] == cart.items[3].cost
        expectedDiscountedCost[4] == cart.items[4].cost
        expectedDiscountedCost[5] == cart.items[5].cost
    }

    def "applyDiscountToCartItems should not apply discount to each item in cart when cart is not eligible for discount"() {
        given:
        Cart cart = new Cart(UUID.randomUUID(),
                [new Item("food", 10, ItemType.FOOD),
                 new Item("gadget", 20, ItemType.GADGET)])

        List<Double> expectedUndiscountedCost = [10, 20]

        when:
        orderingService.applyDiscountToCartItems(cart)

        then:
        expectedUndiscountedCost[0] == cart.items[0].cost
        expectedUndiscountedCost[1] == cart.items[1].cost
    }

    def "createAnOrder should throw an UnableToCreateOrderException when cart contains food item and canContainFood is false"() {
        given:
        Cart cart = new Cart(UUID.randomUUID(),
                [new Item("food", 10, ItemType.FOOD),
                 new Item("gadget", 10, ItemType.GADGET)])

        when:
        orderingService.createAnOrder(cart, "Sean", "BGC", false)

        then:
        thrown(UnableToCreateOrderException)
    }

    def "createAnOrder should create an order when #description"() {
        given:
        String expectedRecipientName = "Sean"
        String expectedRecipientAddress = "BGC"
        OrderStatus expectedOrderStatus = OrderStatus.PENDING

        when:
        Order actualOrder = orderingService.createAnOrder(cart, "Sean", "BGC", canContainFood)

        then:
        1 * orderRepository.saveOrder(_ as Order)
        expectedRecipientName == actualOrder.recipientName
        expectedRecipientAddress == actualOrder.recipientAddress
        expectedOrderStatus == actualOrder.status

        where:
        cart                                                    | canContainFood | description
        new Cart(UUID.randomUUID(), [
                new Item("food", 10, ItemType.FOOD)])           | true           | "cart contains food item and can contain food item"
        new Cart(UUID.randomUUID(), [
                new Item("gadget", 10, ItemType.GADGET)])       | false          | "cart does not contain food item and can not contain food item"
        new Cart(UUID.randomUUID(), [
                new Item("appliance", 10, ItemType.APPLIANCE)]) | true           | "cart does not contain food item and can contain food item"
    }
}
