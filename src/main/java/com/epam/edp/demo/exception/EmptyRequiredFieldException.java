package com.epam.edp.demo.exception;

public class EmptyRequiredFieldException extends RuntimeException {
    public EmptyRequiredFieldException(String message) {
        super(message);
    }
}
