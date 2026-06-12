package com.restaurant.booking.controller;

import com.restaurant.booking.model.dto.request.OrderUpdateRequest;
import com.restaurant.booking.model.dto.response.ApiResponse;
import com.restaurant.booking.model.entity.AuthUser;
import com.restaurant.booking.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse> getSubmittedOrdersByUser(@AuthenticationPrincipal AuthUser currentUser) {
        ApiResponse response = orderService.getOrdersByUserId(currentUser.getId());
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @DeleteMapping("/{preOrderId}/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable String preOrderId) {
        ApiResponse response = orderService.cancelPreOrder(preOrderId);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<ApiResponse> editPreOrder(@RequestBody OrderUpdateRequest body) {
        ApiResponse response = orderService.editPreOrder(body);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

}
