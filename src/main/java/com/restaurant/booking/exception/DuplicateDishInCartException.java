package com.restaurant.booking.exception;

public class DuplicateDishInCartException extends RuntimeException {
    public DuplicateDishInCartException(String message) {
        super(message);
    }
}
