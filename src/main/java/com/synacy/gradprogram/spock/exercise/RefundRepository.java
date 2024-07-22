package com.synacy.gradprogram.spock.exercise;

// Note: This is a dummy implementation and for the exercise purpose only
public class RefundRepository {

  public void saveRefundRequest(RefundRequest request) {
    // This method saves the RefundRequest to the database
    System.out.println("Saving RefundRequest to DB with details: "
        + request.getOrderId() + ", "
        + request.getRecipientName() + ", "
        + request.getRefundAmount() + ", "
        + request.getStatus()
    );
  }

}
