package synacy.gradprogram.spock.exercise

import com.synacy.gradprogram.spock.exercise.Cart
import com.synacy.gradprogram.spock.exercise.Item
import com.synacy.gradprogram.spock.exercise.ItemType
import com.synacy.gradprogram.spock.exercise.Order
import com.synacy.gradprogram.spock.exercise.OrderStatus
import com.synacy.gradprogram.spock.exercise.OrderingService
import com.synacy.gradprogram.spock.exercise.UnableToCreateOrderException;
import spock.lang.Specification;

class OrderingServiceSpec extends Specification {

    List<Item> items
    OrderingService orderingService

    def setup() {
        items = new ArrayList<>()
        orderingService = new OrderingService()
    }

    def "cartContainsFoodItem should return true if the cart contains a food item"() {
        given:
        items.add(new Item("Bacon", 10.0, ItemType.FOOD))

        Cart cart = new Cart(UUID.randomUUID(), items)

        when:
        boolean result = orderingService.cartContainsFoodItem(cart)

        then:
        result
    }

    def "cartContainsFoodItem should return false if the cart does not contain a food item"() {
        given:
        Cart cart = new Cart(UUID.randomUUID(), items)

        items.add(new Item("Refrigerator",500.0, ItemType.APPLIANCE))

        when:
        boolean result = orderingService.cartContainsFoodItem(cart)

        then:
        !result
    }

    def "calculateTotalCostOfCart should return the total cost the cart"() {
        given:

        double gadgetPrice = 500.0
        double clothingPrice = 500.0
        double foodPrice = 500.0
        double appliancePrice = 500.0

        items.add(new Item("iPhone", gadgetPrice, ItemType.GADGET))
        items.add(new Item("Shirt", clothingPrice, ItemType.CLOTHING))
        items.add(new Item("Eggs", foodPrice, ItemType.FOOD))
        items.add(new Item("Television", appliancePrice ,ItemType.APPLIANCE))

        Cart cart = new Cart(UUID.randomUUID(), items)

        when:
        double result = orderingService.calculateTotalCostOfCart(cart)

        then:
        result == gadgetPrice + clothingPrice + foodPrice + appliancePrice
    }

    def "isCartEligibleForDiscount should return true if the cart is eligible for discount"() {
        given:

        double iphonePrice = 500.0
        double shirtPrice = 500.0
        double eggsPrice = 500.0
        double cheesePrice = 320.0
        double televisionPrice = 500.0
        double microwavePrice = 600.0

        items.add(new Item("iPhone", iphonePrice, ItemType.GADGET))
        items.add(new Item("Shirt", shirtPrice, ItemType.CLOTHING))
        items.add(new Item("Eggs", eggsPrice, ItemType.FOOD))
        items.add(new Item("Cheese", cheesePrice, ItemType.FOOD))
        items.add(new Item("Television", televisionPrice ,ItemType.APPLIANCE))
        items.add(new Item("Microwave", microwavePrice, ItemType.APPLIANCE))

        Cart cart = new Cart(UUID.randomUUID(), items)

        when:
        boolean result = orderingService.isCartEligibleForDiscount(cart)

        then:
        result
    }

    def "isCartEligibleForDiscount should return false if the cart is not eligible for discount"() {
        given:

        double iphonePrice = 40.0

        items.add(new Item("iPhone", iphonePrice, ItemType.GADGET))

        Cart cart = new Cart(UUID.randomUUID(), items)

        when:
        boolean result = orderingService.isCartEligibleForDiscount(cart)

        then:
        !result
    }

    def "applyDiscountToCartItems should apply discounted cost to item with a discount rate of 10%"() {
        given:

        double iphonePrice = 500.0
        double shirtPrice = 500.0
        double eggsPrice = 500.0
        double cheesePrice = 320.0
        double televisionPrice = 500.0
        double microwavePrice = 600.0

        items.add(new Item("iPhone", iphonePrice, ItemType.GADGET))
        items.add(new Item("Shirt", shirtPrice, ItemType.CLOTHING))
        items.add(new Item("Eggs", eggsPrice, ItemType.FOOD))
        items.add(new Item("Cheese", cheesePrice, ItemType.FOOD))
        items.add(new Item("Television", televisionPrice ,ItemType.APPLIANCE))
        items.add(new Item("Microwave", microwavePrice, ItemType.APPLIANCE))

        Cart cart = new Cart(UUID.randomUUID(), items)

        Cart testCart = new Cart(UUID.randomUUID(), items)

        double discountRate = 0.10

        List<Item> cartItems = testCart.getItems()

        double firstItemCost = cartItems.get(0).cost
        cartItems.get(0).setCost(firstItemCost * (1 - discountRate))

        double secondItemCost = cartItems.get(1).cost
        cartItems.get(1).setCost(secondItemCost * (1 - discountRate))

        double thirdItemCost = cartItems.get(2).cost
        cartItems.get(2).setCost(thirdItemCost * (1 - discountRate))

        double fourthItemCost = cartItems.get(3).cost
        cartItems.get(3).setCost(fourthItemCost * (1 - discountRate))

        double fifthItemCost = cartItems.get(4).cost
        cartItems.get(4).setCost(fifthItemCost * (1 - discountRate))

        double sixthItemCost = cartItems.get(5).cost
        cartItems.get(5).setCost(sixthItemCost * (1 - discountRate))

        when:
        orderingService.applyDiscountToCartItems(cart)

        then:
        cart.getItems().get(0).cost == testCart.getItems().get(0).cost
        cart.getItems().get(1).cost == testCart.getItems().get(1).cost
        cart.getItems().get(2).cost == testCart.getItems().get(2).cost
        cart.getItems().get(3).cost == testCart.getItems().get(3).cost
        cart.getItems().get(4).cost == testCart.getItems().get(4).cost
    }

    def "applyDiscountToCartItems should not apply discounted cost to item"() {
        given:
        double gadgetPrice = 500.0
        double clothingPrice = 400.0
        double foodPrice = 300.0
        double appliancePrice = 200.0

        items.add(new Item("iPhone", gadgetPrice, ItemType.GADGET))
        items.add(new Item("Shirt", clothingPrice, ItemType.CLOTHING))
        items.add(new Item("Eggs", foodPrice, ItemType.FOOD))
        items.add(new Item("Television", appliancePrice ,ItemType.APPLIANCE))
        Cart cart = new Cart(UUID.randomUUID(), items)

        when:
        orderingService.applyDiscountToCartItems(cart)

        then:
        cart.getItems().get(0).cost == gadgetPrice
        cart.getItems().get(1).cost == clothingPrice
        cart.getItems().get(2).cost == foodPrice
        cart.getItems().get(3).cost == appliancePrice
    }

    def "createAnOrder should throw UnableToCreateOrderException when canContainFood is false but cart contains food items"() {
        given:
        double eggsPrice = 45.0

        items.add(new Item("Eggs", eggsPrice, ItemType.FOOD))

        Cart cart = new Cart(UUID.randomUUID(), items)

        String recipientName = "Vincent"
        String recipientAddress = "Cebu"
        boolean canContainFood = false

        when:
        orderingService.createAnOrder(cart, recipientName, recipientAddress, canContainFood)

        then:
        thrown(UnableToCreateOrderException)
    }

    def "createAnOrder should create an order when canContainFood is false and cart does not contain food item"() {
        given:
        double televisionPrice = 500.0

        items.add(new Item("Television", televisionPrice, ItemType.GADGET))

        Cart cart = new Cart(UUID.randomUUID(), items)

        String recipientName = "Vincent"
        String recipientAddress = "Cebu"
        boolean canContainFood = true
        OrderStatus orderStatus = OrderStatus.PENDING

        when:
        Order resultOrder = orderingService.createAnOrder(cart, recipientName, recipientAddress, canContainFood)

        then:
        resultOrder.getTotalCost() == televisionPrice
        resultOrder.getRecipientName() == recipientName
        resultOrder.getRecipientAddress() == recipientAddress
        resultOrder.getStatus() == orderStatus

    }


    def "createAnOrder should create an order when canContainFood is true and cart contains food item"() {
        given:
        double eggsPrice = 45.0

        items.add(new Item("Eggs", eggsPrice, ItemType.FOOD))

        Cart cart = new Cart(UUID.randomUUID(), items)

        String recipientName = "Vincent"
        String recipientAddress = "Cebu"
        boolean canContainFood = true

        when:
        Order resultOrder = orderingService.createAnOrder(cart, recipientName, recipientAddress, canContainFood)

        then:
        resultOrder.getTotalCost() == eggsPrice
        resultOrder.getRecipientName() == recipientName
        resultOrder.getRecipientAddress() == recipientAddress
        resultOrder.getStatus() == OrderStatus.PENDING
    }

}