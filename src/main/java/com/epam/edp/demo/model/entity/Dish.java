package com.epam.edp.demo.model.entity;

import com.epam.edp.demo.model.constant.DishStatus;
import com.epam.edp.demo.model.constant.DishType;
import lombok.*;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "dishes")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dish {

    @Id
    @Column(nullable = false, updatable = false)
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double weight;

    @Column(name = "image_url")
    private String imageKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "dish_status", nullable = false)
    private DishStatus dishStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "dish_type", nullable = false)
    private DishType dishType;

    private String calories;

    private String carbohydrates;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String fats;

    private String vitamins;

    @Column(name = "expected_ready_time")
    private String expectedReadyTime; // in minutes

}
