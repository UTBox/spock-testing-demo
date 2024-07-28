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

    def "cancelOrder should throw UnableToCancelException when OrderStatus is not PENDING or FOR_DELIVERY"() {
        given:
        def cancelOrderRequest = Mock(CancelOrderRequest)
        def orderUUID = UUID.randomUUID()

        cancelOrderRequest.getOrderId() >> orderUUID

        Order order = Mock(Order)
        order.getStatus() >> OrderStatus.DELIVERED

        this.orderRepository.fetchOrderById(orderUUID) >> Optional.ofNullable(order)

        when:
        orderingService.cancelOrder(cancelOrderRequest)

        then:
        thrown(UnableToCancelException)
    }
}
