package com.restaurant.booking.model.dto.request;

import lombok.*;

import java.util.List;

@Data
public class LocationUpdateRequest {
    private String locationId;
    private List<String> specialDishesId;
}
