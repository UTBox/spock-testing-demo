package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class RefundService {

  private final RefundRepository refundRepository;

  private RefundService(RefundRepository refundRepository) {
    this.refundRepository = refundRepository;
  }
  public BigDecimal calculateRefund(CancelOrderRequest request, Order order) {
    Date dateOrdered = order.getDateOrdered();
    BigDecimal halfAmount = BigDecimal.valueOf(order.getTotalCost()).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);

    if(request.getReason() == CancelReason.WRONG_ITEM ){
      return halfAmount;
    }else if(request.getReason() == CancelReason.DAMAGED || DateUtils.isWithinThreeDays(dateOrdered)){
      return BigDecimal.valueOf(order.getTotalCost());
    }
    return halfAmount;
  }

  private void createAndSaveRefundRequest(Order order, BigDecimal refundAmount) {
    RefundRequest request = new RefundRequest();
    request.setRecipientName(order.getRecipientName());
    request.setOrderId(order.getId());
    request.setRefundAmount(refundAmount);
    request.setStatus(RefundRequestStatus.TO_PROCESS);

    refundRepository.saveRefundRequest(request);
  }

}
