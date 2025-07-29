package com.epam.edp.demo.model.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserLoginRequest {
    private String email;
    private String password;
}
