package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;
import java.util.Date;

public class RefundService {
  RefundRepository refundRepository;
  DateUtils dateUtils;

  public RefundService(RefundRepository refundRepository, DateUtils dateUtils){
    this.refundRepository = refundRepository;
    this.dateUtils = dateUtils;
  }

  private BigDecimal calculateRefund(Order order, CancelOrderRequest cancelOrderRequest) {
    int refundDateThreshold = 3;

    if(cancelOrderRequest.getReason() == CancelReason.DAMAGED){
      return BigDecimal.valueOf(order.getTotalCost());
    }

    if(isRefundFull(order.getDateOrdered(), cancelOrderRequest.getDateCancelled(), refundDateThreshold)){
      return BigDecimal.valueOf(order.getTotalCost());
    }

    return BigDecimal.valueOf(order.getTotalCost() / 2);
  }

  public void createAndSaveRefundRequest(Order order, CancelOrderRequest cancelOrderRequest) {
    RefundRequest refundRequest = createRefundRequest(order, cancelOrderRequest);
    refundRepository.saveRefundRequest(refundRequest);
  }

  public RefundRequest createRefundRequest(Order order, CancelOrderRequest cancelOrderRequest){

    RefundRequest refundRequest = new RefundRequest();
    refundRequest.setRecipientName(order.getRecipientName());
    refundRequest.setOrderId(order.getId());
    refundRequest.setRefundAmount(this.calculateRefund(order, cancelOrderRequest));
    refundRequest.setStatus(RefundRequestStatus.TO_PROCESS);

    return refundRequest;
  }

  private boolean isRefundFull(Date orderDate, Date cancelDate, int refundDateThreshold){
    return dateUtils.getDateDiffInDays(orderDate, cancelDate) <= refundDateThreshold;
  }
}
