package com.restaurant.booking.model.dto.request;

import com.restaurant.booking.model.constant.FeedbackSortType;
import com.restaurant.booking.model.constant.FeedbackType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
public class FeedbacksByLocationRequest {

    @NotBlank(message = "Feedback type is required")
    private FeedbackType feedbackType;

    @NotBlank(message = "Feedback sort by is required")
    private FeedbackSortType sortBy;

    @Positive(message = "You cannot use negative number")
    private Integer page;

    @Positive(message = "You cannot use negative number")
    private Integer size;

    private Boolean ascending = true;

}
