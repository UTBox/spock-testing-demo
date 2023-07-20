package com.mechanitis.demo.spock

import com.synacy.gradprogram.spock.exercise.Cart
import com.synacy.gradprogram.spock.exercise.Item
import com.synacy.gradprogram.spock.exercise.ItemType
import com.synacy.gradprogram.spock.exercise.Order
import com.synacy.gradprogram.spock.exercise.OrderStatus
import com.synacy.gradprogram.spock.exercise.OrderingService
import com.synacy.gradprogram.spock.exercise.UnableToCreateOrderException
import spock.lang.Specification

class OrderingServiceSpec extends Specification {

    OrderingService service
    Cart cart
    def items = [
            new Item ("Rice", 50.00, ItemType.FOOD),
            new Item ("Chicken", 20.00, ItemType.FOOD),
            new Item ("Nasa", 34.5, ItemType.CLOTHING),
            new Item ("Air-con", 34.5, ItemType.APPLIANCE),
            new Item ("Laptop", 34.5, ItemType.GADGET),
            new Item ("Nike", 500.54, ItemType.CLOTHING)
    ]
//    Item item1 = new Item ("Rice", 50.00, ItemType.FOOD)
//    Item item2 = new Item ("Chicken", 20.00, ItemType.FOOD)
//    Item item3 = new Item ("Nasa", 34.5, ItemType.CLOTHING)
//    Item item4 = new Item ("Air-con", 34.5, ItemType.APPLIANCE)
//    Item item5 = new Item ("Laptop", 34.5, ItemType.GADGET)


    def setup() {
        service = new OrderingService()
    }

    def "should return true when cart contains food item type"() {
        when:
        boolean containsFoodType = service.cartContainsFoodItem(cart = new Cart(UUID.randomUUID(), items))

        then:
        containsFoodType
    }

    def "should demonstrate the total cost of cart"() {
        given:
        Item item = new Item ("Rice", 50.00, ItemType.FOOD)

        when:
        double totalPrice = service.calculateTotalCostOfCart(cart = new Cart(UUID.randomUUID(), items))
        totalPrice =+ item.getCost()

        then:
        totalPrice
    }

    def "should return true when cart is eligible for discount"() {
        given:
        cart = new Cart(UUID.randomUUID(), items)

        when:
        boolean result = service.isCartEligibleForDiscount(cart)

        then:
        result
    }

    def "should apply discount to cart items when the cart is eligible for discount"() {
        given:
        cart = new Cart(UUID.randomUUID(), items)
        List<Double> initialCosts = cart.getItems().collect() { it.getCost() }

        when:
        service.applyDiscountToCartItems(cart)
        List<Double>  updatedCosts = cart.getItems().collect() { it.getCost() }

        then:
        updatedCosts.size() == initialCosts.size()
        boolean isEligible = updatedCosts.every() { it < (initialCosts[5]) }
        isEligible

    }

    def "should not apply discount to cart items when the cart is not eligible for discount"() {
        given:
        cart = new Cart(UUID.randomUUID(), items)
        List<Double> initialCosts = cart.getItems().collect() { it.getCost() }

        when:
        service.applyDiscountToCartItems(cart)
        List<Double>  updatedCosts = cart.getItems().collect() { it.getCost() }

        then:
        updatedCosts.size() == initialCosts.size()
    }


    def "should create an order with correct details when cart is valid and contain food"() {
        given:
        cart = new Cart(UUID.randomUUID(), items)
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

    def "should throw and exception when cart contains food items and canContainFood is false"() {
        given:
        cart = new Cart(UUID.randomUUID(), items)
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
