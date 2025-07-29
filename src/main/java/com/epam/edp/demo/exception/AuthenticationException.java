package com.epam.edp.demo.exception;

/*
Exception for handling authentication errors with Cognito
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
