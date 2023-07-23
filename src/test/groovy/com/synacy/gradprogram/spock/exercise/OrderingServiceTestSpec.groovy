package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceTestSpec extends Specification {
    OrderingService orderingService
    RefundRepository refundRepository = Mock(RefundRepository)
    OrderRepository orderRepository = Mock(OrderRepository)

    void setup() {
        orderingService = new OrderingService(orderRepository, refundRepository)
        orderRepository.fetchOrderById(_) >> Optional.empty()
    }

    def "CancelOrder Should cancel PENDING and FOR_DELIVERY orders and create a refund request"() {
        given:
        CancelOrderRequest request = new CancelOrderRequest()
        UUID orderId = UUID.randomUUID()
        request.setOrderId(orderId)
        request.setReason(CancelReason.DAMAGED)
        request.setDateCancelled(new Date())

        when:
        orderingService.cancelOrder(request, OrderStatus.PENDING)

        then:
        1 * refundRepository.saveRefundRequest(_)
        1 * orderRepository.fetchOrderById(orderId) >> Optional.of(new Order())
    }

    def "Should throw UnableToCancelException for DELIVERED order"() {
        given:
        CancelOrderRequest request = new CancelOrderRequest()
        request.setOrderId(UUID.randomUUID())
        request.setReason(CancelReason.DAMAGED)
        request.setDateCancelled(new Date())

        when:
        orderingService.cancelOrder(request, OrderStatus.DELIVERED)

        then:
        def exception = thrown(UnableToCancelException)
        exception.message == "Unable to cancel order: Invalid order status for cancellation."
    }


}
