package com.restaurant.booking.model.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private int statusCode;
    private boolean success;
    private String message;
    private Object data;
}
