package com.epam.edp.demo.exception;

public class FieldDoesNotMeetTheRequirementsException extends RuntimeException {
    public FieldDoesNotMeetTheRequirementsException(String message) {
        super(message);
    }
}
