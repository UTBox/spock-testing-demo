package com.synacy.gradprogram.spock.exercise


import spock.lang.Specification

class OrderingServiceSpec extends Specification {
    OrderingService orderingService
    OrderRepository orderRepository = Mock(OrderRepository)

    def setup(){
        orderingService = new OrderingService(orderRepository)
    }

    def "cancelOrder should change status of order from  PENDING to CANCELLED and create refund request saving it to the database"(){
        given:
        Order order = new Order(status: OrderStatus.PENDING, totalCost: 50.0)
        CancelOrderRequest cancelOrderRequest = Mock(CancelOrderRequest)

        when:
        orderingService.cancelOrder(cancelOrderRequest, order)

        then:
        1 * orderRepository.saveOrder(order)
    }

    def "cancelOrder should change statues of order from FOR_DELIVERY to CANCELLED and create refund request saving it to the database"(){
        given:
        Order order = new Order(status: OrderStatus.FOR_DELIVERY, totalCost: 100.0)
        CancelOrderRequest cancelOrderRequest = Mock(CancelOrderRequest)

        when:
        orderingService.cancelOrder(cancelOrderRequest, order)

        then:
        1 * orderRepository.saveOrder(order)

    }
    def "cancelOrder should throw UnableToCancelException when order status request is not PENDING or FOR_DELIVERY"() {
        given:
        Order order = new Order(status: OrderStatus.DELIVERED, totalCost: 150.0)
        CancelOrderRequest cancelOrderRequest = Mock(CancelOrderRequest)

        when:
        orderingService.cancelOrder(cancelOrderRequest, order)

        then:
        UnableToCancelException exception = thrown(UnableToCancelException)
        exception.message == "Cannot Cancel Order"
    }
}
