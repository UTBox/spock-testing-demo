package com.synacy.gradprogram.spock;

import java.math.BigDecimal;

public class Sample {

    public BigDecimal calculateTotalWithTax(BigDecimal baseAmount,BigDecimal taxRate){
        BigDecimal tax = baseAmount.multiply(taxRate);
        BigDecimal total = baseAmount.add(tax);
        return total;
    }

    public BigDecimal calculateTotalWithTaxWithException(BigDecimal baseAmount,BigDecimal taxRate){

        if (baseAmount.compareTo(BigDecimal.valueOf(0)) != 1 || taxRate.compareTo(BigDecimal.valueOf(0)) != 1){
            throw new IllegalArgumentException();
        }
        else{
            BigDecimal tax = baseAmount.multiply(taxRate);
            BigDecimal total = baseAmount.add(tax);
            return total;
        }
    }

    public BigDecimal calculateTotalWithTaxWithCustomException(BigDecimal baseAmount,BigDecimal taxRate) throws ValueErrorException{

        if (baseAmount.compareTo(BigDecimal.valueOf(0)) != 1 || taxRate.compareTo(BigDecimal.valueOf(0)) != 1){
            throw new ValueErrorException("There is a negative value in your inputs");
        }
        else{
            BigDecimal tax = baseAmount.multiply(taxRate);
            BigDecimal total = baseAmount.add(tax);
            return total;
        }
    }
    static class ValueErrorException extends Exception{
        public ValueErrorException(String message){
            super (message);
        }
    }

    public BigDecimal calculateTotalWithTaxWithConvertingNegativeToPositive(BigDecimal baseAmount,BigDecimal taxRate){

        if (baseAmount.compareTo(BigDecimal.valueOf(0)) != 1 || taxRate.compareTo(BigDecimal.valueOf(0)) != 1){
            baseAmount = baseAmount.abs();
            taxRate =taxRate.abs();

            BigDecimal tax = baseAmount.multiply(taxRate);
            BigDecimal total = baseAmount.add(tax);
            return total;

        }
        else{
            BigDecimal tax = baseAmount.multiply(taxRate);
            BigDecimal total = baseAmount.add(tax);
            return total;
        }
    }

    public String reverseString (String word){

        StringBuilder newString = new StringBuilder();
        newString.append(word);
        String reverse = String.valueOf(newString.reverse());
        return reverse;
    }


}
