package com.restaurant.booking.service;

import com.restaurant.booking.exception.*;
import com.restaurant.booking.exception.*;
import com.restaurant.booking.model.constant.UserRole;
import com.restaurant.booking.model.dto.request.UpdatePasswordRequest;
import com.restaurant.booking.model.dto.request.UserCreateRequest;
import com.restaurant.booking.model.dto.request.UserLoginRequest;
import com.restaurant.booking.model.dto.response.ApiResponse;
import com.restaurant.booking.model.entity.AuthUser;
import com.restaurant.booking.repository.AuthUserRepository;
import com.restaurant.booking.repository.WaiterRepository;
import com.restaurant.booking.security.JwtService;
import com.restaurant.booking.util.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final WaiterRepository waiterRepository;
    private final AuthUserRepository authUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 16;
    private final PasswordEncoder passwordEncoder;


    public ApiResponse register(UserCreateRequest request) throws JsonProcessingException {
        log.info("User registering...");

        ApiResponse validationResponse = validate(request);
        if (validationResponse != null) {
            return validationResponse;
        }

        Optional<AuthUser> existingUser = authUserRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return buildResponse(400, false, "User already exists");
        }

        if (waiterRepository.existsByEmail(request.getEmail())) {
            log.info("Email is registered as waiter, assigning waiter role");

            authUserRepository.save(
                    new AuthUser(
                            UUID.randomUUID().toString(),
                            request.getFirstName(),
                            request.getLastName(),
                            request.getEmail(),
                            passwordEncoder.encode(request.getPassword()),
                            null,
                            UserRole.WAITER,
                            false
                    )
            );

        } else {
            AuthUser authUser = new AuthUser(
                    UUID.randomUUID().toString(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    passwordEncoder.encode(request.getPassword()),
                    null,
                    UserRole.CUSTOMER,
                    false
            );
            authUserRepository.save(
                    authUser
            );
        }
        log.info("Sign up successful for user: {}", request.getEmail());
        return buildResponse(200, true, "User registered successfully");
    }

    public ApiResponse login(UserLoginRequest request) throws JsonProcessingException {
        if (request == null) {
            return buildResponse(400, false, "Empty body of the request is not allowed", null);
        }

        String email = request.getEmail();
        String password = request.getPassword();

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            log.info("Invalid email or password");
            return buildResponse(400, false, "Invalid email or password", null);
        }

        log.info("Trying to sign in user: {}", email);

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            AuthUser authenticatedUser = authUserRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFountException("User not found"));
            String accessToken = jwtService.generateToken(authenticatedUser);
            log.info("Successfully signed in user: {}", email);
            return buildResponse(200, true, "Successfully logged in", Map.of("accessToken", accessToken));

        } catch (BadCredentialsException ex) {
            log.warn("Login failed: bad credentials for {}", email);
            return buildResponse(400, false, "Invalid email or password", null);
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            return buildResponse(401, false, e.getMessage(), null);
        } catch (Exception ex) {
            log.error("Unexpected error during login", ex);
            return buildResponse(500, false, "Internal server error", null);
        }
    }

    public ApiResponse updatePassword(UpdatePasswordRequest body) throws JsonProcessingException {
        AuthUser user = Utils.getCurrentUser();

        if (body == null) {
            return buildResponse(400, false, "Empty body of the request is not allowed");
        }

        String oldPassword = body.getOldPassword();
        String newPassword = body.getNewPassword();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return buildResponse(400, false, "Old password invalid");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return buildResponse(400, false, "New password must be different from the old password");
        }

        checkPasswordRequirements(newPassword);

        user.setPassword(passwordEncoder.encode(newPassword));
        authUserRepository.save(user);
        return buildResponse(200, true, "Password has been successfully updated");
    }

    private ApiResponse validate(UserCreateRequest request) throws JsonProcessingException {
        try {
            Map<String, Object> fields = new HashMap<>();
            fields.put("First name", request.getFirstName());
            fields.put("Last name", request.getLastName());
            fields.put("email", request.getEmail());
            fields.put("Password", request.getPassword());
            nullChecker(fields);
        } catch (EmptyRequiredFieldException e) {
            log.debug("Empty required fields for sign up. First name: {}, Last name: {}, Email: {}",
                    request.getFirstName(), request.getLastName(), request.getEmail());

            return buildResponse(400, false, e.getMessage());
        }

        try {
            checkFieldRequirements(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword()
            );
        } catch (FieldDoesNotMeetTheRequirementsException | PasswordDoesNotMeetTheRequirementsException e) {
            return buildResponse(400, false, e.getMessage());
        }
        return null;
    }

    private void checkFieldRequirements(
            String firstName,
            String lastName,
            String email,
            String password
    ) throws PasswordDoesNotMeetTheRequirementsException, FieldDoesNotMeetTheRequirementsException {
        String NAME_REGEX = "^[A-Za-z\\-']{1,50}$";
        if (!firstName.matches(NAME_REGEX)) {
            throw new FieldDoesNotMeetTheRequirementsException("First name must be up to 50 characters. Only Latin letters, hyphens, and apostrophes are allowed.");
        }

        if (!lastName.matches(NAME_REGEX)) {
            log.debug("Invalid last name. Last name: {}", lastName);
            throw new FieldDoesNotMeetTheRequirementsException("Last name must be up to 50 characters. Only Latin letters, hyphens, and apostrophes are allowed.");
        }

        checkPasswordRequirements(password);

        String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(EMAIL_REGEX)) {
            log.warn("Provided email: {} does not match the required email address", email);
            throw new FieldDoesNotMeetTheRequirementsException("Invalid email address. Please ensure it follows the format: username@domain.com");
        }
    }

    private void checkPasswordRequirements(String password) throws PasswordDoesNotMeetTheRequirementsException {
        if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH)
            throw new PasswordDoesNotMeetTheRequirementsException(
                    "Password must be " + PASSWORD_MIN_LENGTH + "-" + PASSWORD_MAX_LENGTH + " characters long"
            );
        if (password.equals(password.toLowerCase()))
            throw new PasswordDoesNotMeetTheRequirementsException("At least one uppercase letter required");
        if (password.equals(password.toUpperCase()))
            throw new PasswordDoesNotMeetTheRequirementsException("At least one lowercase letter required");
        String SPECIAL_CHARACTER_REGEX = ".*[^a-zA-Z0-9].*";
        if (!password.matches(SPECIAL_CHARACTER_REGEX))
            throw new PasswordDoesNotMeetTheRequirementsException("At least one special character required");
        String NUMBER_REGEX = ".*[0-9].*";
        if (!password.matches(NUMBER_REGEX))
            throw new PasswordDoesNotMeetTheRequirementsException("At least one number required");
    }

    private void nullChecker(Map<String, Object> fields) throws EmptyRequiredFieldException {
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();

            if (fieldValue == null) {
                throw new EmptyRequiredFieldException("Required field '" + fieldName + "' is missing");
            }

            if (fieldValue instanceof String) {
                String stringValue = (String) fieldValue;
                if (stringValue.isEmpty()) {
                    throw new EmptyRequiredFieldException("Required field '" + fieldName + "' cannot be empty");
                }
            }
        }
    }

    private ApiResponse buildResponse(int statusCode, boolean success, String message) throws JsonProcessingException {
        return ApiResponse.<Void>builder()
                .statusCode(statusCode)
                .success(success)
                .message(!message.isBlank() ? message: null)
                .build();
    }

    private ApiResponse buildResponse(int statusCode, boolean success, String message, Object body) throws JsonProcessingException {
        return ApiResponse.<Object>builder()
                .statusCode(statusCode)
                .success(success)
                .message(!message.isBlank() ? message: null)
                .data(body)
                .build();
    }
}
