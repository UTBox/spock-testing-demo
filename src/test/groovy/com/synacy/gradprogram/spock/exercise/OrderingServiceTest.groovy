package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceTest extends Specification {

    OrderingService orderingService

    Item foodItem1 = new Item("bread", 10.0, ItemType.FOOD)
    Item applianceItem1 = new Item("toaster", 500.0, ItemType.APPLIANCE)
    Item clothingItem1 = new Item("shirt", 200.0, ItemType.CLOTHING)
    Item gadgetItem1 = new Item("phone", 1000.0, ItemType.GADGET)

    Item foodItem2 = new Item("small bread", 1.0, ItemType.FOOD)
    Item applianceItem2 = new Item("small toaster", 5.0, ItemType.APPLIANCE)
    Item clothingItem2 = new Item("small shirt", 2.0, ItemType.CLOTHING)
    Item gadgetItem2 = new Item("small phone", 10.0, ItemType.GADGET)

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

    def "calculateTotalCostOfCart should return the sum of the cost of each item in cart"() {
        given:
        List itemListWithEachType = [foodItem1, applianceItem1, clothingItem1, gadgetItem1]
        Cart cartWithEachItemType = new Cart(UUID.randomUUID(), itemListWithEachType)
        double expectedTotalCost = 1710.0

        when:
        double actualTotalCost = orderingService.calculateTotalCostOfCart(cartWithEachItemType)

        then:
        expectedTotalCost == actualTotalCost
    }
}
