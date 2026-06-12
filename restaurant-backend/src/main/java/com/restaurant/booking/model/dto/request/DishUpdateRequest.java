package com.restaurant.booking.model.dto.request;

import com.restaurant.booking.model.constant.DishStatus;
import com.restaurant.booking.model.constant.DishType;
import lombok.Data;

@Data
public class DishUpdateRequest {
    private String dishId;
    private String name;
    private Double price;
    private Double weight;
    private DishStatus dishStatus;
    private DishType dishType;
    private String calories;
    private String carbohydrates;
    private String description;
    private String fats;
    private String vitamins;
}
