package com.restaurant.booking.exception;

public class EmptyRequiredFieldException extends RuntimeException {
    public EmptyRequiredFieldException(String message) {
        super(message);
    }
}
