package com.restaurant.booking.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FeedbackResponse {
    private String id;
    private String rate;
    private String comment;
    private String userName; // user's firstname
    private String date;
    private String locationId;
    private String userAvatarUrl;
}
