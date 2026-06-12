package com.restaurant.booking.repository;

import com.restaurant.booking.model.constant.DishType;
import com.restaurant.booking.model.entity.Dish;
import com.restaurant.booking.repository.custom.DishRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface DishRepository extends JpaRepository<Dish, String>, DishRepositoryCustom {

    List<Dish> findAllByDishType(DishType dishType);

    List<Dish> findByIdIn(List<String> ids);
}
