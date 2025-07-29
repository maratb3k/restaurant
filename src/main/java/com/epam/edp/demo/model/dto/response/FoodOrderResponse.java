package com.epam.edp.demo.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FoodOrderResponse {
    private String dishId;
    private Integer quantity;
    private String foodName;
    private Double totalPrice;
    private String expectedReadyTime;
}