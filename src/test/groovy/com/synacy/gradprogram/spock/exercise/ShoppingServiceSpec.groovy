package com.synacy.gradprogram.spock.exercise


import spock.lang.Specification

class ShoppingServiceSpec extends Specification {

    ShoppingService shoppingService

    OrderingService orderingService = Mock(OrderingService)
    DeliveryService deliveryService = Mock(DeliveryService)
    OrderRepository orderRepository = Mock(OrderRepository)
    DeliveryRequestRepository deliveryRequestRepository = Mock(DeliveryRequestRepository)

    void setup() {
        shoppingService = new ShoppingService(orderingService, deliveryService, orderRepository, deliveryRequestRepository)
    }

    def "BuyNonSpoilingItemsInCart should call applyDiscountToCartItems, create an order, and create a delivery based on #expectedOrder"() {
        given:
        Order expectedOrder = new Order()
        Cart cart = new Cart(UUID.randomUUID(), [])
        User user = new User()
        user.setFirstName("John")
        user.setLastName("Doe")
        user.setAddress("Liberty City")

        when:
        shoppingService.buyNonSpoilingItemsInCart(cart, user)

        then:
        1 * orderingService.applyDiscountToCartItems(cart)
        1 * orderingService.createAnOrder(cart, "John Doe", "Liberty City", false) >> expectedOrder
        1 * deliveryService.createDelivery(expectedOrder)
    }

    def "GetOrderSummary should return an OrderSummary object"() {
        given:
        Order expectedOrder = new Order()
        UUID expectedOrderId = expectedOrder.getId()
        expectedOrder.setTotalCost(5000)
        expectedOrder.setRecipientName("John Doe")
        expectedOrder.setRecipientAddress("Liberty City")
        expectedOrder.setStatus(OrderStatus.PENDING)

        DeliveryRequest deliveryRequest = new DeliveryRequest()
        Date expectedDeliveryRequestDate = new Date()
        deliveryRequest.setCourier(Courier.JRS)
        deliveryRequest.setDeliveryDate(expectedDeliveryRequestDate)

        orderRepository.fetchOrderById(expectedOrderId) >> expectedOrder
        deliveryRequestRepository.fetchDeliveryRequestByOrderId(expectedOrderId) >> deliveryRequest

        when:
        OrderSummary actualOrderSummary = shoppingService.getOrderSummary(expectedOrderId)

        then:
        expectedOrder.totalCost == actualOrderSummary.getTotalCost()
        expectedOrder.getStatus() == actualOrderSummary.getStatus()
        expectedDeliveryRequestDate == actualOrderSummary.getDeliveryDate()
        actualOrderSummary.getCourier() != null
    }
}
