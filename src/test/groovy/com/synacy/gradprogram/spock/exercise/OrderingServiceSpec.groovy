package com.synacy.gradprogram.spock.exercise


import spock.lang.Specification

class OrderingServiceSpec extends Specification {
    OrderingService orderingService
    OrderRepository orderRepository = Mock(OrderRepository)

    def setup(){
        orderingService = new OrderingService(orderRepository)
    }

    def "cancelOrder should cancel PENDING orders and create refund request saving it to the database"(){
        given:
        UUID orderId = UUID.randomUUID()
        Order order = new Order(id: orderId, status: OrderStatus.PENDING, totalCost: 50.0)

        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(orderId: orderId, reason: CancelReason.DAMAGED, dateCancelled: new Date())

        when:
        orderingService.cancelOrder(cancelOrderRequest)

        then:
        1 * orderCancellationService.saveOrder(pendingOrder)
        1 * orderCancellationService.createRefundRequest(pendingOrder, pendingCancelRequest.reason, pendingCancelRequest.dateCancelled)

    }

    def "cancelOrder should cancel FOR_DELIVERY orders and create refund request saving it to the database"(){
        given:
        UUID orderId = UUID.randomUUID()
        Order order = new Order(id: orderId, status: OrderStatus.FOR_DELIVERY, totalCost: 100.0)
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(orderId: orderId, reason: CancelReason.DAMAGED, dateCancelled: new Date())

        when:
        orderingService.cancelOrder(cancelOrderRequest)

        then:
        1 * orderingService.saveOrder(cancelOrderRequest)
        1 * orderingService.createRefundRequest(cancelOrderRequest, cancelOrderRequest.reason, cancelOrderRequest.dateCancelled)


    }
    def "cancelOrder should throw UnableToCancelException when order request is not PENDING and FOR_DELIVERY"() {
        given:
        UUID orderId = UUID.randomUUID()
        Order order = new Order(id: orderId, status: OrderStatus.DELIVERED, totalCost: 150.0)
        CancelOrderRequest cancelRequest = new CancelOrderRequest(orderId: orderId, reason: CancelReason.DAMAGED, dateCancelled: new Date())

        when:
        orderingService.cancelOrder(cancelRequest)

        then:
        UnableToCancelException exception = thrown(UnableToCancelException)
        exception.message == "Cannot Cancel Order"
    }
}
