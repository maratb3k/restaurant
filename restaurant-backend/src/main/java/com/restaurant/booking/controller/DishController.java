package com.restaurant.booking.controller;

import com.restaurant.booking.model.constant.DishType;
import com.restaurant.booking.model.constant.SortBy;
import com.restaurant.booking.model.dto.request.DishCreateRequest;
import com.restaurant.booking.model.dto.request.DishUpdateRequest;
import com.restaurant.booking.model.dto.response.ApiResponse;
import com.restaurant.booking.service.DishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @PostMapping
    public ResponseEntity<ApiResponse> createDish(
            @RequestPart DishCreateRequest body,
            @RequestPart MultipartFile image
    ) throws IOException {
        return ResponseEntity
                .status(201)
                .body(dishService.saveDish(body, image));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getDishes(
            @RequestParam(name = "dishType") DishType dishType,
            @RequestParam(name = "sort") SortBy sort
    ) {
        return ResponseEntity
                .status(200)
                .body(dishService.getDishesFiltered(dishType, sort));
    }

    @GetMapping("/{dishId}")
    public ResponseEntity<ApiResponse> getDishById(@PathVariable String dishId) {
        return ResponseEntity
                .status(200)
                .body(dishService.getDishesById(dishId));
    }

    @DeleteMapping("/{dishId}")
    public ResponseEntity<ApiResponse> deleteDish(@PathVariable String dishId) {
        return ResponseEntity
                .status(200)
                .body(dishService.deleteDish(dishId));
    }

    @PutMapping("/{dishId}")
    public ResponseEntity<ApiResponse> updateDish(@RequestBody DishUpdateRequest body) {
        return ResponseEntity
                .status(200)
                .body(dishService.updateDish(body));
    }

    @GetMapping("/most-popular")
    public ResponseEntity<ApiResponse> getMostPopularDishes() {
        ApiResponse apiResponse = dishService.getMostPopularDishes();
        return ResponseEntity
                .status(apiResponse.getStatusCode())
                .body(apiResponse);
    }


}
