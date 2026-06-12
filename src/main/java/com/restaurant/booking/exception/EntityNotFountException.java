package com.restaurant.booking.exception;

public class EntityNotFountException extends RuntimeException{
    public EntityNotFountException(String message) {
        super(message);
    }
}
