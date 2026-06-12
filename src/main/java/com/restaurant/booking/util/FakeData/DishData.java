package com.restaurant.booking.util.FakeData;


import com.restaurant.booking.model.constant.DishStatus;
import com.restaurant.booking.model.constant.DishType;
import com.restaurant.booking.model.entity.Dish;

import java.util.List;
import java.util.UUID;

public class DishData {

    public static List<Dish> dishes = List.of(

            Dish.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Chocolate Lava Cake")
                    .price(9.0)
                    .weight(180.0)
                    .dishStatus(DishStatus.AVAILABLE)
                    .dishType(DishType.MAIN)
                    .calories("500 kcal")
                    .carbohydrates("45g")
                    .description("Warm chocolate cake with a molten chocolate center.")
                    .fats("28g")
                    .vitamins("Iron, magnesium")
                    .build(),

            Dish.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Spring Salad")
                    .price(11.9)
                    .weight(400.0)
                    .dishStatus(DishStatus.AVAILABLE)
                    .dishType(DishType.MAIN)
                    .calories("300 kcal")
                    .carbohydrates("30-35g")
                    .description("Fresh salad with seasonal vegetables and a light vinaigrette.")
                    .fats("20-25g")
                    .vitamins("Vitamin C, Vitamin A, calcium, and iron")
                    .build(),

            Dish.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Grilled Chicken")
                    .price(15.5)
                    .weight(500.0)
                    .dishStatus(DishStatus.AVAILABLE)
                    .dishType(DishType.MAIN)
                    .calories("450 kcal")
                    .carbohydrates("10-12g")
                    .description("Tender grilled chicken breast served with roasted vegetables.")
                    .fats("15g")
                    .vitamins("Vitamin B6, B12, and niacin")
                    .build(),

            Dish.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Pumpkin Soup")
                    .price(8.0)
                    .weight(300.0)
                    .dishStatus(DishStatus.AVAILABLE)
                    .dishType(DishType.APPETIZERS)
                    .calories("180 kcal")
                    .carbohydrates("20g")
                    .description("Creamy pumpkin soup with a hint of nutmeg.")
                    .fats("10g")
                    .vitamins("Vitamin A, Vitamin C")
                    .build(),

            Dish.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Beef Steak")
                    .price(22.0)
                    .weight(550.0)
                    .dishStatus(DishStatus.AVAILABLE)
                    .dishType(DishType.MAIN)
                    .calories("650 kcal")
                    .carbohydrates("5g")
                    .description("Juicy grilled steak with a peppercorn sauce.")
                    .fats("30g")
                    .vitamins("Iron, Vitamin B12")
                    .build(),

            Dish.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Fruit Bowl")
                    .price(7.5)
                    .weight(250.0)
                    .dishStatus(DishStatus.AVAILABLE)
                    .dishType(DishType.DESSERTS)
                    .calories("200 kcal")
                    .carbohydrates("35g")
                    .description("A mix of fresh seasonal fruits.")
                    .fats("2g")
                    .vitamins("Vitamin C, potassium")
                    .build(),

            Dish.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Spaghetti Carbonara")
                    .price(13.0)
                    .weight(450.0)
                    .dishStatus(DishStatus.AVAILABLE)
                    .dishType(DishType.MAIN)
                    .calories("550 kcal")
                    .carbohydrates("50g")
                    .description("Classic Italian pasta with creamy egg and pancetta sauce.")
                    .fats("25g")
                    .vitamins("Vitamin D, calcium")
                    .build(),

            Dish.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Greek Yogurt Parfait")
                    .price(6.5)
                    .weight(200.0)
                    .dishStatus(DishStatus.AVAILABLE)
                    .dishType(DishType.DESSERTS)
                    .calories("150 kcal")
                    .carbohydrates("18g")
                    .description("Layered Greek yogurt with honey, granola, and berries.")
                    .fats("5g")
                    .vitamins("Vitamin C, calcium")
                    .build(),

            Dish.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Caesar Salad")
                    .price(10.0)
                    .weight(350.0)
                    .dishStatus(DishStatus.AVAILABLE)
                    .dishType(DishType.APPETIZERS)
                    .calories("320 kcal")
                    .carbohydrates("22g")
                    .description("Romaine lettuce with Caesar dressing, croutons, and parmesan.")
                    .fats("18g")
                    .vitamins("Vitamin K, calcium")
                    .build(),

            Dish.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Mushroom Risotto")
                    .price(14.0)
                    .weight(400.0)
                    .dishStatus(DishStatus.AVAILABLE)
                    .dishType(DishType.MAIN)
                    .calories("470 kcal")
                    .carbohydrates("42g")
                    .description("Creamy risotto with wild mushrooms and herbs.")
                    .fats("20g")
                    .vitamins("Vitamin D, iron")
                    .build()
    );

}
