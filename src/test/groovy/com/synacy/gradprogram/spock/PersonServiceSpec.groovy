package com.synacy.gradprogram.spock

import spock.lang.Specification
import spock.lang.Subject

class PersonServiceSpec extends Specification {

    PersonRepository personRepository = Mock()
    EventPublisher eventPublisher = Mock()

    @Subject
    PersonService service = new PersonService(personRepository, eventPublisher)

    def "addPerson should call save"() {
        when:
        service.addPerson("Kenichi", 32, Mock(Address), Sex.MALE)

        then:
        1 * personRepository.save(_)
    }

    def "addPerson should call save on the correct person object"() {
        given:
        Address address = Mock()

        when:
        service.addPerson("Kenichi", 32, address, Sex.MALE)

        then:
        1 * personRepository.save(_) >> { Person passedPerson ->
            assert "Kenichi" == passedPerson.name
            assert 32 == passedPerson.age
            assert Sex.MALE == passedPerson.sex
            assert address == passedPerson.address
        }
    }

    def "deletePerson should delete the correct person."() {
        given:
        UUID id = UUID.randomUUID()
        Person person = Mock()

        personRepository.findById(id) >> person

        when:
        service.deletePerson(id)

        then:
        1 * personRepository.delete(person)
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
}
