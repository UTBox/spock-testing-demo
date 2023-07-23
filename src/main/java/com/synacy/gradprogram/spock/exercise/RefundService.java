package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;

public class RefundService {

  public BigDecimal calculateRefund(CancelReason cancelReason, Order order, RefundRequest refundRequest) {
    // TODO: Implement me. Full refund if cancel reason is due to damaged item.
    //  Also full refund if the order was cancelled within 3 days of order date, else refund half of the total cost.
    double refundAmount =  order.getTotalCost();
    if (cancelReason == CancelReason.DAMAGED) {
      refundRequest.setRefundAmount(BigDecimal.valueOf(refundAmount));

      return BigDecimal.valueOf(refundAmount);
    }
    return null;
  }
  private void createAndSaveRefundRequest() {
    // TODO: Implement me. Creates a TO_PROCESS refund request and saves it to the database
  }

}
