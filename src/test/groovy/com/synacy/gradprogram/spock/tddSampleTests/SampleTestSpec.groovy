package com.synacy.gradprogram.spock.tddSampleTests

import com.synacy.gradprogram.spock.sampleTDD.SampleCalculateWithTax
import spock.lang.Specification

class SampleTestSpec extends Specification {
    def "calculateTotalWithTax() should return total amount with tax"() {
        given:
        SampleCalculateWithTax calculate = new SampleCalculateWithTax();
        double testBaseAmount = 90;
        double testRate = 0.60;

        when:
        double amountToPay = calculate.calculateTotalWithTax(testBaseAmount, testRate);

        then:
        144 as double == amountToPay
    }

    def "calculateTotalWithTax() should return total amount of tax based on the matrix"() {
        expect:
        new SampleCalculateWithTax().calculateTotalWithTax(testBaseAmount, testRate) == c;

        where:
        testBaseAmount | testRate || c
        90             | 0.60     || 144
        100            | 0.20     || 120
        345            | 0.34     || 462.3


    }

    def "reverseString() should return reversed string value"() {
        given:
        SampleCalculateWithTax calculate = new SampleCalculateWithTax();

        when:
        String result = calculate.reverseString("now");

        then:
        "won" == result;
    }

    def "calculateTotalWithTax() should handle IllegalArgumentException thrown"() {
        given:
        SampleCalculateWithTax calculate = new SampleCalculateWithTax();

        when:
        double amountToPay = calculate.calculateTotalWithTax(-1, 9)

        then:
        thrown(IllegalArgumentException);

    }
}
