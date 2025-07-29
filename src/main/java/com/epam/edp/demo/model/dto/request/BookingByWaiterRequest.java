package com.epam.edp.demo.model.dto.request;

import lombok.Data;

@Data
public class BookingByWaiterRequest {
    private String locationId;
    private String tableNumber;
    private String guestsNumber;
    private String date;
    private String timeFrom;
    private String timeTo;
    private String clientType;
    private String customerEmail;
}
