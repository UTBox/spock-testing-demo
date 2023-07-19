package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceTest extends Specification {

    OrderingService orderingService

    Item foodItem = new Item("bread", 10.0, ItemType.FOOD)
    Item applianceItem = new Item("toaster", 500.0, ItemType.APPLIANCE)
    Item clothingItem = new Item("shirt", 200.0, ItemType.CLOTHING)
    Item gadgetItem = new Item("phone", 1000.0, ItemType.GADGET)

    Item foodItemLowCost = new Item("small bread", 1.0, ItemType.FOOD)
    Item applianceItemLowCost = new Item("small toaster", 5.0, ItemType.APPLIANCE)
    Item clothingItemLowCost = new Item("small shirt", 2.0, ItemType.CLOTHING)
    Item gadgetItemLowCost = new Item("small phone", 10.0, ItemType.GADGET)

    def setup(){
        orderingService = new OrderingService()
    }

    def "cartContainsFoodItem should return true if Cart has Item with type FOOD"() {
        given:
        List itemListWithEachType = [foodItem, applianceItem, clothingItem, gadgetItem]
        Cart cartWithEachItemType = new Cart(UUID.randomUUID(), itemListWithEachType)

        when:
        boolean containsFoodItem = orderingService.cartContainsFoodItem(cartWithEachItemType)

        then:
        containsFoodItem
    }

    def "cartContainsFoodItem should return false if Cart has no Item with type FOOD"() {
        given:
        List itemListWithoutFood = [applianceItem, clothingItem, gadgetItem]
        Cart cartWithoutFoodItem = new Cart(UUID.randomUUID(), itemListWithoutFood)

        when:
        boolean containsFoodItem = orderingService.cartContainsFoodItem(cartWithoutFoodItem)

        then:
        !containsFoodItem
    }

    def "calculateTotalCostOfCart should return the sum of the cost of each item in cart"() {
        given:
        List itemListWithEachType = [foodItem, applianceItem, clothingItem, gadgetItem]
        Cart cartWithEachItemType = new Cart(UUID.randomUUID(), itemListWithEachType)
        double expectedTotalCost = 1710.0

        when:
        double actualTotalCost = orderingService.calculateTotalCostOfCart(cartWithEachItemType)

        then:
        expectedTotalCost == actualTotalCost
    }
}
