package synacy.gradprogram.spock.exercise

import com.synacy.gradprogram.spock.exercise.Cart
import com.synacy.gradprogram.spock.exercise.Item
import com.synacy.gradprogram.spock.exercise.ItemType
import com.synacy.gradprogram.spock.exercise.Order
import com.synacy.gradprogram.spock.exercise.OrderRepository
import com.synacy.gradprogram.spock.exercise.OrderStatus
import com.synacy.gradprogram.spock.exercise.OrderingService
import com.synacy.gradprogram.spock.exercise.UnableToCreateOrderException
import spock.lang.Specification

class OrderingServiceSpec extends Specification {
    OrderingService orderingService

    OrderRepository orderRepository = Mock(OrderRepository)

    void setup() {
        orderingService = new OrderingService(orderRepository)
    }

    def "cartContainsFoodItem should return true if cart has at least one food item"() {
        given:
        Cart cart = new Cart(UUID.randomUUID(), [
                new Item("Television", 500.0, ItemType.APPLIANCE),
                new Item("Eggs", 45.0, ItemType.FOOD)
        ])

        when:
        boolean result = orderingService.cartContainsFoodItem(cart)

        then:
        result == true
    }

    def "cartContainsFoodItem should return false if cart does not have any food item"() {
        given:
        Cart cart = new Cart(UUID.randomUUID(), [
                new Item("Television", 500.0, ItemType.APPLIANCE),
        ])

        when:
        boolean result = orderingService.cartContainsFoodItem(cart)

        then:
        result == false
    }

    def "calculateTotalCostOfCart should calculate the total cost of the cart"() {
        given:
        Cart cart = new Cart(UUID.randomUUID(), [
                new Item("Television", 500.0, ItemType.APPLIANCE),
        ])

        when:
        def totalPrice = orderingService.calculateTotalCostOfCart(cart)

        then:
        totalPrice == 500.0
    }


    def "createAnOrder should throw UnableToCreateOrderException when canContainFood is false but cart contains food items"() {
        given:
        double eggsPrice = 45.0

        Cart cart = new Cart(UUID.randomUUID(), [new Item("Eggs", eggsPrice, ItemType.FOOD)])

        String recipientName = "Vincent"
        String recipientAddress = "Cebu"
        boolean canContainFood = false

        when:
        orderingService.createAnOrder(cart, recipientName, recipientAddress, canContainFood)

        then:
        thrown(UnableToCreateOrderException)
    }

    def "isCartEligibleForDiscount should return true if the totalPrice and cart size are greater than the tresholds"() {
        given:
        double iphonePrice = 500.0
        double shirtPrice = 500.0
        double eggsPrice = 500.0
        double cheesePrice = 320.0
        double televisionPrice = 500.0
        double microwavePrice = 600.0

        Cart cart = new Cart(UUID.randomUUID(), [
                new Item("iPhone", iphonePrice, ItemType.GADGET),
                new Item("Shirt", shirtPrice, ItemType.CLOTHING),
                new Item("Eggs", eggsPrice, ItemType.FOOD),
                new Item("Cheese", cheesePrice, ItemType.FOOD),
                new Item("Television", televisionPrice ,ItemType.APPLIANCE),
                new Item("Microwave", microwavePrice, ItemType.APPLIANCE)
        ])

        when:
        boolean result = orderingService.isCartEligibleForDiscount(cart)

        then:
        result == true
    }

    def "isCartEligibleForDiscount should return false if the totalPrice and cart size are lesser than the threshold"() {
        given:

        double iphonePrice = 40.0

        items.add(new Item("iPhone", iphonePrice, ItemType.GADGET))

        Cart cart = new Cart(UUID.randomUUID(), items)

        when:
        boolean result = orderingService.isCartEligibleForDiscount(cart)

        then:
        result == false
    }

    def "isCartEligibleForDiscount should return false if the totalPrice is greater and cart size is lesser than the threshold"() {
        given:
        double iphonePrice = 60.0

        Cart cart = new Cart(UUID.randomUUID(), [new Item("iPhone", iphonePrice, ItemType.GADGET)])

        when:
        boolean result = orderingService.isCartEligibleForDiscount(cart)

        then:
        result == false
    }

    def "isCartEligibleForDiscount should return false if the totalPrice is lesser and cart size is greater than the threshold"() {
        given:
        double iphonePrice = 10.0
        double eggPrice = 1.0
        double televisionPrice = 1.0
        double laptopPrice = 1.0
        double posterPrice = 1.0
        double chargerPrice = 1.0

        Cart cart = new Cart(UUID.randomUUID(), [
                new Item("iPhone", iphonePrice, ItemType.GADGET),
                new Item("Eggs", eggPrice, ItemType.FOOD),
                new Item("Television", televisionPrice, ItemType.APPLIANCE),
                new Item("Laptop", laptopPrice, ItemType.GADGET),
                new Item("Poster", posterPrice, ItemType.APPLIANCE),
                new Item("Charger", chargerPrice, ItemType.GADGET),
        ])

        when:
        boolean result = orderingService.isCartEligibleForDiscount(cart)

        then:
        result == false
    }

    def "applyDiscountToCartItems should not apply discounted cost to item"() {
        given:
        double gadgetPrice = 500.0
        double clothingPrice = 400.0
        double foodPrice = 300.0
        double appliancePrice = 200.0

        Cart cart = new Cart(UUID.randomUUID(), [
                new Item("iPhone", gadgetPrice, ItemType.GADGET),
                new Item("Shirt", clothingPrice, ItemType.CLOTHING),
                new Item("Eggs", foodPrice, ItemType.FOOD),
                new Item("Television", appliancePrice ,ItemType.APPLIANCE)
        ])

        when:
        orderingService.applyDiscountToCartItems(cart)

        then:
        cart.getItems().get(0).cost == 500.0
        cart.getItems().get(1).cost == 400.0
        cart.getItems().get(2).cost == 300.0
        cart.getItems().get(3).cost == 200.0
    }


    def "createAnOrder should create an order when canContainFood is false and cart does not contain food item"() {
        given:
        double televisionPrice = 500.0

        Cart cart = new Cart(UUID.randomUUID(), [new Item("Television", televisionPrice, ItemType.GADGET)])

        String recipientName = "Vincent"
        String recipientAddress = "Cebu"
        boolean canContainFood = true

        when:
        orderingService.createAnOrder(cart, recipientName, recipientAddress, canContainFood)

        then:
        1 * orderRepository.saveOrder(_) >> { Order order ->
            assert order.getTotalCost() == 500.0
            assert order.getRecipientName() == "Vincent"
            assert order.getRecipientAddress() == "Cebu"
            assert order.getStatus() == OrderStatus.PENDING
        }

    }


    def "createAnOrder should create an order when canContainFood is true and cart contains food item"() {
        given:
        double eggsPrice = 45.0

        Cart cart = new Cart(UUID.randomUUID(), [new Item("Eggs", eggsPrice, ItemType.FOOD)])

        String recipientName = "Vincent"
        String recipientAddress = "Cebu"
        boolean canContainFood = true

        when:
        orderingService.createAnOrder(cart, recipientName, recipientAddress, canContainFood)

        then:
        1 * orderRepository.saveOrder(_) >> { Order order ->
            assert order.getTotalCost() == 45
            assert order.getRecipientName() == "Vincent"
            assert order.getRecipientAddress() == "Cebu"
            assert order.getStatus() == OrderStatus.PENDING
        }
    }
}
