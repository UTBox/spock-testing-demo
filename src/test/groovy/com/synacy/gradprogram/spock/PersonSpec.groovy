package com.synacy.gradprogram.spock

import com.mechanitis.demo.spock.Colour
import spock.lang.Specification

class PersonSpec extends Specification {

    def "getName should return the name of the initialized person in class Person"() {
        given:
        //def phAddress = new Address("PH", "Cebu", "Escario")
        Address address = Mock()

        when:
        def person = new Person("Mark", 20, address, Sex.MALE)
        then:
        person.getName() == "Mark"
    }

    def "setName should update the name of the initialized person in class Person"() {
        given:
        Address address = Mock()

        when:
        def person = new Person("Jack", 20, address, Sex.MALE)
        person.setName("MrKrabs")

        then:
        person.getName() == "MrKrabs"
    }
    def "getAge should return the age of the initialized person in class Person() {
        given:
        Address address = Mock()

        when:
        def person = new Person("Mark", 22, address, Sex.MALE)

        then:
        person.getAge() == 22
    }

    def "setAge should update the age of the initialized person in class Person"() {
        given:
        Address address = Mock()

        when:
        def person = new Person("Jack", 20, address, Sex.MALE)
        person.setAge(23)

        then:
        person.getAge() == 23
    }

    def "getAddress should return the given address of an initialized person of Class Person"() {
        given:
        def phAddress = new Address("PH", "Cebu", "Escario")
        def wrongAddress = new Address("wrong", "place", "wrong time")

        when:
        def person = new Person("Mark", 20, phAddress, Sex.MALE)

        then:
//      person.getAddress() == wrongAddress //demonstrates if it were to be a wrong address
        person.getAddress() == phAddress
        }

    def "setAddress should update the Address parameter of an intialized person in Class Person"() {
        given:
        def phAddress = new Address("PH", "Cebu", "Escario")
        def wrongAddress = new Address("wrong", "place", "wrong time")

        when:
        def person = new Person("Mark", 20, phAddress, Sex.MALE)
        person.setAddress(wrongAddress)

        then:
        person.getAddress() == wrongAddress
    }

    def "getSex should return the sex of an initialized person of Class Person"() {
        given:
        Address address = Mock()

        when:
        def person = new Person("Martina", 20, address, Sex.FEMALE)

        then:
        person.getSex() == Sex.FEMALE

    }

    def "setSex should update the sex parameter of an initialized person of Class Person"() {
        given:
        Address address = Mock()
        def person = new Person("Jack", 20, address, Sex.MALE)

        when:
        person.setSex(Sex.FEMALE)

        then:
        person.getSex() == Sex.FEMALE
    }

    def "toString should demonstrate the given parameters initialized from Class Person"() {
        given:
        Address address = new Address("PH", "Cebu", "Escario")
        Person person = new Person("Jack", 20, address, Sex.MALE)

        when:
        def message = person.toString()

        then:
        message == "Name: " + person.name +
                ", Age: " + person.age +
                ", Address: (" + address.toString() + ")" +
                ", Sex: " + person.sex
    }

}