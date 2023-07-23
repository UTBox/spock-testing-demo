package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;
import java.util.Date;

public class RefundService {

  private RefundRepository refundRepository;
  private RefundRequestStatus refundRequestStatus;
  public RefundService(RefundRepository refundRepository, RefundRequestStatus refundRequestStatus) {
    this.refundRepository = refundRepository;
    this.refundRequestStatus = refundRequestStatus;
  }
  public BigDecimal calculateRefund(Date orderDate, CancelReason cancelReason, BigDecimal totalCost) {
// TODO: Implement me. Full refund if cancel reason is due to damaged item.
    //  Also full refund if the order was cancelled within 3 days of order date, else refund half of the total cost.
    long daysSinceOrder = daysBetween(orderDate, new Date());

    if (cancelReason == CancelReason.DAMAGED || daysSinceOrder <=3){
      return totalCost;
    }else {
      return totalCost.divide(new BigDecimal("2"));
    }

  }
  private long daysBetween(Date startDate, Date endDate) {
    long diff = endDate.getTime() - startDate.getTime();
    return diff / (24 * 60 * 60 * 1000);
  }

  public void createAndSaveRefundRequest(RefundRequest refundRequest) {
    // TODO: Implement me. Creates a TO_PROCESS refund request and saves it to the database
    if(refundRequest.getStatus() == RefundRequestStatus.TO_PROCESS){
      refundRepository.saveRefundRequest(refundRequest);
    }
  }

}
