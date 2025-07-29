package com.epam.edp.demo.service;

import com.epam.edp.demo.exception.EntityNotFountException;
import com.epam.edp.demo.mapper.DishMapper;
import com.epam.edp.demo.model.constant.DishType;
import com.epam.edp.demo.model.constant.SortBy;
import com.epam.edp.demo.model.dto.request.DishCreateRequest;
import com.epam.edp.demo.model.dto.request.DishUpdateRequest;
import com.epam.edp.demo.model.dto.response.ApiResponse;
import com.epam.edp.demo.model.dto.response.DishResponse;
import com.epam.edp.demo.model.entity.Dish;
import com.epam.edp.demo.model.entity.FoodOrder;
import com.epam.edp.demo.model.entity.Order;
import com.epam.edp.demo.repository.DishRepository;
import com.epam.edp.demo.repository.OrderRepository;
import com.epam.edp.demo.storage.MinioStorageService;
import com.epam.edp.demo.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;
    private final MinioStorageService minioStorageService;
    private final DishMapper dishMapper;
    private final OrderRepository orderRepository;


    public ApiResponse getDishesById(String dishId) {
        log.info("Fetching dish with id: {}", dishId);

        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new EntityNotFountException("Dish not found"));

        return ResponseBuilder.build(200, true, dishMapper.toResponse(dish));
    }

    public ApiResponse saveDish(DishCreateRequest request, MultipartFile image) throws IOException {
        byte[] bytes = image.getBytes();
        String imageKey = "dish:" + UUID.randomUUID() + ":" + LocalDateTime.now();
        minioStorageService.saveImage(imageKey, bytes, "image/png");

        Dish dish = dishMapper.toEntity(request);
        dish.setImageKey(imageKey);

        dishRepository.save(dish);

        return ResponseBuilder.build(201, true, "Dish created successfully with id: " + dish.getId());
    }

    public ApiResponse getDishesFiltered(DishType dishType, SortBy sort) {
        // fetching filtered dish by dish type
        List<Dish> filterDishByType = new ArrayList<>(dishRepository.findAllByDishType(dishType));

        if (sort == SortBy.PRICE_ASC || sort == SortBy.PRICE_DESC) {
            // filtering by price
            filterDishByType.sort(sort == SortBy.PRICE_ASC
                    ? Comparator.comparing(Dish::getPrice)
                    : Comparator.comparing(Dish::getPrice).reversed());
        } else {
            // filtering by popularity
            List<Order> allOrders = orderRepository.findAll();
            Map<String, Integer> popularityMap = new HashMap<>();

            for (Order order : allOrders) {
                for (FoodOrder foodOrder : order.getOrders()) {
                    popularityMap.merge(foodOrder.getDishId(), foodOrder.getQuantity(), Integer::sum);
                }
            }

            filterDishByType.sort((d1, d2) -> {
                int pop1 = popularityMap.getOrDefault(d1.getId(), 0);
                int pop2 = popularityMap.getOrDefault(d2.getId(), 0);
                return sort == SortBy.POPULARITY_ASC
                        ? Integer.compare(pop1, pop2)
                        : Integer.compare(pop2, pop1);
            });
        }
        return ResponseBuilder.build(200, true, dishMapper.toResponses(filterDishByType));
    }

    public ApiResponse updateDish(DishUpdateRequest request) {
        log.info("Updating dish with id: {}", request.getDishId());

        Dish dish = dishRepository.findById(request.getDishId())
                .orElseThrow(() -> new EntityNotFountException("Dish not found!"));

        Dish updatedDish = dishMapper.toUpdatedEntity(dish, request);

        dishRepository.save(updatedDish);

        return ResponseBuilder.build(200, true, "Dish updated successfully!");
    }

    public ApiResponse deleteDish(String dishId) {
        if (dishId == null || dishId.isBlank()) {
            return ResponseBuilder.build(400, false, "Dish ID cannot be null or empty");
        }

        Optional<Dish> optionalDish = dishRepository.findById(dishId);
        if (!optionalDish.isPresent()) {
            return ResponseBuilder.build(404, false, "Dish not found with id: " + dishId);
        }

        try {
            dishRepository.deleteById(dishId);
            return ResponseBuilder.build(200, true, "Dish deleted successfully");

        } catch (IllegalArgumentException e) {
            log.error("Error deleting dish: {}", e.getMessage(), e);
            return ResponseBuilder.build(500, false, "Internal server error while deleting dish");
        }
    }

    public ApiResponse getMostPopularDishes() {
        List<Dish> allDishes = dishRepository.findAll();
        List<Order> allOrders = orderRepository.findAll();
        Map<String, Integer> popularityMap = new HashMap<>();

        for (Order order : allOrders) {
            for (FoodOrder foodOrder : order.getOrders()) {
                popularityMap.merge(foodOrder.getDishId(), foodOrder.getQuantity(), Integer::sum);
            }
        }

        allDishes.sort((d1, d2) -> {
            int pop1 = popularityMap.getOrDefault(d1.getId(), 0);
            int pop2 = popularityMap.getOrDefault(d2.getId(), 0);
            return Integer.compare(pop1, pop2);
        });

        List<DishResponse> response = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            response.add(dishMapper.toResponse(allDishes.get(i)));
        }

        return ApiResponse.builder()
                .success(true)
                .statusCode(200)
                .data(response)
                .build();
    }
}