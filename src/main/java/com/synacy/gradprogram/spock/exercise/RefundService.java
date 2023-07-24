package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;

public class RefundService {

  private final RefundRepository refundRepository;

  public RefundService(RefundRepository refundRepository) {
    this.refundRepository = refundRepository;
  }

  public BigDecimal calculateRefund(CancelOrderRequest request, Order order) {
    long millisecondsInThreeDays = 86400000 * 3;
    long refundDateLimit = order.getDateOrdered().getTime() + millisecondsInThreeDays;
    BigDecimal refundAmount;

    if (request.getReason() == CancelReason.DAMAGED || request.getDateCancelled().getTime() < refundDateLimit) {
      refundAmount = BigDecimal.valueOf(order.getTotalCost());
    } else {
      refundAmount = BigDecimal.valueOf(order.getTotalCost()/2);
    }

    return refundAmount;
  }

  public void createAndSaveRefundRequest(Order order, BigDecimal refundAmount) {
    RefundRequest refundRequest = new RefundRequest();

    refundRequest.setStatus(RefundRequestStatus.TO_PROCESS);
    refundRequest.setRecipientName(order.getRecipientName());
    refundRequest.setRefundAmount(refundAmount);
    refundRequest.setOrderId(order.getId());

    refundRepository.saveRefundRequest(refundRequest);
  }

}
