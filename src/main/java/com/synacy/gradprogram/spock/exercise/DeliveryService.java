package com.synacy.gradprogram.spock.exercise;

public class DeliveryService {

  private final DateUtils dateUtils;
  private final DeliveryRequestRepository deliveryRequestRepository;

  public DeliveryService(DeliveryRequestRepository deliveryRequestRepository, DateUtils dateUtils) {
    this.dateUtils = dateUtils;
    this.deliveryRequestRepository = deliveryRequestRepository;
  }

  public DeliveryRequest createDelivery(Order order) {
    DeliveryRequest deliveryRequest = new DeliveryRequest();
    deliveryRequest.setOrderId(order.getId());
    deliveryRequest.setDeliveryDate(dateUtils.getCurrentDate());
    deliveryRequest.setCourier(determineCourier(order.getTotalCost()));

    deliveryRequestRepository.saveDeliveryRequest(deliveryRequest);

    return deliveryRequest;
  }

  private Courier determineCourier(double orderTotalCost) {
    if (orderTotalCost < 20) {
      return Courier.JRS;
    } else if (orderTotalCost >= 20 && orderTotalCost <= 30) {
      return Courier.GRAB;
    } else {
      return Courier.LBC;
    }
  }

}
