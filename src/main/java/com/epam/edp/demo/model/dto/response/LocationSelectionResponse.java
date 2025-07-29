package com.epam.edp.demo.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LocationSelectionResponse {
    private String id;
    private String address;
}
