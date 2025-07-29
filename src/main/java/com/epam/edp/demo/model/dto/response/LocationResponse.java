package com.epam.edp.demo.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LocationResponse {
    private String id;
    private String address;
    private String description;
    private String totalCapacity;
    private String averageOccupancy;
    private String rating;
    private String imageUrl;
}
