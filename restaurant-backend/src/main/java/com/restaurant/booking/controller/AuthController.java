package com.restaurant.booking.controller;

import com.restaurant.booking.model.dto.request.UserCreateRequest;
import com.restaurant.booking.model.dto.request.UserLoginRequest;
import com.restaurant.booking.model.dto.response.ApiResponse;
import com.restaurant.booking.service.AuthUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse> register(@RequestBody UserCreateRequest body) throws JsonProcessingException {
        ApiResponse response = userService.register(body);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse> login(@RequestBody UserLoginRequest body) throws JsonProcessingException {
        ApiResponse response = userService.login(body);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

}
