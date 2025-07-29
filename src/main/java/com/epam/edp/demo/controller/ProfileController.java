package com.epam.edp.demo.controller;

import com.epam.edp.demo.model.dto.request.UpdatePasswordRequest;
import com.epam.edp.demo.model.dto.request.UpdateProfileRequest;
import com.epam.edp.demo.model.dto.response.ApiResponse;
import com.epam.edp.demo.service.AuthUserService;
import com.epam.edp.demo.service.ProfileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final AuthUserService authUserService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getProfile() throws JsonProcessingException {
        ApiResponse response = profileService.getProfile();
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse> updateProfile(@RequestBody UpdateProfileRequest body) throws JsonProcessingException {
        ApiResponse response = profileService.updateProfile(body);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @PutMapping("/profile/password")
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody UpdatePasswordRequest body) throws JsonProcessingException {
        ApiResponse response = authUserService.updatePassword(body);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

}
