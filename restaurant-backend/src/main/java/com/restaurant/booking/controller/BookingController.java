package com.restaurant.booking.controller;

import com.restaurant.booking.model.dto.request.BookingByClientRequest;
import com.restaurant.booking.model.dto.request.BookingByWaiterRequest;
import com.restaurant.booking.model.dto.response.ApiResponse;
import com.restaurant.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/client")
    public ResponseEntity<ApiResponse> bookTableByClient(@RequestBody BookingByClientRequest body) {
        ApiResponse response = bookingService.bookingTableByClient(body);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @PostMapping("/waiter")
    public ResponseEntity<ApiResponse> bookTableByWaiter(@RequestBody BookingByWaiterRequest body) {
        ApiResponse response = bookingService.bookingByWaiter(body);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @GetMapping("/tables")
    public ResponseEntity<ApiResponse> getTables(
            @RequestParam String locationId,
            @RequestParam String date,
            @RequestParam String time,
            @RequestParam Integer guests
    ) {
        ApiResponse response = bookingService.getTables(locationId, date, time, guests);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }


}
