package com.restaurant.booking.model.dto.request;

import lombok.Data;

@Data
public class ReservationPostponeRequest {
    private String date;
    private String timeFrom;
    private String timeTo;
}
