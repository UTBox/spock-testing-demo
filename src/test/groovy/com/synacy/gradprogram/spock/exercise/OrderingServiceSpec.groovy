package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {

    OrderingService service
    OrderRepository orderRepository = new OrderRepository()

    def setup() {
        service = new OrderingService(orderRepository)
    }

    def "cartContainsFoodItem should return true when cart contains food item type"() {
        given:
        def items = [
                new Item ("Rice", 50.00, ItemType.FOOD),
                new Item ("Chicken", 20.00, ItemType.FOOD),
                new Item ("Nasa", 30.00, ItemType.CLOTHING),
                new Item ("Air-con", 30.00, ItemType.APPLIANCE),
                new Item ("Laptop", 30.00, ItemType.GADGET),
                new Item ("Nike", 500.00, ItemType.CLOTHING)
        ]

        when:
        boolean containsFoodType = service.cartContainsFoodItem(new Cart(UUID.randomUUID(), items))

        then:
        containsFoodType
    }

    def "calculateTotalCostOfCart should calculate the total cost of items in cart"() {
        given:
        def items = [
                new Item ("Rice", 50.00, ItemType.FOOD),
                new Item ("Chicken", 20.00, ItemType.FOOD),
                new Item ("Nasa", 30.00, ItemType.CLOTHING),
                new Item ("Air-con", 30.00, ItemType.APPLIANCE),
                new Item ("Laptop", 30.00, ItemType.GADGET),
                new Item ("Nike", 500.00, ItemType.CLOTHING)
        ]
        Cart cart = new Cart(UUID.randomUUID(), items)
        double expectedTotalPrice = 660d

        when:
        double totalPrice = service.calculateTotalCostOfCart(cart)

        then:
        expectedTotalPrice == totalPrice
    }

    def "isCartEligibleForDiscount should return true when cart is eligible for discount"() {
        given:
        def items = [
                new Item ("Rice", 50.00, ItemType.FOOD),
                new Item ("Chicken", 20.00, ItemType.FOOD),
                new Item ("Nasa", 30.00, ItemType.CLOTHING),
                new Item ("Air-con", 30.00, ItemType.APPLIANCE),
                new Item ("Laptop", 30.00, ItemType.GADGET),
                new Item ("Nike", 500.00, ItemType.CLOTHING)
        ]
        Cart cart = new Cart(UUID.randomUUID(), items)

        when:
        boolean isEligible = service.isCartEligibleForDiscount(cart)

        then:
        isEligible

    }

    def "applyDiscountToCartItems should apply discount to cart items when the cart is eligible for discount"() {
        given:
        def items = [
                new Item ("Rice", 50.00, ItemType.FOOD),
                new Item ("Chicken", 20.00, ItemType.FOOD),
                new Item ("Nasa", 30.00, ItemType.CLOTHING),
                new Item ("Air-con", 30.00, ItemType.APPLIANCE),
                new Item ("Laptop", 30.00, ItemType.GADGET),
                new Item ("Nike", 500.00, ItemType.CLOTHING)
        ]
        Cart cart = new Cart(UUID.randomUUID(), items)

        when:
        service.applyDiscountToCartItems(cart)

        then:
        5d == items[0].getCost()
        2d == items[1].getCost()
        3d == items[2].getCost()
        3d == items[3].getCost()
        3d == items[4].getCost()
        50d == items[5].getCost()
    }

    def "applyDiscountToCartItems should not apply discount to cart items when the cart is not eligible for discount"() {
        given:
        def items = [
                new Item ("Chicken", 20.00, ItemType.FOOD),
                new Item ("Nasa", 30.00, ItemType.CLOTHING),
                new Item ("Air-con", 30.00, ItemType.APPLIANCE),
                new Item ("Laptop", 30.00, ItemType.GADGET),
        ]
        Cart cart = new Cart(UUID.randomUUID(), items)

        when:
        service.applyDiscountToCartItems(cart)

        then:
        20d == items.get(0).getCost()
        30d == items.get(1).getCost()
        30d == items.get(2).getCost()
        30d == items.get(3).getCost()
    }


    def "createAnOrder should create an order with correct details when cart is valid and contain food"() {
        given:
        def items = [
                new Item ("Rice", 50.00, ItemType.FOOD),
                new Item ("Chicken", 20.00, ItemType.FOOD),
                new Item ("Nasa", 30.00, ItemType.CLOTHING),
                new Item ("Air-con", 30.00, ItemType.APPLIANCE),
                new Item ("Laptop", 30.00, ItemType.GADGET),
                new Item ("Nike", 500.00, ItemType.CLOTHING)
        ]
        Cart cart = new Cart(UUID.randomUUID(), items)
        String recipientName = "Romeo"
        String recipientAddress = "Cebu City"
        boolean canContainFood = true

        when:
        Order order = service.createAnOrder(cart, recipientName, recipientAddress, canContainFood)

        then:
        order.totalCost == service.calculateTotalCostOfCart(cart)
        order.recipientName == recipientName
        order.recipientAddress == recipientAddress
        order.status == OrderStatus.PENDING
    }

    def "createAnOrder should throw and exception when cart contains food items and canContainFood is false"() {
        given:
        def items = [
                new Item ("Rice", 50.00, ItemType.FOOD),
                new Item ("Chicken", 20.00, ItemType.FOOD),
                new Item ("Nasa", 30.00, ItemType.CLOTHING),
                new Item ("Air-con", 30.00, ItemType.APPLIANCE),
                new Item ("Laptop", 30.00, ItemType.GADGET),
                new Item ("Nike", 500.00, ItemType.CLOTHING)
        ]
        Cart cart = new Cart(UUID.randomUUID(), items)
        String recipientName = "Romeo"
        String recipientAddress = "Cebu City"
        boolean canContainFood = false

        when:
        service.createAnOrder(cart, recipientName, recipientAddress, canContainFood)

        then:
        UnableToCreateOrderException exception = thrown(UnableToCreateOrderException)
        exception.message == "Cart should not contain FOOD items"
    }
}
