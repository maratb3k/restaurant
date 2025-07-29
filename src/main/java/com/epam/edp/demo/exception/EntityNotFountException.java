package com.epam.edp.demo.exception;

public class EntityNotFountException extends RuntimeException{
    public EntityNotFountException(String message) {
        super(message);
    }
}
