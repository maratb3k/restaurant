package com.epam.edp.demo.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProfileResponse {
    String firstName;
    String lastName;
    String imageUrl;
}
