package com.synacy.gradprogram.spock

import spock.lang.Specification
import spock.lang.Subject

class PersonServiceSpec extends Specification {

    PersonRepository personRepository = Mock()
    EventPublisher eventPublisher = Mock()

    @Subject
    PersonService service = new PersonService(personRepository, eventPublisher)

    def "addPerson should save the new person."() {
        given:
        Address address = Mock()

        when:
        service.addPerson("Kenichi", 32, address, Sex.MALE)

        then:
        1 * personRepository.save(_) >> { Person passedPerson ->
            assert "Kenichi" == passedPerson.name
            assert 32 == passedPerson.age
            assert address == passedPerson.address
            assert Sex.MALE == passedPerson.sex
        }
    }

    def "updatePersonName should invoke save on the person with an updated name."() {
        given:
        UUID id = UUID.randomUUID()
        String newName = "John Wick"
        Person person = new Person("Kenichi", 14, Mock(Address), Sex.MALE)

        personRepository.findById(id) >> person

        when:
        service.updatePersonName(id, newName)

        then:
        1 * personRepository.save(_) >> { Person passedPerson ->
            assert newName == passedPerson.name

        }
    }

    def "updatePersonAge should invoke save on the person with an updated age"(){
        given:
        UUID id = UUID.randomUUID()
        int newAge = 25
        Person person = new Person("Fern", 24, Mock(Address), Sex.MALE)
        and:
        personRepository.findById(id) >> person

        when:
        service.updatePersonAge(id, newAge)

        then:
        1 * personRepository.save(_)
    }
}