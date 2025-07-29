package com.epam.edp.demo.controller;

import com.epam.edp.demo.model.dto.request.ReservationPostponeRequest;
import com.epam.edp.demo.model.dto.request.ReservationUpdateRequest;
import com.epam.edp.demo.model.dto.response.ApiResponse;
import com.epam.edp.demo.service.ReservationService;
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

//    @PostMapping("/{reservationId}/order/{dishId}")
//    public ResponseEntity<ApiResponse> orderDish(
//            @PathVariable String reservationId,
//            @PathVariable String dishId
//    ) {
//        reservationService.
//    }

}
