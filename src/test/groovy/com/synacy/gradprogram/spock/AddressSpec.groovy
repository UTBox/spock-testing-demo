package com.synacy.gradprogram.spock

import spock.lang.Specification

class AddressSpec extends Specification {

    def "getCountry should return the country of Address."() {
        given:
        Address address = new Address("Philippines", "Cebu", "Mabolo")

        when:
        String result = address.getCountry()

        then:
        "Philippines" == result
    }

    def "test setCountry"() {
        given:
        Address address = new Address("China", "Cebu", "2nd street")

        when:
        address.setCountry("India")

        then:
        "Philippines" == address.country
    }
//
//    def "test getCity"() {
//        given:
//
//        when:
//        // TODO implement stimulus
//        then:
//        // TODO implement assertions
//    }
//
//    def "test setCity"() {
//        given:
//
//        when:
//        // TODO implement stimulus
//        then:
//        // TODO implement assertions
//    }
//
//    def "test getStreet"() {
//        given:
//
//        when:
//        // TODO implement stimulus
//        then:
//        // TODO implement assertions
//    }
//
//    def "test setStreet"() {
//        given:
//
//        when:
//        // TODO implement stimulus
//        then:
//        // TODO implement assertions
//    }
//
//    def "test toString"() {
//        given:
//
//        when:
//        // TODO implement stimulus
//        then:
//        // TODO implement assertions
//    }
}
