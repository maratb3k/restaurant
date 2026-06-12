package com.restaurant.booking.controller;

import com.restaurant.booking.model.dto.request.ReservationPostponeRequest;
import com.restaurant.booking.model.dto.request.ReservationUpdateRequest;
import com.restaurant.booking.model.dto.response.ApiResponse;
import com.restaurant.booking.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.GenericResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final GenericResponseService responseBuilder;

    @GetMapping
    public ResponseEntity<ApiResponse> getReservation() {
        ApiResponse response = reservationService.getReservation();
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @DeleteMapping("/{reservationId}/cancel")
    public ResponseEntity<ApiResponse> cancelReservation(@PathVariable String reservationId) {
        ApiResponse response = reservationService.deleteReservation(reservationId);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<ApiResponse> updateReservation(@PathVariable String id,
                                                         @RequestBody ReservationUpdateRequest request){
        ApiResponse apiResponse = reservationService.editReservation(request, id);
        return ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse);
    }

    @PutMapping("/{id}/postpone")
    public ResponseEntity<ApiResponse> postponeReservation(@PathVariable String id,
                                                         @RequestBody ReservationPostponeRequest request){
        ApiResponse apiResponse = reservationService.postponeByWaiter(request, id);
        return ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse);
    }

}
