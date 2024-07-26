package com.synacy.gradprogram.spock.exercise;

import java.util.Date;

public class OrderSummary {
  private final double totalCost;
  private final OrderStatus status;
  private final Date deliveryDate;
  private final Courier courier;

  public OrderSummary(double totalCost, OrderStatus status, Date deliveryDate, Courier courier) {
    this.totalCost = totalCost;
    this.status = status;
    this.deliveryDate = deliveryDate;
    this.courier = courier;
  }

  public double getTotalCost() {
    return totalCost;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public Date getDeliveryDate() {
    return deliveryDate;
  }

  public Courier getCourier() {
    return courier;
  }
}
