package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;

public class RefundService {

  private final RefundRepository refundRepository;

  public RefundService(RefundRepository refundRepository) {
    this.refundRepository = refundRepository;
  }

  public BigDecimal calculateRefund(CancelOrderRequest request, Order order) {
    long refundDateLimit = order.getDateOrdered().getTime() + (86400000 * 3);
    BigDecimal refundAmount;

    if (request.getReason() == CancelReason.DAMAGED || request.getDateCancelled().getTime() < refundDateLimit) {
      refundAmount = BigDecimal.valueOf(order.getTotalCost());
    } else {
      refundAmount = BigDecimal.valueOf(order.getTotalCost()/2);
    }
    return refundAmount;
  }

  private void createAndSaveRefundRequest(Order order) {
    // TODO: Implement me. Creates a TO_PROCESS refund request and saves it to the database

  }

}
