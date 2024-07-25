package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class ShoppingServiceSpec extends Specification {
    ShoppingService shoppingService

    OrderingService orderingService = Mock()
    DeliveryService deliveryService = Mock()
    OrderRepository orderRepository = Mock()
    DeliveryRequestRepository deliveryRequestRepository = Mock()

    def setup(){
        shoppingService = new ShoppingService(orderingService,
                deliveryService,
                orderRepository,
                deliveryRequestRepository
        )
    }

    def "buyNonSpoilingItemsInCart should #discountDesc when the cart is #eligibilityDesc"(){
        given:
        User user = new User(firstName: "John", lastName: "Wick", address: "Hollywoo")

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * orderingService.applyDiscountToCartItems(cart) >> { Cart passedCart ->
            expectedCost == passedCart.getItems()[0].getCost()
            expectedCost == passedCart.getItems()[1].getCost()
        }

        where:
        cart                                  | expectedCost   | discountDesc         | eligibilityDesc
        createGadgetCartWith2Items(1)         | 1d             | "not apply discount" | "not eligible"
        createGadgetCartWith2Items(26)        | 26d            | "not apply discount" | "not eligible"
        createGadgetCartWith6Items(1)         | 1d             | "not apply discount" | "not eligible"
        createGadgetCartWith6Items(50)        | 45d            | "apply discount"     | "eligible"
    }

    def "buyNonSpoilingItemsInCart should create a non-Food order based on the cart with non-food items and recipient's name and address"(){
        given:
        User user = new User(firstName: "John", lastName: "Wick", address: "Hollywoo")

        Cart cart = createGadgetCartWith2Items(2)

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * orderingService.createAnOrder(cart, user.getFullName(), user.getAddress(), false)
    }

    def "buyNonSpoilingItemsInCart should not create a delivery when cart contains a food item"(){
        given:
        User user = Mock()
        Order order = Mock()

        Cart cart = createFoodCartWith2Items(2)

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * orderingService.createAnOrder(cart, user.getFullName(), user.getAddress(), false)
        0 * deliveryService.createDelivery(order)
    }


    def "buyNonSpoilingItemsInCart should create a delivery based on the non-food order"(){
        given:
        User user = new User(firstName: "John", lastName: "Wick", address: "Hollywoo")

        Cart cart = createGadgetCartWith2Items(2)

        Order order = new Order()

        orderingService.createAnOrder(cart, user.getFullName(), user.getAddress(), false) >> order

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * deliveryService.createDelivery(order)
    }

    def "getOrderSummary should return an OrderSummary from the orderId, order, and delivery request"(){
        given:
        double totalCost = 0
        OrderStatus status = OrderStatus.PENDING

        Order order = new Order(totalCost: totalCost, status: status)
        UUID uuid = order.id

        orderRepository.fetchOrderById(uuid) >> order

        and:
        Date deliveryDate = new Date()
        Courier courier = Courier.JRS
        DeliveryRequest deliveryRequest = new DeliveryRequest(deliveryDate: deliveryDate, courier: courier)

        deliveryRequestRepository.fetchDeliveryRequestByOrderId(uuid) >> deliveryRequest

        when:
        OrderSummary orderSummary = shoppingService.getOrderSummary(uuid)

        then:
        totalCost == orderSummary.getTotalCost()
        status == orderSummary.getStatus()
        deliveryDate == orderSummary.getDeliveryDate()
        courier == orderSummary.getCourier()
    }


    def Cart createGadgetCartWith2Items(double cost){
        UUID uuid = UUID.randomUUID()
        Cart cart = new Cart(uuid, new ArrayList<Item>())

        cart.addItem(new Item("Mouse", cost, ItemType.GADGET))
        cart.addItem(new Item("Mouse", cost, ItemType.GADGET))

        return cart
    }

    def Cart createGadgetCartWith6Items(double cost){
        UUID uuid = UUID.randomUUID()
        Cart cart = new Cart(uuid, new ArrayList<Item>())

        cart.addItem(new Item("Mouse", cost, ItemType.GADGET))
        cart.addItem(new Item("Mouse", cost, ItemType.GADGET))
        cart.addItem(new Item("Mouse", cost, ItemType.GADGET))
        cart.addItem(new Item("Mouse", cost, ItemType.GADGET))
        cart.addItem(new Item("Mouse", cost, ItemType.GADGET))
        cart.addItem(new Item("Mouse", cost, ItemType.GADGET))

        return cart
    }

    def Cart createFoodCartWith2Items(double cost){
        UUID uuid = UUID.randomUUID()
        Cart cart = new Cart(uuid, new ArrayList<Item>())

        cart.addItem(new Item("Cabbage", cost, ItemType.FOOD))
        cart.addItem(new Item("Cabbage", cost, ItemType.FOOD))

        return cart
    }
}
