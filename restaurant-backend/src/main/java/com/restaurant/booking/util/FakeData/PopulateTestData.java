package com.restaurant.booking.util.FakeData;

import com.restaurant.booking.model.entity.Dish;
import com.restaurant.booking.model.entity.Location;
import com.restaurant.booking.model.entity.Tables;
import com.restaurant.booking.model.entity.Waiter;
import com.restaurant.booking.repository.DishRepository;
import com.restaurant.booking.repository.LocationRepository;
import com.restaurant.booking.repository.TableRepository;
import com.restaurant.booking.repository.WaiterRepository;
import com.restaurant.booking.storage.MinioStorageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@DependsOn({"dishRepository", "locationRepository", "tableRepository", "waiterRepository"})
public class PopulateTestData {

    private final DishRepository dishRepository;
    private final LocationRepository locationRepository;
    private final TableRepository tableRepository;
    private final MinioStorageService minioStorageService;
    private final WaiterRepository waiterRepository;

    @PostConstruct
    public void trigger() throws IOException {
        log.info("Trying to populate with data");
        if (dishRepository.findAll().isEmpty())  populateDishTable();
        log.info("Dishes populated successfully...");
        if (locationRepository.findAll().isEmpty())  populateLocationTable();
        log.info("Locations populated successfully...");
    }

    private void populateLocationTable() throws IOException {
        log.info("{} locations being saved...", LocationData.locations.size());
        List<Location> locations = LocationData.locations;
        List<Dish> dishes = dishRepository.findAll();

        // fake emails for fake waiters
        List<String> emails = new ArrayList<>(WaitersEmails.emails);

        for (int i = 0; i < locations.size(); i++) {
            // set location image
            String locationImageName = i + "location.jpg";
            Location location = locations.get(i);
            location.setImageKey(setImagesToLocations(locationImageName));

            //set location speciality dishes
            location.setSpecialDishesId(setSpecialDishes(dishes));

            //create tables for location
            createTables(location);

            //create 4 waiters for each location
            createWaiter(location, emails);

            //save location
            locationRepository.save(location);
        }

        log.info("{} Locations saved successfully", locations.size());
    }

    private void createWaiter(Location location, List<String> waiterEmail) {
        List<Waiter> waiters = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            String email = waiterEmail.remove(0);
            waiters.add(
                    Waiter.builder()
                            .id(UUID.randomUUID().toString())
                            .email(email)
                            .locationId(location.getId())
                            .build()
            );
        }

        waiterRepository.saveAll(waiters);
    }

    private void createTables(Location location) {
        int totalCapacity = location.getTotalCapacity();
        List<Tables> tablesList = new ArrayList<>();
        int tableNumberCounter = 1;

        for (int i = 0; i < totalCapacity / 10; i++) {
            tablesList.add(Tables.builder()
                    .id(UUID.randomUUID().toString())
                    .tableNumber(tableNumberCounter++)
                    .tableCapacity(4)
                    .locationId(location.getId())
                    .timeSlots(new ArrayList<>())
                    .build());

            tablesList.add(Tables.builder()
                    .id(UUID.randomUUID().toString())
                    .tableNumber(tableNumberCounter++)
                    .tableCapacity(6)
                    .locationId(location.getId())
                    .timeSlots(new ArrayList<>())
                    .build());
        }

        tableRepository.saveAll(tablesList);
    }


    private List<String> setSpecialDishes(List<Dish> dishes) {
        List<String> specialDishesId = new ArrayList<>();
        while (specialDishesId.size() < 4) {
            int dishIdx = new Random().nextInt(10);
            String dishId = dishes.get(dishIdx).getId();
            if (!specialDishesId.contains(dishId)) {
                specialDishesId.add(dishId);
            }
        }
        return specialDishesId;
    }

    private void populateDishTable() throws IOException {
        log.info("{} dishes being saved...", DishData.dishes.size());
        List<Dish> dishes = DishData.dishes;

        for (int i = 0; i < dishes.size(); i++) {
            String dishImageName = i + "dish.jpg";

            Dish dish = dishes.get(i);
            dish.setImageKey(setImagesToDishes(dishImageName));
            dishRepository.save(dish);
        }

        log.info(DishData.dishes.size() + " dishes saved successfully");

    }

    private String setImagesToLocations(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/location-images/" + fileName);
        if (inputStream == null) {
            throw new RuntimeException("Image not found in resources: " + fileName);
        }

        byte[] bytes = inputStream.readAllBytes();
        String imageKey = "location:" + UUID.randomUUID() + ":" + LocalDateTime.now();
        minioStorageService.saveImage(imageKey, bytes, "image/png");
        return imageKey;
    }

    private String setImagesToDishes(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/dish-images/" + fileName);
        if (inputStream == null) {
            throw new RuntimeException("Image not found in resources: " + fileName);
        }

        byte[] bytes = inputStream.readAllBytes();
        String imageKey = "dish:" + UUID.randomUUID() + ":" + LocalDateTime.now();
        minioStorageService.saveImage(imageKey, bytes, "image/png");
        return imageKey;
    }

}
