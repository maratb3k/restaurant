package com.epam.edp.demo.controller;

import com.epam.edp.demo.model.dto.request.FeedbackCreateRequest;
import com.epam.edp.demo.model.dto.response.ApiResponse;
import com.epam.edp.demo.service.FeedbackService;
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
