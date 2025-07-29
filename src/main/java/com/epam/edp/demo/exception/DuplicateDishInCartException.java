package com.epam.edp.demo.exception;

public class DuplicateDishInCartException extends RuntimeException {
    public DuplicateDishInCartException(String message) {
        super(message);
    }
}
