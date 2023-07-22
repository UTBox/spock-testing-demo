package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;

public class RefundService {

  private final OrderingService orderingService;


  public RefundService(OrderingService orderingService) {
    this.orderingService = orderingService;
  }

  public BigDecimal calculateRefund(CancelReason cancelReason, Cart cart, RefundRequest refundRequest) {
    // TODO: Implement me. Full refund if cancel reason is due to damaged item.
    //  Also full refund if the order was cancelled within 3 days of order date, else refund half of the total cost.
    double refundAmount = orderingService.calculateTotalCostOfCart(cart);
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
