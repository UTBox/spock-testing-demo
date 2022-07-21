package com.synacy.gradprogram.spock

import spock.lang.Specification
import spock.lang.Unroll

class AddressSpec extends Specification {
    def "getCountry should return the country's name"() {
        given:
        Address address = new Address("Philippines", "Cebu", "Pope John Paul II Ave")

        when:
        String country = address.getCountry()

        then:
        "Philippines" == country
    }

    def "setCountry should assign the country's name"() {
        given:
        Address address = new Address("China","Cebu","Pope John Paul II Ave")

        when:
        address.setCountry("Philippines")

        then:
        address.country == "Philippines"

    }

    @Unroll
    def "getCity should return the city's name. City name: #expected"() {
        expect:
        city.getCity() == expected

        where:
        expected << ["Manila", "Cebu","Iloilo","General Santos"]
        city = new Address("Philippines", expected,"Pope John Paul II Ave")
    }

    def "setCity should assign the city's name"() {
        given:
        Address address = new Address("Philippines","Marikina","Pope John Paul II Ave")

        when:
        address.setCity("Cebu")

        then:
        address.city == "Cebu"

    }

    def "getStreet should return the street's name"() {
        given:
        Address address = new Address ("Philippines","Cebu","Colon Street")

        when:
        String street = address.getStreet()

        then:
        street == "Colon Street"
    }

    def "setStreet should assign the name of the street"() {
        given:
        Address address = new Address ("Philippines", "Cebu", "R. Palma Street")

        when:
        address.setStreet("Colon Street")

        then:
        address.street == "Colon Street"
    }

    def "toString should return a string of complete address with country, city, and street"() {
        given:
        Address address = new Address ("Philippines", "Cebu", "Colon Street")

        when:
        String completeAddress = address.toString()

        then:
        completeAddress == "Country: Philippines, City: Cebu, Street: Colon Street"
    }
}
