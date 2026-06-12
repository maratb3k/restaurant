package com.restaurant.booking.model.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TableDto {
    private String tableNumber;
    private String capacity;
    private String locationId;
    private String locationAddress;
    private String date;
    private List<String> availableSlots;
}
