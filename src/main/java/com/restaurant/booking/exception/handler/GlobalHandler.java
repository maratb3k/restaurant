package com.restaurant.booking.exception.handler;

import com.restaurant.booking.exception.*;
import com.restaurant.booking.model.dto.response.ApiResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException ex) {
        ApiResponse response = ApiResponse.builder()
                .statusCode(400)
                .success(false)
                .message("Invalid email or password")
                .build();

        return ResponseEntity.status(401).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("Invalid request");

        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .statusCode(400)
                .success(false)
                .message(message)
                .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleMissingBody(HttpMessageNotReadableException ex) {
        ApiResponse response = ApiResponse.builder()
                .statusCode(400)
                .success(false)
                .message("Required request body is missing")
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EntityNotFountException.class)
    public ResponseEntity<ApiResponse> handleEntityNotFound(EntityNotFountException ex) {

        ApiResponse response = ApiResponse.builder()
                .statusCode(404)
                .success(false)
                .message(ex.getMessage())
                .build();

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFound(UserNotFoundException ex) {
        ApiResponse response = ApiResponse.builder()
                .statusCode(404)
                .success(false)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @ExceptionHandler(EmptyRequiredFieldException.class)
    public ResponseEntity<ApiResponse> handleEmptyField(EmptyRequiredFieldException ex) {
        ApiResponse response = ApiResponse.builder()
                .statusCode(400)
                .success(false)
                .message(ex.getMessage())
                .build();

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(PasswordDoesNotMeetTheRequirementsException.class)
    public ResponseEntity<ApiResponse> handleWeakPassword(PasswordDoesNotMeetTheRequirementsException ex) {
        ApiResponse response = ApiResponse.builder()
                .statusCode(400)
                .success(false)
                .message(ex.getMessage())
                .build();

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(FieldDoesNotMeetTheRequirementsException.class)
    public ResponseEntity<ApiResponse> handleFieldDoesNotMeetTheRequirements(FieldDoesNotMeetTheRequirementsException ex) {
        ApiResponse response = ApiResponse.builder()
                .statusCode(400)
                .success(false)
                .message(ex.getMessage())
                .build();

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(DuplicateDishInCartException.class)
    public ResponseEntity<ApiResponse> handleDuplicateDishInCartException(DuplicateDishInCartException ex) {
        ApiResponse response = ApiResponse.builder()
                .statusCode(409)
                .success(false)
                .message(ex.getMessage())
                .build();

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleEnumMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Invalid or missing query parameter: " + ex.getName();
        ApiResponse response = ApiResponse.builder()
                .statusCode(400)
                .success(false)
                .message(message)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ApiResponse response = ApiResponse.builder()
                .statusCode(400)
                .success(false)
                .message("Bad input: " + ex.getMessage())
                .build();

        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleException(RuntimeException ex) {
        ex.printStackTrace();

        ApiResponse response = ApiResponse.builder()
                .statusCode(500)
                .success(false)
                .message(ex.getMessage())
                .build();

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

}
