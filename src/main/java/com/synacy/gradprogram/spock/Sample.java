package com.synacy.gradprogram.spock;

public class Sample {
    public double calculateTotalWithTax(double baseAmount, double taxRate) throws BaseAmountException, TaxRateException {
        double taxAmount = baseAmount * taxRate;
        if (baseAmount <= 0) {
            throw new BaseAmountException("Base amount inputted is an invalid value. Please input a positive value.");
        } else if (taxRate < 0)  {
            throw new TaxRateException("Tax rate inputted is a negative value. Please input a positive value.");
        } else {
            return baseAmount + taxAmount;
        }
    }
    static class BaseAmountException extends Exception {
        public BaseAmountException(String message) {
            super(message);
        }
    }
    static class TaxRateException extends Exception {
        public TaxRateException(String message) {
            super(message);
        }
    }

    public static String reverseString(String message) {
        char ch[] = message.toCharArray();
        String reversedMessage = "";
        for (int i = ch.length; i > 0; i--) {
            reversedMessage += ch[i];
        }
        return reversedMessage;
    }
}
