package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;

public class RefundService {

  public BigDecimal calculateRefund(CancelOrderRequest request, Order order) {
    long refundDateLimit = order.getDateOrdered().getTime() + (86400000 * 3);

    if (request.getReason() == CancelReason.DAMAGED) {
      return BigDecimal.valueOf(order.getTotalCost());
    } else if (request.getDateCancelled().getTime() < refundDateLimit) {
      return BigDecimal.valueOf(order.getTotalCost());
    }

    //  TODO: else refund half of the total cost.
    return null;
  }

  private void createAndSaveRefundRequest() {
    // TODO: Implement me. Creates a TO_PROCESS refund request and saves it to the database
  }

}
