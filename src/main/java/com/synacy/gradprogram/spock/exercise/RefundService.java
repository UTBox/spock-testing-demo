package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;

public class RefundService {

  public BigDecimal calculateRefund(CancelOrderRequest request, Order order) {
    if (request.getReason() == CancelReason.DAMAGED) {
      return BigDecimal.valueOf(order.getTotalCost());
    }
    //  TODO: Also full refund if the order was cancelled within 3 days of order date, else refund half of the total cost.
    return null;
  }

  private void createAndSaveRefundRequest() {
    // TODO: Implement me. Creates a TO_PROCESS refund request and saves it to the database
  }

}
