package com.epam.edp.demo.util.FakeData;

import com.epam.edp.demo.model.entity.Location;

import java.util.List;
import java.util.UUID;

public class LocationData {

    public static List<Location> locations = List.of(
            Location.builder()
                    .id(UUID.randomUUID().toString())
                    .address("12 Freedom Square")
                    .description("In the heart of the city, this branch blends traditional charm with modern elegance. \n" +
                            "Specializing in fusion cuisine and artisanal desserts. \n" +
                            "Perfect for business meetings and weekend brunches.")
                    .totalCapacity(40)
                    .averageOccupancy(70)
                    .rating(4)
                    .build(),

            Location.builder()
                    .id(UUID.randomUUID().toString())
                    .address("99 Chavchavadze Avenue")
                    .description("Situated near the university district, this spot is known for quick bites and a vibrant coffee bar. \n" +
                            "The menu includes street food-inspired dishes, smoothies, and craft teas.")
                    .totalCapacity(30)
                    .averageOccupancy(50)
                    .rating(4)
                    .build(),

            Location.builder()
                    .id(UUID.randomUUID().toString())
                    .address("18 Agmashenebeli Avenue")
                    .description("Classic Georgian interiors and an open kitchen create a welcoming vibe. \n" +
                            "Known for khinkali variations and wine pairings. \n" +
                            "Popular with tourists and foodies alike.")
                    .totalCapacity(60)
                    .averageOccupancy(85)
                    .rating(5)
                    .build(),

            Location.builder()
                    .id(UUID.randomUUID().toString())
                    .address("22 Vake Park Road")
                    .description("Nestled beside Vake Park, this location offers a relaxed garden setting. \n" +
                            "Focus on organic dishes and handmade pastries. \n" +
                            "Great for families and weekend retreats.")
                    .totalCapacity(30)
                    .averageOccupancy(65)
                    .rating(5)
                    .build(),

            Location.builder()
                    .id(UUID.randomUUID().toString())
                    .address("7 Liberty Tower Street")
                    .description("Located inside a modern office complex, ideal for busy professionals. \n" +
                            "Offers daily specials and a rotating soup menu. \n" +
                            "Minimalist decor and fast service.")
                    .totalCapacity(50)
                    .averageOccupancy(90)
                    .rating(4)
                    .build(),

            Location.builder()
                    .id(UUID.randomUUID().toString())
                    .address("3 Mtatsminda Ridge")
                    .description("A hidden gem with panoramic city views. \n" +
                            "Romantic setting perfect for sunset dinners. \n" +
                            "Features slow-cooked regional dishes and exclusive wines.")
                    .totalCapacity(20)
                    .averageOccupancy(40)
                    .rating(5)
                    .build(),

            Location.builder()
                    .id(UUID.randomUUID().toString())
                    .address("61 Old Tbilisi Lane")
                    .description("Located in the historic part of town, surrounded by cobblestone streets. \n" +
                            "Rustic feel with a modern kitchen twist. \n" +
                            "Famous for breakfast platters and locally roasted coffee.")
                    .totalCapacity(30)
                    .averageOccupancy(55)
                    .rating(4)
                    .build(),

            Location.builder()
                    .id(UUID.randomUUID().toString())
                    .address("5 Tech Valley Road")
                    .description("Tech-inspired décor and smart table ordering. \n" +
                            "Serves high-protein, health-conscious meals. \n" +
                            "Popular with digital nomads and students.")
                    .totalCapacity(50)
                    .averageOccupancy(75)
                    .rating(5)
                    .build(),

            Location.builder()
                    .id(UUID.randomUUID().toString())
                    .address("35 Seaside Promenade")
                    .description("Coastal branch offering seafood delights and tropical drinks. \n" +
                            "Outdoor seating with sea breeze and music evenings. \n" +
                            "Kid-friendly menu and weekend events.")
                    .totalCapacity(60)
                    .averageOccupancy(80)
                    .rating(5)
                    .build(),

            Location.builder()
                    .id(UUID.randomUUID().toString())
                    .address("14 Art House Boulevard")
                    .description("Attached to an art gallery, combining culture and cuisine. \n" +
                            "Seasonal tasting menus, gallery brunches, and art-inspired cocktails. \n" +
                            "Intimate, creative, and eclectic.")
                    .totalCapacity(30)
                    .averageOccupancy(45)
                    .rating(4)
                    .build()
    );

}
