package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RefundService {

    private final OrderRepository orderRepository;
    private final RefundRepository refundRepository;

    public RefundService(OrderRepository orderRepository, RefundRepository refundRepository) {
        this.orderRepository = orderRepository;
        this.refundRepository = refundRepository;
    }

    public BigDecimal calculateRefund(RefundRequest refundRequest, CancelOrderRequest cancelOrderRequest) {
        // TODO: Implement me. Full refund if cancel reason is due to damaged item.
        //  Also full refund if the order was cancelled within 3 days of order date, else refund half of the total cost.
        BigDecimal refundAmount = refundRequest.getRefundAmount();
        CancelReason cancelReason = cancelOrderRequest.getReason();
        if (cancelReason == CancelReason.DAMAGED) {
            return refundAmount;
        }

        Order order = orderRepository.fetchOrderById(refundRequest.getOrderId()).get();
        Date orderDate = order.getDateOrdered();
        Date cancelDate = cancelOrderRequest.getDateCancelled();

        long dateDiffInDays = TimeUnit.DAYS.convert(
                cancelDate.getTime() - orderDate.getTime(), TimeUnit.MILLISECONDS);

        if(dateDiffInDays <= 3){
            return refundAmount;
        }

        return refundAmount.divide(BigDecimal.valueOf(2));
    }

    public void createAndSaveRefundRequest(Order order) {
        // TODO: Implement me. Creates a TO_PROCESS refund request and saves it to the database
        RefundRequest refundRequest = new RefundRequest();

        refundRequest.setRecipientName(order.getRecipientName());
        refundRequest.setOrderId(order.getId());
        refundRequest.setRefundAmount(BigDecimal.valueOf(order.getTotalCost()));
        refundRequest.setStatus(RefundRequestStatus.TO_PROCESS);

        refundRepository.saveRefundRequest(refundRequest);
    }

}
