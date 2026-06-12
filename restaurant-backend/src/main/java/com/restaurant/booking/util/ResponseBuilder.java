package com.restaurant.booking.util;

import com.restaurant.booking.model.dto.response.ApiResponse;

public class ResponseBuilder {

    public static ApiResponse build(
            Integer statusCode,
            Boolean isSuccess,
            Object data
    ){
        return ApiResponse.builder()
                .statusCode(statusCode)
                .success(isSuccess)
                .data(data)
                .build();
    }

    public static ApiResponse build(
            Integer statusCode,
            Boolean isSuccess,
            String message
    ){
        return ApiResponse.builder()
                .statusCode(statusCode)
                .success(isSuccess)
                .message(message)
                .build();
    }
}
