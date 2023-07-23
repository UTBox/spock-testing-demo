package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;
import java.util.Date;

public class RefundService {

  public BigDecimal calculateRefund(CancelReason cancelReason, Order order, RefundRequest refundRequest) {
    // TODO: Implement me. Full refund if cancel reason is due to damaged item.
    //  Also full refund if the order was cancelled within 3 days of order date, else refund half of the total cost.
    double refundAmount =  order.getTotalCost();
    Date threeDaysAgo = order.getDateOrdered();
    boolean within3Days = order.getDateOrdered().after(threeDaysAgo);
    if (cancelReason == CancelReason.DAMAGED || within3Days) {
      refundRequest.setRefundAmount(BigDecimal.valueOf(refundAmount));

      return BigDecimal.valueOf(refundAmount);
    }
    return null;
  }
  private void createAndSaveRefundRequest() {
    // TODO: Implement me. Creates a TO_PROCESS refund request and saves it to the database
  }

}
