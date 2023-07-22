package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {

    RefundService service
    OrderingService orderingService = Mock(OrderingService)
    RefundRequest refundRequest = Mock(RefundRequest)

    void setup() {
        service = new RefundService(orderingService, refundRequest)
    }

    def "calculateRefund should give full refund due to damage item cancel reason"() {
        given:
        List<Item> items = [new Item("Fried Chicken", 50.00d, ItemType.FOOD),
                            new Item("Nike", 500.00d, ItemType.CLOTHING),
                            new Item("Rice Cooker", 300.75d, ItemType.APPLIANCE),
                            new Item("IPhone", 23000.00d, ItemType.GADGET)]
        Cart cart = new Cart(UUID.randomUUID(), items)
        orderingService.calculateTotalCostOfCart(cart) >> 23650.75d

        when:
        BigDecimal refundAmount = service.calculateRefund(CancelReason.DAMAGED, cart)

        then:
        refundAmount == BigDecimal.valueOf(23650.75d)
    }
}
