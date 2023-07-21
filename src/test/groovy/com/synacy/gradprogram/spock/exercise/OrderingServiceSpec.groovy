package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification
import spock.lang.Unroll

class OrderingServiceSpec extends Specification {

    OrderingService orderingService
    OrderRepository orderRepository = Mock(OrderRepository)
    def setup(){
        orderingService = new OrderingService(orderRepository)
    }


    Item food = new Item("Burger", 15, ItemType.FOOD)
    Item food2 = new Item("Pizza", 5, ItemType.FOOD)
    Item gadget = new Item("Cellphone", 10, ItemType.GADGET)
    Item gadget2 = new Item("iPad", 5, ItemType.GADGET)
    Item appliance = new Item("TV", 20, ItemType.APPLIANCE)
    Item appliance2 = new Item("TV", 5, ItemType.APPLIANCE)
    Item clothing = new Item("Dress", 10, ItemType.CLOTHING)
    Item clothing2 = new Item("Dress", 5, ItemType.CLOTHING)



    def "cartContainsFoodItem should respond with true if cart contains item with type food"() {
        given:
        List<Item> testListItemWFood = [food, gadget, appliance, clothing]
        def testCart = new Cart(UUID.randomUUID(), testListItemWFood)

        when:
        boolean foodItem = orderingService.cartContainsFoodItem(testCart)

        then:
        true == foodItem
    }

    def "cartContainsFoodItem should respond with false if cart does not contains item with type food"() {
        given:
        List<Item> testListItemWoFood = [gadget, appliance, clothing]
        def testCart = new Cart(UUID.randomUUID(), testListItemWoFood)

        when:
        boolean foodItem = orderingService.cartContainsFoodItem(testCart)

        then:
        false == foodItem
    }

    def "calculateTotalCostOfCart should respond with the total cost of item inside the cart"() {
        given:
        List<Item> testListItem = [food, gadget, appliance, clothing]
        def testCart = new Cart(UUID.randomUUID(), testListItem)

        when:
        double testTotal = orderingService.calculateTotalCostOfCart(testCart)

        then:
        55 == testTotal
    }

    def "isCartEligibleForDiscount should respond with true if total cart price is greater than 50.0 and size is greater than 5"() {

        given:
        List<Item> testListItem = [food, gadget, appliance, clothing, food2, gadget2]
        def testCart = new Cart(UUID.randomUUID(), testListItem)

        when:
        boolean isEligible = orderingService.isCartEligibleForDiscount(testCart)

        then:
        true == isEligible
    }

    def "isCartEligibleForDiscount should respond with false if cart price is less than 50.0 and cart size is less than 5"() {

        given:
        List<Item> testListItem = [food, gadget, clothing]
        def testCart = new Cart(UUID.randomUUID(), testListItem)

        when:
        boolean isEligible = orderingService.isCartEligibleForDiscount(testCart)

        then:
        false == isEligible
    }

    def "isCartEligibleForDiscount should respond with false if cart price is greater than 50.0 but cart size is less than 5"() {

        given:
        List<Item> testListItem = [food, gadget, appliance, clothing]
        def testCart = new Cart(UUID.randomUUID(), testListItem)

        when:
        boolean isEligible = orderingService.isCartEligibleForDiscount(testCart)

        then:
        false == isEligible
    }

    def "isCartEligibleForDiscount should respond with false if cart price is less than 50.0 but cart size is greater than 5"() {

        given:
        List<Item> testListItem = [food2, gadget2, clothing2, gadget, clothing, appliance2]
        def testCart = new Cart(UUID.randomUUID(), testListItem)

        when:
        boolean isEligible = orderingService.isCartEligibleForDiscount(testCart)

        then:
        false == isEligible
    }

    @Unroll
    def "ApplyDiscountToCartItems should respond with discounted cost"() {
        given:
        List<Item> testListItem = [food, gadget, appliance, clothing, food2, gadget2]
        List<Double> discountedCost = [1.5,1,2,1,0.5,0.5]
        def testCart = new Cart(UUID.randomUUID(), testListItem)

        when:
        orderingService.applyDiscountToCartItems(testCart)

        then:
        0.upto(discountedCost.size() - 1) { index ->
            discountedCost[index] == testCart.items[index].cost
        }
    }

    def "CreateAnOrder should throw exception when canContainFood is true and cart has food items"() {
        given:
        List<Item> testListItem = [gadget, appliance, clothing, gadget2, food]
        def testCart = new Cart(UUID.randomUUID(), testListItem)
        def recipientName = "Harry"
        def recipientAddress = "Cebu City"
        def canContainFood = false

        when:
        orderingService.createAnOrder(testCart, recipientName, recipientAddress, canContainFood)

        then:
        UnableToCreateOrderException orderException = thrown(UnableToCreateOrderException)
        orderException.message == "Cart should not contain FOOD items"
    }

    def "CreateAnOrder should respond with object order and set order status to Pending"() {
        given:
        List<Item> testListItem = [gadget, appliance, clothing, gadget2]
        def testCart = new Cart(UUID.randomUUID(), testListItem)
        def recipientName = "Harry"
        def recipientAddress = "Cebu City"
        def canContainFood = true
        double totalCost = 30

        when:
        Order testOrder = orderingService.createAnOrder(testCart, recipientName, recipientAddress, canContainFood)

        then:
        1 * orderRepository.saveOrder(_)
//                >> {
//            assert totalCost == testOrder.getTotalCost()
//            assert recipientName == testOrder.getRecipientName()
//            assert recipientAddress == testOrder.getRecipientAddress()
//            assert OrderStatus.PENDING == testOrder.getStatus()
//        }
    }
}
