package com.restaurant.booking.controller;

import com.restaurant.booking.model.dto.request.FeedbacksByLocationRequest;
import com.restaurant.booking.model.dto.request.LocationCreateRequest;
import com.restaurant.booking.model.dto.request.LocationUpdateRequest;
import com.restaurant.booking.model.dto.response.ApiResponse;
import com.restaurant.booking.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@Slf4j
public class LocationController {

    private final LocationService locationService;


    @PostMapping
    public ResponseEntity<ApiResponse> createLocation(
            @RequestPart LocationCreateRequest body,
            @RequestPart MultipartFile image
    ) throws IOException {
        log.info("Creating location: {}", body);
        ApiResponse response = locationService.saveLocation(body, image);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<ApiResponse> getLocationById(@PathVariable String locationId) {
        log.info("Retrieving location with id: {}", locationId);
        return ResponseEntity
                .status(200)
                .body(locationService.getLocationById(locationId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getLocations() throws Exception {
        log.info("Retrieving locations");
        return ResponseEntity
                .status(200)
                .body(locationService.getLocations());
    }

    @GetMapping("/select-options")
    public ResponseEntity<ApiResponse> getLocationsForSelection() {
        log.info("Retrieving locations for selection");
        return ResponseEntity
                .status(200)
                .body(locationService.getLocationsForSelection());
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<ApiResponse> updateLocationById(
            @PathVariable String locationId,
            @RequestBody LocationUpdateRequest request
    ) {
        log.info("Getting location with id: {}", locationId);
        return ResponseEntity
                .status(200)
                .body(locationService.updateLocation(request));
    }

    @GetMapping("/{locationId}/speciality-dishes")
    public ResponseEntity<ApiResponse> getSpecialityDishes(@PathVariable String locationId) {
        log.info("Retrieving speciality dishes for location with id {}", locationId);
        return ResponseEntity
                .status(200)
                .body(locationService.getSpecialDishesByLocation(locationId));
    }

    @PostMapping("/{locationId}/feedbacks")
    public ResponseEntity<ApiResponse> getFeedbacksByLocation(
            @PathVariable String locationId,
            @RequestBody FeedbacksByLocationRequest body
    ) throws Exception {
        log.info("Retrieving feedbacks for location with id: {}", locationId);
        return ResponseEntity
                .status(200)
                .body(locationService.getFeedbacksByLocation(body, locationId));
    }

}
