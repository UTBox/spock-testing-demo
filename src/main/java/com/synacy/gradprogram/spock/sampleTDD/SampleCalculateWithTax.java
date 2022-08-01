package com.synacy.gradprogram.spock.sampleTDD;

public class SampleCalculateWithTax {
    public double calculateTotalWithTax(double baseAmount, double rate) {
        if (baseAmount < 0 || rate < 0) {
            throw new IllegalArgumentException();
        }
        return (baseAmount + (baseAmount * rate));

    }

    public String reverseString(String stringInput) {

        StringBuilder sb=new StringBuilder(stringInput);
        sb.reverse();
        return sb.toString();

    }
}
