package com.synacy.gradprogram.spock

import spock.lang.Specification

class AddressSpec extends Specification {
    def "getCountry should return the country of Address"() {
        given:
        def ph_address = new Address("Philippines", "Cebu City", "Escario")

        when:
        String countryLocation = ph_address.country

        then:
        countryLocation == "Philippines"
    }

    def "setCountry should demonstrate"() {
        given:
        def phAddress = new Address("Cuba", "Talisay", "Escario")

        when:
        phAddress.setCountry("Philippines")

        then:
        phAddress.getCountry() == "Philippines"

    }

    def "getCity should return the city of Address"() {
        given:
        def phAddress = new Address("Philippines", "Cebu City", "Escario")
        when:
        String cityLocation = phAddress.city

        then:
        "Cebu City" == cityLocation

    }

    def "setCity should update the given City"() {
        given:
        def phAddress = new Address("PH", "Cebu", "Escario")
        when:
        phAddress.setCountry("Talisay")

        then:
        phAddress.getCountry() == "Cebu"

    }

    def "getStreet should return the street of Address"() {
        given:
        def phAddress = new Address("PH", "Cebu", "Escario")

        when:
        String streetLocation = phAddress.getStreet()

        then:
        "Escario" == streetLocation

    }

    def "setStreet should update the given Street"() {
        given:
        def phAddress = new Address("Philippines", "Cebu", "Escario")

        when:
        phAddress.setStreet("Escario")

        then:
        phAddress.getStreet() == "Escario"

    }

    def "toString should provide a string of the parameters initialized in Address"() {
        given:
        def phAddress = new Address("Philippines", "Cebu", "Escario")

        when:
        def message = phAddress.toString()

        then:
        message == "Country: " + phAddress.country +
                ", City: " + phAddress.city +
                ", Street: " + phAddress.street


    }
}
