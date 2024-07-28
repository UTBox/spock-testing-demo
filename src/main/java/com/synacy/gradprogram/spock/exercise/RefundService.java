package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;

public class RefundService {

  private BigDecimal calculateRefund() {
    // TODO: Implement me. Full refund if cancel reason is due to damaged item.
    //  Also full refund if the order was cancelled within 3 days of order date, else refund half of the total cost.
    return null;
  }

  public void createAndSaveRefundRequest(Order order, CancelReason reason) {
    // TODO: Implement me. Creates a TO_PROCESS refund request and saves it to the database
  }

}
