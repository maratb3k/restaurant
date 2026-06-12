package com.restaurant.booking.model.dto.request;

import lombok.*;

@Data
public class UserCreateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
