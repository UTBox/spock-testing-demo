package com.synacy.gradprogram.spock

import spock.lang.Specification

class AddressSpec extends Specification {
    def "getCountry should return the country of Address"() {
        given:
        def ph_address = new Address("Philippines", "Cebu City", "Escario")
        when:
        // TODO implement stimulus
        String countryLocation = ph_address.country
        then:
        // TODO implement assertions
        countryLocation == "Philippines"
    }
//
    def "setCountry should demonstrate"() {
        given:
        def phAddress = new Address("Cuba", "Talisay", "Escario")

        when:
        phAddress.setCountry("Philippines")
        // TODO implement stimulus
        then:
        phAddress.getCountry() == "Philippines"
        // TODO implement assertions
    }
//
    def "getCity should return the city of Address"() {
        given:
        def phAddress = new Address("Philippines", "Cebu City", "Escario")
        when:
        String cityLocation = phAddress.city
        // TODO implement stimulus
        then:
        "Cebu City" == cityLocation
        // TODO implement assertions
    }
//
    def "setCity"() {
        given:
        def phAddress = new Address("PH", "Cebu", "Escario")
        when:
        phAddress.setCountry("Talisay")
        // TODO implement stimulus
        then:
        phAddress.getCountry() == "Cebu"
        // TODO implement assertions
    }
//
    def "getStreet should return the street of Address"() {
        given:
        def phAddress = new Address("PH", "Cebu", "Escario")
        when:
        String streetLocation = phAddress.getStreet()
        // TODO implement stimulus
        then:
        "Escario" == streetLocation
        // TODO implement assertions
    }
//
    def "test setStreet"() {
        given:
        def phAddress = new Address("Philippines", "Cebu", "Escario")
        when:
        // TODO implement stimulus
        phAddress.setCountry("Escario")
        then:
        phAddress.getCountry() == "Escario"
        // TODO implement assertions
    }
//
    def "test toString"() {
        given:
        def phAddress = new Address("Philippines", "Cebu", "Escario")
        when:
        def message = phAddress.toString()
        // TODO implement stimulus
        then:
        message == "Country: " + phAddress.country +
                ", City: " + phAddress.city +
                ", Street: " + phAddress.street
        // TODO implement assertions

    }
//        public static void checkAddressParameters() {
//
//        }
}
