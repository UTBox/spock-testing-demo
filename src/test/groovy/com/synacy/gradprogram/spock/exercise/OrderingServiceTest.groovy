package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceTest extends Specification {

    OrderingService orderingService

    // ITEM TEMPLATE
    Item foodItem1 = new Item("bread", 10.0, ItemType.FOOD)
    Item applianceItem1 = new Item("toaster", 500.0, ItemType.APPLIANCE)
    Item clothingItem1 = new Item("shirt", 200.0, ItemType.CLOTHING)
    Item gadgetItem1 = new Item("phone", 1000.0, ItemType.GADGET)

    def setup(){
        orderingService = new OrderingService()
    }

    def "cartContainsFoodItem should return true if Cart has Item with type FOOD"() {
        given:
        List itemListWithEachType = [foodItem1, applianceItem1, clothingItem1, gadgetItem1]
        Cart cartWithEachItemType = new Cart(UUID.randomUUID(), itemListWithEachType)

        when:
        boolean containsFoodItem = orderingService.cartContainsFoodItem(cartWithEachItemType)

        then:
        containsFoodItem
    }

    def "cartContainsFoodItem should return false if Cart has no Item with type FOOD"() {
        given:
        List itemListWithoutFood = [applianceItem1, clothingItem1, gadgetItem1]
        Cart cartWithoutFoodItem = new Cart(UUID.randomUUID(), itemListWithoutFood)

        when:
        boolean containsFoodItem = orderingService.cartContainsFoodItem(cartWithoutFoodItem)

        then:
        !containsFoodItem
    }

}
