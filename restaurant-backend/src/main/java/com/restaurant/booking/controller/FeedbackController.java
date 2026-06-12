package com.restaurant.booking.controller;

import com.restaurant.booking.model.dto.request.FeedbackCreateRequest;
import com.restaurant.booking.model.dto.response.ApiResponse;
import com.restaurant.booking.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<ApiResponse> createFeedback(@Valid @RequestBody FeedbackCreateRequest body) {
        ApiResponse response = feedbackService.createFeedback(body);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

}
