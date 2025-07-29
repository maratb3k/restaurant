package com.epam.edp.demo.model.dto.request;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    String base64encodedImage;
    String firstName;
    String lastName;
}
