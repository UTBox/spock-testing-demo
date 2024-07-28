package synacy.gradprogram.spock.exercise

import com.synacy.gradprogram.spock.exercise.CancelOrderRequest
import com.synacy.gradprogram.spock.exercise.Order
import com.synacy.gradprogram.spock.exercise.OrderRepository
import com.synacy.gradprogram.spock.exercise.OrderStatus
import com.synacy.gradprogram.spock.exercise.OrderingService
import com.synacy.gradprogram.spock.exercise.RefundService
import com.synacy.gradprogram.spock.exercise.UnableToCancelException
import spock.lang.Specification

class OrderingServiceSpec extends Specification {
    OrderingService orderingService

    OrderRepository orderRepository = Mock(OrderRepository)
    RefundService refundService = Mock(RefundService)

    void setup() {
        orderingService = new OrderingService(orderRepository, refundService)
    }

    def "cancelOrder should throw UnableToCancelException when OrderStatus is CANCELLED or DELIVERED"() {
        given:
        def cancelOrderRequest = Mock(CancelOrderRequest)
        cancelOrderRequest.getOrderId() >> orderUUID

        Order order = Mock(Order)
        order.getStatus() >> orderStatus

        this.orderRepository.fetchOrderById(orderUUID) >> Optional.ofNullable(order)

        when:
        orderingService.cancelOrder(cancelOrderRequest)

        then:
        thrown(UnableToCancelException)

        where:
        orderStatus | orderUUID
        OrderStatus.CANCELLED | UUID.randomUUID()
        OrderStatus.DELIVERED | UUID.randomUUID()
    }

    def "cancelOrder should cancel PENDING and FOR_DELIVERY orders and create a refund request"() {
        given:
        def cancelOrderRequest = Mock(CancelOrderRequest)
        cancelOrderRequest.getOrderId() >> orderUUID

        Order order = Mock(Order)
        order.getStatus() >> orderStatus

        this.orderRepository.fetchOrderById(orderUUID) >> Optional.ofNullable(order)

        when:
        orderingService.cancelOrder(cancelOrderRequest)

        then:
        1 * order.setStatus(OrderStatus.CANCELLED)
        1 * this.refundService.createAndSaveRefundRequest(order, cancelOrderRequest)

        where:
        orderStatus | orderUUID
        OrderStatus.PENDING | UUID.randomUUID()
        OrderStatus.FOR_DELIVERY | UUID.randomUUID()
    }
}
