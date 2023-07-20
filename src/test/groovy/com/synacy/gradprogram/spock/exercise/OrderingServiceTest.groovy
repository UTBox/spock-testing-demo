package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class OrderingServiceTest extends Specification {

    OrderingService service
    
    def setup() {

        service = new OrderingService()
    }

    def "cartContainsFoodItem should verify if the cart has food items."() {
        given:
            Item nuggets = new Item("Nuggets", 185, ItemType.FOOD)
            Item ham = new Item("Ham", 198, ItemType.FOOD)
            Item ipad = new Item("iPad", 29990, ItemType.GADGET)
            Item appleJuice = new Item("Apple Juice", 174, ItemType.FOOD)

            List<Item> items = [nuggets, ham, ipad, appleJuice]

            Cart itemsInTheCart = new Cart(UUID.randomUUID(), items)

        when:
            boolean hasFoodItems = service.cartContainsFoodItem(itemsInTheCart)

        then:
            hasFoodItems
    }

    def "cartContainsFoodItem should verify if the cart has no food items."() {
        given:
            Item iphone = new Item("iPhone", 62000, ItemType.GADGET)
            Item macBook = new Item("MacBook", 78000, ItemType.GADGET)
            Item mouse = new Item("Mouse", 300, ItemType.GADGET)
            Item keyboard = new Item("Keyboard", 400, ItemType.GADGET)

            List<Item> items = [iphone, macBook, mouse, keyboard]

            Cart itemsInTheCart = new Cart(UUID.randomUUID(), items)


        when:
            boolean hasFoodItems = service.cartContainsFoodItem(itemsInTheCart)

        then:
            !hasFoodItems
    }
}
