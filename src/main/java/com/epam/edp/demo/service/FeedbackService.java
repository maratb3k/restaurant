package com.epam.edp.demo.service;

import com.epam.edp.demo.exception.EntityNotFountException;
import com.epam.edp.demo.mapper.FeedbackMapper;
import com.epam.edp.demo.model.constant.ReservationStatus;
import com.epam.edp.demo.model.dto.request.FeedbackCreateRequest;
import com.epam.edp.demo.model.dto.response.ApiResponse;
import com.epam.edp.demo.model.entity.Feedback;
import com.epam.edp.demo.model.entity.Location;
import com.epam.edp.demo.model.entity.Reservation;
import com.epam.edp.demo.repository.FeedbackRepository;
import com.epam.edp.demo.repository.LocationRepository;
import com.epam.edp.demo.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final LocationRepository locationRepository;
    private final ReservationRepository reservationRepository;

    public ApiResponse createFeedback(FeedbackCreateRequest request){
        try {
            log.info("Creating feedback for reservationId: {}", request.getReservationId());

            Location location = locationRepository.findById(request.getLocationId())
                    .orElseThrow(() -> new EntityNotFountException("Location not found"));

        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new EntityNotFountException("Reservation not found"));

            Feedback feedback = feedbackMapper.toEntity(request);
            Feedback savedFeedback = feedbackRepository.save(feedback);
        if (reservation.getStatus() == ReservationStatus.FINISHED) {
            return ApiResponse.builder()
                    .statusCode(400)
                    .success(false)
                    .message("Feedback already given!")
                    .build();
        }

        Feedback feedbackResponse = feedbackMapper.toEntity(request);
        feedbackRepository.save(feedbackResponse);

            // after feedback is given to reservation, reservation status changes to Finished
            reservation.setStatus(ReservationStatus.FINISHED);
            reservation.setFeedbackId(savedFeedback.getId());
            reservationRepository.save(reservation);

            return ApiResponse.builder()
                    .statusCode(201)
                    .success(true)
                    .message("Feedback created successfully!")
                    .build();

        } catch (Exception e) {
            log.error("Error saving feedback: {}", e.getMessage());
            return ApiResponse.builder()
                    .statusCode(500)
                    .success(false)
                    .message("Internal server error while saving feedback")
                    .build();
        }
    }
}
