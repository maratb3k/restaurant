package com.restaurant.booking.model.dto.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String email;
    private String password;
}
