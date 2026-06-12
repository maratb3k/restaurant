package com.restaurant.booking.model.dto.request;

import lombok.Data;

@Data
public class ReservationUpdateRequest {
    private String guestNumber;
    private String date;
    private String tableNumber;
    private String locationId;
    private String timeFrom;
    private String timeTo;
}
