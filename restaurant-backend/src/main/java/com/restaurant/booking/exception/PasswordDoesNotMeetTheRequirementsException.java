package com.restaurant.booking.exception;

public class PasswordDoesNotMeetTheRequirementsException extends RuntimeException{
    public PasswordDoesNotMeetTheRequirementsException(String message) {
        super(message);
    }
}
