package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class RefundService {

  private final RefundRepository refundRepository;
  private final DateUtils dateUtils;

  public RefundService(RefundRepository refundRepository, DateUtils dateUtils) {
    this.refundRepository = refundRepository;
    this.dateUtils = dateUtils;
  }

  private BigDecimal calculateRefund(Order order, CancelOrderRequest request) {
    // TODO: Implement me. Full refund if cancel reason is due to damaged item.
    //  Also full refund if the order was cancelled within 3 days of order date, else refund half of the total cost.

    double orderTotalCost = order.getTotalCost();
    CancelReason orderCancelReason = request.getReason();

    Date orderDate = order.getDateOrdered();

    if (orderCancelReason == CancelReason.DAMAGED || this.isOrderCancelledWithinThreeDays(orderDate)) {
      return BigDecimal.valueOf(orderTotalCost);
    }

    return BigDecimal.valueOf(orderTotalCost / 2);
  }

  private Boolean isOrderCancelledWithinThreeDays(Date orderDate) {
    Date currentDate = new Date();

    if (this.dateUtils.calculateDifferenceInDays(orderDate, currentDate) > RefundConstants.MAX_DAYS_FOR_FULL_REFUND) {
      return false;
    }
    return true;
  }

  public void createAndSaveRefundRequest(Order order, CancelOrderRequest request) {
    // TODO: Implement me. Creates a TO_PROCESS refund request and saves it to the database
    UUID orderId = order.getId();
    String recipientName = order.getRecipientName();

    RefundRequest refundRequest = new RefundRequest();
    refundRequest.setOrderId(orderId);
    refundRequest.setRecipientName(recipientName);
    refundRequest.setRefundAmount(this.calculateRefund(order, request));
    refundRequest.setStatus(RefundRequestStatus.TO_PROCESS);

    this.refundRepository.saveRefundRequest(refundRequest);
  }

}
