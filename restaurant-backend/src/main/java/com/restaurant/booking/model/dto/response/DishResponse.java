package com.restaurant.booking.model.dto.response;

import com.restaurant.booking.model.constant.DishStatus;
import com.restaurant.booking.model.constant.DishType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DishResponse {
    private String id;
    private String name;
    private Double price;
    private Double weight;
    private String imageUrl;
    private DishStatus dishStatus;
    private DishType dishType;
    private String calories;
    private String carbohydrates;
    private String description;
    private String fats;
    private String vitamins;
}
