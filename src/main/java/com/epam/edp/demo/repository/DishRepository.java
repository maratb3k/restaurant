package com.epam.edp.demo.repository;

import com.epam.edp.demo.model.constant.DishType;
import com.epam.edp.demo.model.entity.Dish;
import com.epam.edp.demo.repository.custom.DishRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface DishRepository extends JpaRepository<Dish, String>, DishRepositoryCustom {

    List<Dish> findAllByDishType(DishType dishType);

    List<Dish> findByIdIn(List<String> ids);
}
