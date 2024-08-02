package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RefundService {

    private final RefundRepository refundRepository;

    public RefundService(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }

    public void createAndSaveRefundRequest(Order order, CancelOrderRequest cancelOrderRequest) {
        UUID orderId = order.getId();
        String recipientName = order.getRecipientName();
        BigDecimal refundAmount = calculateRefund(order, cancelOrderRequest);

        RefundRequest refundRequest = createRefundRequest(orderId, recipientName, refundAmount);
        refundRepository.saveRefundRequest(refundRequest);
    }

    private BigDecimal calculateRefund(Order order, CancelOrderRequest cancelOrderRequest) {
        double totalOrderCost = order.getTotalCost();
        CancelReason cancelReason = cancelOrderRequest.getReason();

        Date orderDate = order.getDateOrdered();
        Date cancelDate = cancelOrderRequest.getDateCancelled();
        long dateDiffInDays = calculateDaysBetween(orderDate, cancelDate);

        if (cancelReason == CancelReason.DAMAGED || dateDiffInDays <= 3) {
            return BigDecimal.valueOf(totalOrderCost);
        }

        return BigDecimal.valueOf(totalOrderCost / 2);
    }

    private RefundRequest createRefundRequest(UUID orderId, String recipientName, BigDecimal refundAmount) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setRecipientName(recipientName);
        refundRequest.setOrderId(orderId);
        refundRequest.setRefundAmount(refundAmount);
        refundRequest.setStatus(RefundRequestStatus.TO_PROCESS);

        return refundRequest;
    }

    private long calculateDaysBetween(Date orderDate, Date cancelDate) {
        LocalDate orderLocaleDate = orderDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate cancelLocaleDate = cancelDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return ChronoUnit.DAYS.between(orderLocaleDate, cancelLocaleDate);
    }

}
