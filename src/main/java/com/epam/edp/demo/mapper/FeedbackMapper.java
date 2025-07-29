package com.epam.edp.demo.mapper;

import com.epam.edp.demo.model.dto.request.FeedbackCreateRequest;
import com.epam.edp.demo.model.entity.Feedback;
import com.epam.edp.demo.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FeedbackMapper {

    public Feedback toEntity(FeedbackCreateRequest request) {
        return Feedback.builder()
                .id(UUID.randomUUID().toString())
                .locationId(request.getLocationId())
                .reservationId(request.getReservationId())
                .cuisineComment(request.getCuisineComment())
                .cuisineRating(request.getCuisineRating())
                .serviceComment(request.getServiceComment())
                .serviceRating(request.getServiceRating())
                .feedbackGiverEmail(Objects.requireNonNull(Utils.getCurrentUser()).getEmail())
                .build();
    }

}
