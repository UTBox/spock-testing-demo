package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class RefundService {

  private final RefundRepository refundRepository;

  public RefundService(RefundRepository refundRepository) {
    this.refundRepository = refundRepository;
  }

  public BigDecimal calculateRefund(CancelReason cancelReason, Order order) {
    double refundAmount =  order.getTotalCost();
    boolean isWithinThreeDays = DateUtils.isMoreThanThreeDaysAgo(order.getDateOrdered());
    if (cancelReason == CancelReason.DAMAGED || isWithinThreeDays) {

      return BigDecimal.valueOf(refundAmount);
    } else if (cancelReason == CancelReason.WRONG_ITEM) {

      return BigDecimal.valueOf(refundAmount/2);
    }

    return BigDecimal.valueOf(refundAmount/2);
  }
  public void createAndSaveRefundRequest(UUID orderId, String recipientName, BigDecimal refundAmount) {
    RefundRequest refundRequest = new RefundRequest();
    refundRequest.setOrderId(orderId);
    refundRequest.setRecipientName(recipientName);
    refundRequest.setRefundAmount(refundAmount);
    refundRequest.setStatus(RefundRequestStatus.TO_PROCESS);

    refundRepository.saveRefundRequest(refundRequest);
  }
}
