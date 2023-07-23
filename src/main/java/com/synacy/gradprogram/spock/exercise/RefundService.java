package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;
import java.util.Date;

public class RefundService {

  public BigDecimal calculateRefund(CancelReason cancelReason, Order order, RefundRequest refundRequest) {
    double refundAmount =  order.getTotalCost();
    boolean isMoreThanThreeDaysAgo = DateUtils.isMoreThanThreeDaysAgo(order.getDateOrdered());
    if (cancelReason == CancelReason.DAMAGED || isMoreThanThreeDaysAgo) {
      refundRequest.setRefundAmount(BigDecimal.valueOf(refundAmount));

      return BigDecimal.valueOf(refundAmount);
    } else if (cancelReason == CancelReason.WRONG_ITEM) {
      refundRequest.setRefundAmount(BigDecimal.valueOf(refundAmount/2));

      return BigDecimal.valueOf(refundAmount/2);
    }

    return BigDecimal.valueOf(refundAmount/2);
  }
  private void createAndSaveRefundRequest() {
    // TODO: Implement me. Creates a TO_PROCESS refund request and saves it to the database
  }

}
