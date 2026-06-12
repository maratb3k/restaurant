package com.restaurant.booking.model.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodOrder {
    private String dishId;
    private Integer quantity;
}