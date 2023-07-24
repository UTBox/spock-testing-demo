package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;
import java.util.UUID;

public class RefundService {

  private final RefundRepository refundRepository;
  private final RefundRequest refundRequest;

  public RefundService(RefundRepository refundRepository, RefundRequest refundRequest) {
    this.refundRepository = refundRepository;
    this.refundRequest = refundRequest;
  }

  public BigDecimal calculateRefund(CancelReason cancelReason, Order order) {
    double refundAmount =  order.getTotalCost();
    boolean isMoreThanThreeDaysAgo = DateUtils.isMoreThanThreeDaysAgo(order.getDateOrdered());
    if (cancelReason == CancelReason.DAMAGED || isMoreThanThreeDaysAgo) {
      refundRequest.setOrderId(order.getId());
      refundRequest.setRecipientName(order.getRecipientName());
      refundRequest.setStatus(RefundRequestStatus.TO_PROCESS);
      refundRequest.setRefundAmount(BigDecimal.valueOf(refundAmount));

      return BigDecimal.valueOf(refundAmount);
    } else if (cancelReason == CancelReason.WRONG_ITEM) {
      refundRequest.setOrderId(order.getId());
      refundRequest.setRecipientName(order.getRecipientName());
      refundRequest.setStatus(RefundRequestStatus.TO_PROCESS);
      refundRequest.setRefundAmount(BigDecimal.valueOf(refundAmount/2));

      return BigDecimal.valueOf(refundAmount/2);
    }

    return BigDecimal.valueOf(refundAmount/2);
  }
  private void createAndSaveRefundRequest(UUID orderId, String recipientName, BigDecimal refundAmount) {
    // TODO: Implement me. Creates a TO_PROCESS refund request and saves it to the database
    RefundRequest refundRequest = new RefundRequest();
    refundRequest.setOrderId(orderId);
    refundRequest.setRecipientName(recipientName);
    refundRequest.setRefundAmount(refundAmount);
    refundRequest.setStatus(RefundRequestStatus.TO_PROCESS);

    refundRepository.saveRefundRequest(refundRequest);
  }
}
