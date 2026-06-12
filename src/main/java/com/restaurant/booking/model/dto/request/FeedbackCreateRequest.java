package com.restaurant.booking.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class FeedbackCreateRequest {

    @NotBlank(message = "Location ID must not be null or empty")
    private String locationId;

    @NotBlank(message = "Reservation ID must not be null or empty")
    private String reservationId;

    @NotBlank(message = "You should give comment to cuisine")
    private String cuisineComment;

    @NotNull(message = "You should give rating to cuisine")
    @Min(value = 1, message = "Cuisine rating must be at least 1")
    @Max(value = 5, message = "Cuisine rating cannot exceed 5")
    private Integer cuisineRating;

    @NotBlank(message = "You should give comment to service")
    private String serviceComment;

    @NotNull(message = "You should give rating to service")
    @Min(value = 1, message = "Service rating must be at least 1")
    @Max(value = 5, message = "Service rating cannot exceed 5")
    private Integer serviceRating;

}
