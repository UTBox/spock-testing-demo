package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;

public class RefundService {

  private final DateUtils dateUtils;
  private final RefundRepository refundRepository;
  private final OrderRepository orderRepository;

  public RefundService(DateUtils dateUtils, RefundRepository refundRepository, OrderRepository orderRepository) {
    this.dateUtils = dateUtils;
    this.refundRepository = refundRepository;
    this.orderRepository = orderRepository;
  }

  private BigDecimal calculateRefund(Order order, CancelOrderRequest cancelOrderRequest) {
    final double DIVISOR = 2.0;

    if (dateUtils.isDateWithinRefundPeriod(order.getDateOrdered()) || cancelOrderRequest.getReason() == CancelReason.DAMAGED) {
      return BigDecimal.valueOf(order.getTotalCost());
    }

    return BigDecimal.valueOf(order.getTotalCost() / DIVISOR);
  }

  public void createAndSaveRefundRequest(CancelOrderRequest cancelOrderRequest) {
    Order order = orderRepository.fetchOrderById(cancelOrderRequest.getOrderId()).get();

    RefundRequest refundRequest = new RefundRequest();
    refundRequest.setOrderId(cancelOrderRequest.getOrderId());
    refundRequest.setRecipientName(order.getRecipientName());
    refundRequest.setStatus(RefundRequestStatus.TO_PROCESS);
    refundRequest.setRefundAmount(calculateRefund(order, cancelOrderRequest));

    refundRepository.saveRefundRequest(refundRequest);
  }

}
