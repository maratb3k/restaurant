package com.restaurant.booking.model.dto.request;

import lombok.Data;

@Data
public class BookingByClientRequest {
    private String locationId;
    private String tableNumber;
    private String guestsNumber;
    private String date;
    private String timeFrom;
    private String timeTo;
}
