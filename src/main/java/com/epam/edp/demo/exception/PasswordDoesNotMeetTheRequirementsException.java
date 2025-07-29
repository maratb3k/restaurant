package com.epam.edp.demo.exception;

public class PasswordDoesNotMeetTheRequirementsException extends RuntimeException{
    public PasswordDoesNotMeetTheRequirementsException(String message) {
        super(message);
    }
}
