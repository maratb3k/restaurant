package com.restaurant.booking.service;

import com.amazonaws.SdkClientException;
import com.restaurant.booking.exception.ImageUploadException;
import com.restaurant.booking.exception.UserNotFoundException;
import com.restaurant.booking.model.dto.request.UpdateProfileRequest;
import com.restaurant.booking.model.dto.response.ProfileResponse;
import com.restaurant.booking.model.dto.response.ApiResponse;
import com.restaurant.booking.model.entity.AuthUser;
import com.restaurant.booking.repository.AuthUserRepository;
import com.restaurant.booking.storage.MinioStorageService;
import com.restaurant.booking.util.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final MinioStorageService minioStorageService;
    private final AuthUserRepository authUserRepository;

    @Value("${preSignedUrlExpirationTimeHours}")
    private Long preSignedUrlExpirationTimeHours;

    public ApiResponse getProfile() throws JsonProcessingException {
        try {
            AuthUser authUser = Utils.getCurrentUser();

            String imageUrl;

            if (authUser.getImageKey() == null || authUser.getImageKey().isEmpty()) {
                imageUrl = "";
            } else {
                try {
                    imageUrl = minioStorageService.generatePreSignedUrl(
                            authUser.getImageKey(),
                            preSignedUrlExpirationTimeHours
                    );
                } catch (SdkClientException e) {
                    log.error("Error generating pre-signed URL", e);
                    imageUrl = "";
                }
            }

            ProfileResponse profileResponse = ProfileResponse.builder()
                    .firstName(authUser.getFirstName())
                    .lastName(authUser.getLastName())
                    .imageUrl(imageUrl)
                    .build();

            return buildResponse(200, true, "", profileResponse);

        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            return buildResponse(404, false, e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildResponse(500, false, "Something went wrong, please try again later");
        }

    }

    public ApiResponse updateProfile(UpdateProfileRequest request) throws JsonProcessingException {
        try {
            AuthUser user = Utils.getCurrentUser();
            String email = user.getEmail();

            String encodedImage = request.getBase64encodedImage();
            String firstName = request.getFirstName();
            String lastName = request.getLastName();

            String NAME_REGEX = "^[A-Za-z\\-']{2,50}$";
            if (firstName == null || !firstName.matches(NAME_REGEX)) {
                return buildResponse(400, false, "First name must be 2-50 characters and only contain letters, hyphens, or apostrophes");
            }
            if (lastName == null || !lastName.matches(NAME_REGEX)) {
                return buildResponse(400, false, "Last name must be 2-50 characters and only contain letters, hyphens, or apostrophes");
            }

            byte[] imageBytes;
            String imageKey = email + ":" + LocalDateTime.now();

            if (encodedImage != null && !encodedImage.isEmpty()) {
                imageBytes = Base64.getDecoder().decode(encodedImage);
                try {
                    minioStorageService.saveImage(imageKey, imageBytes, "image/png");
                } catch (ImageUploadException e) {
                    return buildResponse(400, false, e.getMessage());
                }
            }
            try {
                authUserRepository.save(updateUserProfile(
                        user, imageKey , firstName, lastName
                ));
            } catch (IllegalArgumentException e) {
                log.error("User with email " + email + " not found");
                return buildResponse(400, false, e.getMessage());
            }

            return buildResponse(200, true, "Profile updated successfully");
        } catch (Exception e) {
            log.error("Unhandled exception: {}", e.getMessage());
            return buildResponse(400, false, "Something went wrong, please try again");
        }

    }

    private AuthUser updateUserProfile(AuthUser authUser, String imageKey, String firstName, String lastName) {
        if (imageKey != null && !imageKey.isEmpty()) {
            authUser.setImageKey(imageKey);
        }

        if (firstName != null && !firstName.isEmpty()) {
            authUser.setFirstName(firstName);
        }

        if (lastName != null && !lastName.isEmpty()) {
            authUser.setLastName(lastName);
        }
        return authUser;
    }

    private ApiResponse buildResponse(int statusCode, boolean success, String message) throws JsonProcessingException {
        return ApiResponse.<ProfileResponse>builder()
                .statusCode(statusCode)
                .success(success)
                .message(!message.isBlank() ? message: null)
                .build();
    }

    private ApiResponse buildResponse(int statusCode, boolean success, String message, ProfileResponse body) throws JsonProcessingException {
        return ApiResponse.<ProfileResponse>builder()
                .statusCode(statusCode)
                .success(success)
                .message(!message.isBlank() ? message: null)
                .data(body)
                .build();
    }
}
