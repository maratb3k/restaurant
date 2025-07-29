package com.epam.edp.demo.mapper;

import com.epam.edp.demo.model.dto.response.DishResponse;
import com.epam.edp.demo.model.dto.request.DishCreateRequest;
import com.epam.edp.demo.model.dto.request.DishUpdateRequest;
import com.epam.edp.demo.model.entity.Dish;
import com.epam.edp.demo.storage.MinioStorageService;
import com.epam.edp.demo.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DishMapper {

    private final MinioStorageService minioStorageService;

    @Value("${preSignedUrlExpirationTimeHours}")
    private Long preSignedUrlExpirationTimeHours;

    public DishResponse toResponse(Dish entity) {
        return DishResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .dishType(entity.getDishType())
                .fats(entity.getFats())
                .price(entity.getPrice())
                .calories(entity.getCalories())
                .weight(entity.getWeight())
                .carbohydrates(entity.getCarbohydrates())
                .description(entity.getDescription())
                .vitamins(entity.getVitamins())
                .dishStatus(entity.getDishStatus())
                .imageUrl(entity.getImageKey() != null ?
                        minioStorageService.generatePreSignedUrl(entity.getImageKey(), preSignedUrlExpirationTimeHours) : "")
                .build();
    }

    public Dish toEntity(DishCreateRequest request) {
        return Dish.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .price(request.getPrice())
                .weight(request.getWeight())
                .dishStatus(request.getDishStatus())
                .dishType(request.getDishType())
                .calories(request.getCalories())
                .carbohydrates(request.getCarbohydrates())
                .description(request.getDescription())
                .fats(request.getFats())
                .vitamins(request.getVitamins())
                .build();
    }

    public List<DishResponse> toResponses(List<Dish> dishes) {
        return dishes.stream().map(this::toResponse).toList();
    }

    public Dish toUpdatedEntity(Dish dish, DishUpdateRequest request) {
        if (!Utils.isEmpty(request.getName())) dish.setName(request.getName());
        if (request.getPrice() != null) dish.setPrice(request.getPrice());
        if (request.getWeight() != null) dish.setWeight(request.getWeight());
        if (request.getDishStatus() != null) dish.setDishStatus(request.getDishStatus());
        if (request.getDishType() != null) dish.setDishType(request.getDishType());
        if (!Utils.isEmpty(request.getCalories())) dish.setCalories(request.getCalories());
        if (!Utils.isEmpty(request.getCarbohydrates())) dish.setCarbohydrates(request.getCarbohydrates());
        if (!Utils.isEmpty(request.getDescription())) dish.setDescription(request.getDescription());
        if (!Utils.isEmpty(request.getFats())) dish.setFats(request.getFats());
        if (!Utils.isEmpty(request.getVitamins())) dish.setVitamins(request.getVitamins());
        return dish;
    }
}
