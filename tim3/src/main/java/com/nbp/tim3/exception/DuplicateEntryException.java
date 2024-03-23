package com.nbp.tim3.exception;

public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(String errorMessage) {
        super(errorMessage);
    }
}