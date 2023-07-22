package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceSpec extends Specification {
    OrderingService orderingService
    OrderRepository orderRepository
    RefundService refundService

    void setup() {
        orderRepository = Mock(OrderRepository)
        refundService = Mock(RefundService)
        orderingService = new OrderingService(orderRepository, refundService)
    }

    def "cancelOrder should save OrderStatus to CANCELLED for orders with status PENDING and FOR_DELIVERY in repository"() {
        given:
        CancelOrderRequest request = Mock(CancelOrderRequest)

        when:
        orderingService.cancelOrder(request, order)

        then:
        1 * orderRepository.saveOrder(order) >> { Order testOrder ->
            assert expectedStatus == testOrder.getStatus()
        }

        where:
        order                                        |          expectedStatus
        new Order(status: OrderStatus.PENDING)       |   OrderStatus.CANCELLED
        new Order(status: OrderStatus.FOR_DELIVERY)  |   OrderStatus.CANCELLED
    }

    def "cancelOrder should throw UnableToCancelException if OrderStatus is not PENDING or FOR_DELIVERY"() {
        given:
        CancelOrderRequest request = Mock(CancelOrderRequest)

        when:
        orderingService.cancelOrder(request, order)

        then:
        thrown(expectedStatus)

        where:
        order                                     |            expectedStatus
        new Order(status: OrderStatus.CANCELLED)  |   UnableToCancelException
        new Order(status: OrderStatus.DELIVERED)  |   UnableToCancelException
    }

    def "cancelOrder should create a refund request and save it to database"() {
        given:
        Order order = new Order(totalCost: 100, dateOrdered: new Date(), recipientName: "John Doe", status: OrderStatus.PENDING)
        CancelOrderRequest request = new CancelOrderRequest(dateCancelled: new Date())
        BigDecimal refund = 100

        RefundRepository refundRepository = Mock(RefundRepository)
        RefundService refundService = new RefundService(refundRepository)

        orderingService = new OrderingService(orderRepository, refundService)

        when:
        orderingService.cancelOrder(request, order)

        then:
        1 * refundRepository.saveRefundRequest(_) >> { RefundRequest refundRequest ->
            assert order.recipientName == refundRequest.getRecipientName()
            assert order.getId() == refundRequest.getOrderId()
            assert refund == refundRequest.getRefundAmount()
            assert RefundRequestStatus.TO_PROCESS == refundRequest.getStatus()
        }
    }
}
