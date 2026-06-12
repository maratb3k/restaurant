package com.restaurant.booking.model.dto.request;

import lombok.*;

@Data
public class LocationCreateRequest {
    private String address;
    private String description;
    private Integer totalCapacity;
    private Integer averageOccupancy;
}
