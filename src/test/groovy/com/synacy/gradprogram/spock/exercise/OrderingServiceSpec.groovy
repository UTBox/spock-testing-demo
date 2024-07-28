package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {

    OrderingService orderingService

    OrderRepository orderRepository = Mock(OrderRepository)
    CancelOrderRequest cancelOrderRequest = Mock(CancelOrderRequest)
    RefundService refundService = Mock(RefundService)

    void setup() {
        orderingService = new OrderingService(refundService, orderRepository)
    }

    def "cancelOrder should throw UnableToCancelException if #orderStatus is not PENDING or FOR_DELIVERY"() {
        given:
        Order order = Mock(Order)

        UUID uuid = UUID.randomUUID()
        OrderStatus orderStatus = OrderStatus.DELIVERED

        cancelOrderRequest.getOrderId() >> uuid
        orderRepository.fetchOrderById(uuid) >> Optional.of(order)
        order.getStatus() >> orderStatus

        when:
        orderingService.cancelOrder(cancelOrderRequest)

        then:
        thrown(UnableToCancelException)
    }

    def "cancelOrder should set #actualOrderStatus to CANCELLED if given #orderStatus from #cancelOrderRequest is PENDING or FOR_DELIVERY and call createAndSaveRefundRequest"() {
        given:
        Order order = new Order()

        UUID uuid = order.getId()
        OrderStatus orderStatus = OrderStatus.PENDING
        OrderStatus expectedOrderStatus = OrderStatus.CANCELLED
        order.setStatus(orderStatus)

        cancelOrderRequest.getOrderId() >> uuid
        orderRepository.fetchOrderById(uuid) >> Optional.of(order)
//        order.setStatus(expectedOrderStatus)

        when:
        orderingService.cancelOrder(cancelOrderRequest)

        then:
        expectedOrderStatus == order.getStatus()
        1 * refundService.createAndSaveRefundRequest(order)
    }
}
