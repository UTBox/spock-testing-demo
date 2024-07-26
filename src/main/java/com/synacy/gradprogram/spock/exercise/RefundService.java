package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;
import java.util.UUID;

public class RefundService {

  public BigDecimal calculateRefund() {
    // TODO: Implement me. Full refund if cancel reason is due to damaged item.
    //  Also full refund if the order was cancelled within 3 days of order date, else refund half of the total cost.
    return null;
  }

  public void createAndSaveRefundRequest(String recipientName, UUID orderId) {
    // TODO: Implement me. Creates a TO_PROCESS refund request and saves it to the database
  }

}
