package com.epam.edp.demo.repository.custom;

import com.epam.edp.demo.model.entity.Cart;
import com.epam.edp.demo.model.entity.Dish;

import java.util.List;
import java.util.Map;

public interface DishRepositoryCustom {
    List<Dish> getAvailableDishes(Map<String, Integer> dishCountMap, String dishType);
    List<Dish> getPopularDishes(List<Cart> carts, String sortType, String dishType);
    List<Dish> getDishesByTypeAndSort(String dishType, String sortBy, List<Cart> carts);
}
