package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {

    RefundService refundService

    RefundRepository refundRepository = Mock(RefundRepository)
    OrderRepository orderRepository = Mock(OrderRepository)

    void setup() {
        refundService = new RefundService(refundRepository, orderRepository)
    }

    def "createAndSaveRefundRequest should create a RefundRequest with #refundRequestStatus of TO_PROCESS and save it"() {
        given:
        UUID expectedOrderId = UUID.randomUUID()
        String recipientName = "John Doe"
        String refundAmountString = "2500"
        BigDecimal expectedRefundAmount = new BigDecimal(refundAmountString)

        CancelReason cancelReason = CancelReason.DAMAGED
        RefundRequestStatus expectedStatus = RefundRequestStatus.TO_PROCESS

        Order order = Mock(Order)
        CancelOrderRequest cancelOrderRequest = Mock(CancelOrderRequest)
        RefundRequest refundRequest = new RefundRequest()

        cancelOrderRequest.getOrderId() >> expectedOrderId
        orderRepository.fetchOrderById(expectedOrderId) >> Optional.of(order)
        order.getRecipientName() >> recipientName

        refundRequest.setOrderId(expectedOrderId)
        refundRequest.setRecipientName(recipientName)
        refundRequest.setStatus(expectedStatus)
        refundRequest.setRefundAmount(expectedRefundAmount)

        when:
        refundService.createAndSaveRefundRequest(cancelOrderRequest)

        then:
        1 * refundRepository.saveRefundRequest(_ as RefundRequest) >> { RefundRequest request ->
            assert expectedOrderId == request.orderId
            assert recipientName == request.recipientName
            assert expectedRefundAmount == request.refundAmount
            assert expectedStatus == request.status
        }
    }
}
