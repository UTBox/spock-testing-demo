package com.synacy.gradprogram.spock.exercise;

import java.math.BigDecimal;

public class RefundService {

  private final RefundRepository refundRepository;
  private final OrderRepository orderRepository;

  public RefundService(RefundRepository refundRepository, OrderRepository orderRepository) {
    this.refundRepository = refundRepository;
    this.orderRepository = orderRepository;
  }

  private BigDecimal calculateRefund() {
    // TODO: Implement me. Full refund if cancel reason is due to damaged item.
    //  Also full refund if the order was cancelled within 3 days of order date, else refund half of the total cost.
    return null;
  }

  public void createAndSaveRefundRequest(CancelOrderRequest request) {
    // TODO: Implement me. Creates a TO_PROCESS refund request and saves it to the database
    Order order = orderRepository.fetchOrderById(request.getOrderId()).get();

    // Placeholder BigDecimal amount:
    String refundAmountString = "2500";
    BigDecimal refundAmount = new BigDecimal(refundAmountString);

    RefundRequest refundRequest = new RefundRequest();
    refundRequest.setOrderId(request.getOrderId());
    refundRequest.setRecipientName(order.getRecipientName());
    refundRequest.setStatus(RefundRequestStatus.TO_PROCESS);
    refundRequest.setRefundAmount(refundAmount);

    refundRepository.saveRefundRequest(refundRequest);
  }

}
