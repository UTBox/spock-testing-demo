package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {
    RefundService refundService

    RefundRepository refundRepository = Mock(RefundRepository)

    void setup() {
        refundService = new RefundService(refundRepository)
    }

    def "createAndSaveRefundRequest should create a refund request with full refund of the order amount when cancel reason is due to damaged item "() {
        given:
        Order order = new Order(totalCost: 100, dateOrdered: new Date());

        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(reason: CancelReason.DAMAGED, dateCancelled: new Date());

        BigDecimal expectedRefundAmount = BigDecimal.valueOf(100)

        when:
        refundService.createAndSaveRefundRequest(order, cancelOrderRequest);

        then:
        1 * refundRepository.saveRefundRequest(_ as RefundRequest) >> { RefundRequest passedRequest ->
            assert expectedRefundAmount == passedRequest.refundAmount
        }
    }

    def "createAndSaveRefundRequest should create a refund request with #refundDesc when cancel reason is due to wrong item and order is cancelled #dateDesc"() {
        given:
        UUID orderId = UUID.randomUUID()
        Calendar calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -dateDiffInDays)
        Date modifiedDate = calendar.getTime()

        and:
        Order order = Mock(Order)
        order.id >> orderId
        order.recipientName >> "Muzan Kibutsuji"
        order.totalCost >> 100
        order.dateOrdered >> modifiedDate

        and:
        CancelOrderRequest cancelOrderRequest = Mock(CancelOrderRequest)
        cancelOrderRequest.reason >> CancelReason.WRONG_ITEM
        cancelOrderRequest.dateCancelled >> new Date()

        when:
        refundService.createAndSaveRefundRequest(order, cancelOrderRequest)

        then:
        1 * refundRepository.saveRefundRequest(_ as RefundRequest) >> { RefundRequest passedRequest ->
            assert expectedRefund == passedRequest.refundAmount
            assert orderId == passedRequest.orderId
            assert "Muzan Kibutsuji" == passedRequest.recipientName
            assert RefundRequestStatus.TO_PROCESS == passedRequest.status
        }

        where:
        expectedRefund | dateDiffInDays | refundDesc                          | dateDesc
        100            | 3              | "a full refund of the order amount" | "within 3 days of order date"
        50             | 4              | "a refund of half the order amount" | "after 3 days of order date"
    }
}
