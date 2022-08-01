package com.synacy.gradprogram.spock

import spock.lang.Specification


class SampleSpec extends Specification {

    def "calculateTotalWithTax should return total amount with added tax"() {

        given:
        def sample = new Sample()

        when:
        def amount = sample.calculateTotalWithTax(baseAmount, taxRate)

        then:
        expectedAmount == amount

        where:
        baseAmount|taxRate|| expectedAmount
        2000       |  0.3  || 2600
        3000       |  0.3  || 3900
        3000       |  0.4  || 4200
    }

    def "calculateTotalWithTax should throw an exception if a negative value of a baseAmount is passed"() {
        given:
        def sample = new Sample()

        when:
        def amount = sample.calculateTotalWithTax(-200, 0.1)

        then:
            thrown(Sample.BaseAmountException)
    }

    def "calculateTotalWithTax should throw an exception if a negative value of taxRate is passed"() {
        given:
        def sample = new Sample()

        when:
        def amount = sample.calculateTotalWithTax(200, -0.1)

        then:
            thrown(Sample.TaxRateException)
    }

    def "reverseString"() {
        given:
        def sample = new Sample()
        def expectedMessage = "miK"
        when:
        def reverseMessage = sample.reverseString("Kim")

        then:
        expectedMessage = reverseMessage
    }
}

