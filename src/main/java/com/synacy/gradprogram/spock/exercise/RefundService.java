package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;
import java.util.UUID;

public class RefundService {
  RefundRepository refundRepository;

  public RefundService(RefundRepository refundRepository){
    this.refundRepository = refundRepository;
  }

  public BigDecimal calculateRefund() {
    // TODO: Implement me. Full refund if cancel reason is due to damaged item.
    //  Also full refund if the order was cancelled within 3 days of order date, else refund half of the total cost.
    return null;
  }

  public void createAndSaveRefundRequest(String recipientName, UUID orderId) {
    RefundRequest refundRequest = createRefundRequest(recipientName, orderId);
    refundRepository.saveRefundRequest(refundRequest);
  }

  private RefundRequest createRefundRequest(String recipientName, UUID orderId){
    RefundRequest refundRequest = new RefundRequest();
    refundRequest.setRecipientName(recipientName);
    refundRequest.setOrderId(orderId);
    refundRequest.setRefundAmount(this.calculateRefund());
    refundRequest.setStatus(RefundRequestStatus.TO_PROCESS);

    return refundRequest;
  }
}
