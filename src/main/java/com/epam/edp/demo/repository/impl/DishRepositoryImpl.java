package com.epam.edp.demo.repository.impl;

import com.epam.edp.demo.model.constant.DishStatus;
import com.epam.edp.demo.model.constant.DishType;
import com.epam.edp.demo.model.entity.Cart;
import com.epam.edp.demo.model.entity.Dish;
import com.epam.edp.demo.model.entity.FoodOrder;
import com.epam.edp.demo.repository.custom.DishRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class DishRepositoryImpl implements DishRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Dish> getAvailableDishes(Map<String, Integer> dishCountMap, String dishType) {
        if (dishCountMap.isEmpty()) return Collections.emptyList();

        TypedQuery<Dish> query = entityManager.createQuery(
                "SELECT d FROM Dish d WHERE d.id IN :ids", Dish.class
        );
        query.setParameter("ids", dishCountMap.keySet());

        List<Dish> allDishes = query.getResultList();

        return allDishes.stream()
                .filter(dish -> dish.getDishStatus() == DishStatus.AVAILABLE &&
                        (dishType == null || dishType.isEmpty() || dish.getDishType().name().equalsIgnoreCase(dishType)))
                .toList();
    }

    @Override
    public List<Dish> getPopularDishes(List<Cart> carts, String sortType, String dishType) {
        Map<String, Integer> dishCountMap = new HashMap<>();

        for (Cart cart : carts) {
            List<FoodOrder> orders = Optional.ofNullable(cart.getOrders()).orElse(List.of());
            for (FoodOrder order : orders) {
                dishCountMap.merge(order.getDishId(), order.getQuantity(), Integer::sum);
            }
        }

        if (dishCountMap.isEmpty()) {
            return entityManager.createQuery("SELECT d FROM Dish d", Dish.class).getResultList();
        }

        List<Dish> allDishes = getAvailableDishes(dishCountMap, dishType);
        return sortByPopularity(allDishes, dishCountMap, sortType);
    }

    @Override
    public List<Dish> getDishesByTypeAndSort(String dishType, String sortBy, List<Cart> carts) {
        String[] parts = sortBy.split(",");
        String field = parts[0].trim().toLowerCase();
        String order = parts.length > 1 ? parts[1].trim().toLowerCase() : "asc";

        if ("price".equals(field)) {
            TypedQuery<Dish> query = entityManager.createQuery(
                    "SELECT d FROM Dish d WHERE d.dishType = :dishType", Dish.class
            );
            query.setParameter("dishType", DishType.valueOf(dishType.toUpperCase()));
            List<Dish> dishes = query.getResultList();

            dishes.sort(order.equals("asc")
                    ? Comparator.comparing(Dish::getPrice)
                    : Comparator.comparing(Dish::getPrice).reversed());

            return dishes;
        } else {
            return getPopularDishes(carts, order, dishType);
        }
    }

    private List<Dish> sortByPopularity(List<Dish> dishes, Map<String, Integer> dishCountMap, String sortType) {
        Comparator<Dish> comparator = Comparator.comparingInt(
                d -> dishCountMap.getOrDefault(d.getId(), 0)
        );
        if ("desc".equalsIgnoreCase(sortType)) {
            comparator = comparator.reversed();
        }

        return dishes.stream().sorted(comparator).toList();
    }
}
