package synacy.gradprogram.spock.exercise

import com.synacy.gradprogram.spock.exercise.CancelOrderRequest
import com.synacy.gradprogram.spock.exercise.CancelReason
import com.synacy.gradprogram.spock.exercise.DateUtils
import com.synacy.gradprogram.spock.exercise.Order
import com.synacy.gradprogram.spock.exercise.RefundConstants
import com.synacy.gradprogram.spock.exercise.RefundRepository
import com.synacy.gradprogram.spock.exercise.RefundRequest
import com.synacy.gradprogram.spock.exercise.RefundRequestStatus
import com.synacy.gradprogram.spock.exercise.RefundService
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZoneId

class RefundServiceSpec extends Specification {
    RefundService refundService

    RefundRepository refundRepository = Mock(RefundRepository)
    DateUtils dateUtils = Mock(DateUtils)

    void setup() {
        refundService = new RefundService(refundRepository, dateUtils)
    }

    def "createAndSaveRefundRequest should save a refund request with full refund with DAMAGED or WRONG_ITEM CancelReason and is cancelled in 2 days"() {
        given:
        def order = Mock(Order)
        def cancelRequest = Mock(CancelOrderRequest)

        def orderUUID = UUID.randomUUID()
        def recipientName = "Pope Francis"
        def orderTotalCost = 500.0
        def dateOrdered = new Date()
        def daysSinceOrder = 2

        order.getId() >> orderUUID
        order.getRecipientName() >> recipientName
        order.getTotalCost() >> orderTotalCost
        order.getDateOrdered() >> dateOrdered

        cancelRequest.getReason() >> orderCancelReason

        this.dateUtils.calculateDifferenceInDays(_ as Date,_ as Date) >> daysSinceOrder

        when:
        this.refundService.createAndSaveRefundRequest(order, cancelRequest)

        then:
        1 * this.refundRepository.saveRefundRequest(_ as RefundRequest) >> { RefundRequest refundRequest ->
            assert refundRequest.getRecipientName() == recipientName
            assert refundRequest.getOrderId() == orderUUID
            assert refundRequest.getRefundAmount() == BigDecimal.valueOf(500.0);
            assert refundRequest.getStatus() == RefundRequestStatus.TO_PROCESS
        }

        where:
        orderCancelReason << [CancelReason.DAMAGED, CancelReason.WRONG_ITEM]
    }

    def "createAndSaveRefundRequest should save a refund request with a half refund with WRONG_ITEM CancelReason and is cancelled in after 4 days"() {
        given:
        def order = Mock(Order)
        def cancelRequest = Mock(CancelOrderRequest)

        def orderUUID = UUID.randomUUID()
        def recipientName = "Pope Francis"
        def orderTotalCost = 500.0
        def mockDate = generateMockOrderDate()
        def daysSinceOrder = 4

        order.getId() >> orderUUID
        order.getRecipientName() >> recipientName
        order.getTotalCost() >> orderTotalCost
        order.getDateOrdered() >> mockDate

        cancelRequest.getReason() >> CancelReason.WRONG_ITEM

        this.dateUtils.calculateDifferenceInDays(_ as Date,_ as Date) >> daysSinceOrder

        when:
        this.refundService.createAndSaveRefundRequest(order, cancelRequest)

        then:
        1 * this.refundRepository.saveRefundRequest(_ as RefundRequest) >> { RefundRequest refundRequest ->
            assert refundRequest.getRecipientName() == recipientName
            assert refundRequest.getOrderId() == orderUUID
            assert refundRequest.getRefundAmount() == BigDecimal.valueOf(250.0);
            assert refundRequest.getStatus() == RefundRequestStatus.TO_PROCESS
        }
    }

    def "createAndSaveRefundRequest should save a refund request with full refund with DAMAGED CancelReason and is cancelled after 4 days"() {
        given:
        def order = Mock(Order)
        def cancelRequest = Mock(CancelOrderRequest)

        def orderUUID = UUID.randomUUID()
        def recipientName = "Pope Francis"
        def orderTotalCost = 500.0
        def daysSinceOrder = 4

        order.getId() >> orderUUID
        order.getRecipientName() >> recipientName
        order.getTotalCost() >> orderTotalCost

        cancelRequest.getReason() >> CancelReason.DAMAGED

        this.dateUtils.calculateDifferenceInDays(_ as Date,_ as Date) >> daysSinceOrder

        when:
        this.refundService.createAndSaveRefundRequest(order, cancelRequest)

        then:
        1 * this.refundRepository.saveRefundRequest(_ as RefundRequest) >> { RefundRequest refundRequest ->
            assert refundRequest.getRecipientName() == recipientName
            assert refundRequest.getOrderId() == orderUUID
            assert refundRequest.getRefundAmount() == BigDecimal.valueOf(500.0);
            assert refundRequest.getStatus() == RefundRequestStatus.TO_PROCESS
        }
    }

    def generateMockOrderDate() {
        LocalDate localDate = LocalDate.now().minusDays(RefundConstants.MAX_DAYS_FOR_FULL_REFUND + 1)
        Date mockDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return mockDate
    }

}