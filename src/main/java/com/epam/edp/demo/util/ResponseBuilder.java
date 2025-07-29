package com.epam.edp.demo.util;

import com.epam.edp.demo.model.dto.response.ApiResponse;

public class ResponseBuilder {

    // for responses which sends back data
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

    // for void responses
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
