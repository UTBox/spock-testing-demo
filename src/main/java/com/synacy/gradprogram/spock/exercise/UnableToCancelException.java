package com.synacy.gradprogram.spock.exercise;

public class UnableToCancelException extends RuntimeException {
    public UnableToCancelException(String message) {
        super(message);
    }
}
