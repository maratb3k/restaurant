package com.restaurant.booking.exception;

public class FieldDoesNotMeetTheRequirementsException extends RuntimeException {
    public FieldDoesNotMeetTheRequirementsException(String message) {
        super(message);
    }
}
