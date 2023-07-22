package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {

    RefundService service
    OrderingService orderingService = Mock(OrderingService)
    RefundRequest refundRequest
    void setup() {
        service = new RefundService(orderingService)
        refundRequest = new RefundRequest()
    }

    def "calculateRefund should give full refund due to damage item cancel reason"() {
        given:
        List<Item> items = [new Item("Fried Chicken", 50.00d, ItemType.FOOD),
                            new Item("Nike", 500.00d, ItemType.CLOTHING),
                            new Item("Rice Cooker", 300.75d, ItemType.APPLIANCE),
                            new Item("IPhone", 23000.00d, ItemType.GADGET)]
        double totalCost = 23650.75d
        Cart cart = new Cart(UUID.randomUUID(), items)
        orderingService.calculateTotalCostOfCart(cart) >> totalCost

        when:
        BigDecimal refundAmount = service.calculateRefund(CancelReason.DAMAGED, cart, refundRequest)

        then:
        refundAmount == BigDecimal.valueOf(totalCost)
        refundRequest.getRefundAmount() == BigDecimal.valueOf(totalCost)
    }
}
