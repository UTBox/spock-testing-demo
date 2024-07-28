package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {

    OrderingService orderingService

    OrderRepository orderRepository = Mock(OrderRepository)
    RefundService refundService = Mock(RefundService)
    DateUtils dateUtils = Mock(DateUtils)

    void setup() {
        orderingService = new OrderingService(dateUtils ,refundService, orderRepository)
    }

    def "cancelOrder should throw UnableToCancelException if #orderStatus is not PENDING or FOR_DELIVERY"() {
        given:
        Order order = Mock(Order)
        CancelOrderRequest cancelOrderRequest = Mock(CancelOrderRequest)

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

    def "cancelOrder should set order status to CANCELLED if OrderStatus from retrieved #order is PENDING or FOR_DELIVERY and set date cancelled to current date for #cancelOrderRequest"() {
        given:
        Order order = new Order()
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest()
        Date expectedDateCancelled = new Date()

        UUID uuid = order.getId()
        cancelOrderRequest.setOrderId(uuid)
        cancelOrderRequest.setReason(CancelReason.DAMAGED)
        OrderStatus expectedOrderStatus = OrderStatus.CANCELLED
        OrderStatus actualOrderStatus = OrderStatus.PENDING
        order.setStatus(actualOrderStatus)

        orderRepository.fetchOrderById(uuid) >> Optional.of(order)
        dateUtils.getCurrentDate() >> expectedDateCancelled

        when:
        orderingService.cancelOrder(cancelOrderRequest)

        then:
        expectedOrderStatus == order.getStatus()
        expectedDateCancelled == cancelOrderRequest.getDateCancelled()
    }

    def "cancelOrder should call saveOrder and createAndSaveRefundRequest if #validOrderStatus (PENDING or FOR_DELIVERY)"() {
        given:
        Order order = Mock(Order)
        CancelOrderRequest cancelOrderRequest = Mock(CancelOrderRequest)

        OrderStatus validOrderStatus = OrderStatus.PENDING
        CancelReason givenReason = CancelReason.DAMAGED
        Date currentDate = new Date()
        UUID uuid = UUID.randomUUID()

        cancelOrderRequest.getOrderId() >> uuid
        orderRepository.fetchOrderById(uuid) >> Optional.of(order)
        order.getStatus() >> validOrderStatus
        dateUtils.getCurrentDate() >> currentDate
        cancelOrderRequest.getReason() >> givenReason

        when:
        orderingService.cancelOrder(cancelOrderRequest)

        then:
        1 * orderRepository.saveOrder(order)
        1 * refundService.createAndSaveRefundRequest(cancelOrderRequest)
    }
}
