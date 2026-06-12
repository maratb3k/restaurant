package com.restaurant.booking.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationDto {
    private String id;
    private String feedbackId;
    private String guestNumber;
    private String date;
    private String locationAddress;
    private String preOrder;
    private String status;
    private String tableNumber;
    private String timeSlot;
    private String userInfo;
}
