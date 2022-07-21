package com.synacy.gradprogram.spock

import spock.lang.Specification

class PersonSpec extends Specification {


    def "getName should return a name"() {
        given:
        def address = Mock(Address)
        Person person = new Person ("Kim",22,address,Sex.MALE)

        when:
        String Name = person.getName()

        then:
        Name == person.name
    }

    def "setName should provide or assign value to name"() {
        given:
        def address = Mock(Address)
        Person person = new Person ("Kim",22,address,Sex.MALE)

        when:
        person.setName("Irvin")

        then:
        "Irvin"== person.name
    }

    def "getAge takes and returns an integer value of the age of a person"() {
        given:
        def address = Mock(Address)
        Person person = new Person ("Kim",22,address,Sex.MALE)

        when:
        int Age = person.getAge()

        then:
        Age == person.age
    }

    def "setAge assigns an integer value for Age"() {
        given:
        def address = Mock(Address)
        Person person = new Person ("Kim",22,address,Sex.MALE)

        when:
        person.setAge(18)

        then:
        18 == person.age
    }

    def "getAddress calls the calls the Address Class and returns the toString value of address"() {
        given:
        def address = new Address ("Philippines","Cebu","Colon Street")
        Person person = new Person ("Kim", 22, address, Sex.MALE)

        when:
        String CompleteAddress = person.getAddress()

        then:
        "Country: "+address.country+", City: "+address.city+", Street: "+address.street == CompleteAddress
    }


    def "setAddress assigns the address from the Address Class"() {
        given:
        def address = Mock(Address)
        Person person = new Person ("Kim",22, address,Sex.MALE)

        when:
        address = new Address ("Philippines","Cebu","Colon Street")
        person.setAddress(address)

        then:
        "Country: Philippines, City: Cebu, Street: Colon Street" == person.address.toString()
    }

    def "getSex returns the assigned value for sex"() {
        given:
        def address = Mock(Address)
        Person person = new Person ("Kim",22,address,Sex.MALE)

        expect:
        Sex.MALE == person.getSex()
    }

    def "setSex assigns a value to sex of a person"() {
        given:
        def address = Mock(Address)
        Person person = new Person ("Kim",22,address,Sex.MALE)

        when:
        person.setSex(Sex.FEMALE)

        then:
        Sex.FEMALE == person.sex
    }
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
