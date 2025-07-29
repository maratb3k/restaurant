package com.epam.edp.demo.model.dto.request;

import com.epam.edp.demo.model.constant.DishStatus;
import com.epam.edp.demo.model.constant.DishType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
