package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {

    OrderingService orderingService

    OrderRepository orderRepository = Mock()
    RefundService refundService = Mock()

    def setup(){
        orderingService = new OrderingService(orderRepository, refundService)
    }

    def "cancelOrder should throw an UnableToCancelException when no order is associated with the cancel order request"(){
        given:
        UUID uuid = UUID.randomUUID()
        Order order = Mock()

        CancelOrderRequest request = Mock()
        request.getOrderId() >> uuid

        Optional<Order> optionalOrder = Optional.of(order)
        orderRepository.fetchOrderById(uuid) >> optionalOrder

        optionalOrder.isEmpty() >> true

        when:
        orderingService.cancelOrder(request)

        then:
        thrown(UnableToCancelException)
    }
    def "cancelOrder should throw an UnableToCancelException for orders with #status status"(){
        given:
        UUID uuid = UUID.randomUUID()
        Order order = Mock()
        order.getStatus() >> status
        order.getId() >> uuid

        and:
        CancelOrderRequest request = Mock()
        request.getOrderId() >> uuid
        orderRepository.fetchOrderById(uuid) >> Optional.of(order)

        when:
        orderingService.cancelOrder(request)

        then:
        thrown(UnableToCancelException)

        where:
        status << [OrderStatus.CANCELLED, OrderStatus.DELIVERED]
    }

    def "cancelOrder should cancel #status orders"(){
        given:
        Order order = new Order()
        order.setStatus(status)
        UUID uuid = order.getId()

        and:
        CancelOrderRequest request = Mock()
        request.getOrderId() >> uuid
        orderRepository.fetchOrderById(uuid) >> Optional.of(order)

        when:
        orderingService.cancelOrder(request)

        then:
        orderRepository.saveOrder(order) >> {Order savedOrder ->
            OrderStatus.CANCELLED == savedOrder.getStatus()
        }

        where:
        status << [OrderStatus.PENDING, OrderStatus.FOR_DELIVERY]
    }

    def "cancelOrder should create and save a refund request on cancelled #status orders"(){
        given:
        String recipientName = "John Wick"

        Order order = new Order()
        order.setStatus(status)
        order.setRecipientName(recipientName)
        UUID uuid = order.getId()

        and:
        CancelOrderRequest request = Mock()
        request.getOrderId() >> uuid
        orderRepository.fetchOrderById(uuid) >> Optional.of(order)

        when:
        orderingService.cancelOrder(request)

        then:
        1 * refundService.createAndSaveRefundRequest(order, request)

        where:
        status << [OrderStatus.PENDING, OrderStatus.FOR_DELIVERY]
    }


}
