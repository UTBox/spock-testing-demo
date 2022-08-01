package com.synacy.gradprogram.spock

import spock.lang.Specification


class SampleSpec extends Specification {

    Sample sample = new Sample()

    def "calculateTotalWithTax should return the total amount based on the base amount and tax rate using given-expect"(){
        given:
        BigDecimal baseAmount = 1000;
        BigDecimal taxRate = 0.10;

        expect:
        assert 1100 == sample.calculateTotalWithTax(baseAmount,taxRate)
    }

    def "calculateTotalWithTax should return the total amount based on the base amount and tax rate using expect where"(){

        expect:
        assert total == sample.calculateTotalWithTax(baseAmount,taxRate)

        where:
        baseAmount | taxRate || total
        1000 | 0.10 || 1100
        2000 | 0.50 || 3000
        1500 | 0.30 || 1950
    }

    def "calculateTotalWithTaxWithException should throw exception"(){

        given:
        BigDecimal baseAmount = -1000;
        BigDecimal taxRate = 0.10;

        when:
        BigDecimal total = sample.calculateTotalWithTaxWithException(baseAmount, taxRate)


        then: // we want to know if the message was thrown
        thrown(IllegalArgumentException)

    }

    def "calculateTotalWithTaxWithCustomException should throw exception"(){

        given:
        BigDecimal baseAmount = -1000;
        BigDecimal taxRate = 0.10;

        when:
        BigDecimal total = sample.calculateTotalWithTaxWithCustomException(baseAmount, taxRate)


        then: // we want to know if the message was thrown
        thrown(Sample.ValueErrorException)

    }

    def "calculateTotalWithTaxWithConvertingNegativeToPositive will accept a negative value but still returns the total amount based on base amount and tax rate"(){

        given:
        BigDecimal baseAmount = -1000;
        BigDecimal taxRate = 0.10;

        when:
        BigDecimal total = sample.calculateTotalWithTaxWithConvertingNegativeToPositive(baseAmount, taxRate)

        then:
        1100 == total

    }

    def "reverseString should return a string in reverse order"(){
        given:
        String word = "kimjohnun"

        when:
        String reverse = sample.reverseString(word);

        then:
        "nunhojmik" == reverse

    }
}
