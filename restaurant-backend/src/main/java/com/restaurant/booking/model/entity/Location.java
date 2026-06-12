package com.restaurant.booking.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "locations")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @Column(nullable = false, updatable = false)
    private String id = UUID.randomUUID().toString();

    private String address;

    private String description;

    @Column(name = "total_capacity")
    private Integer totalCapacity;

    @Column(name = "average_occupancy")
    private Integer averageOccupancy; // on percentage

    @Column(name = "image_url")
    private String imageKey;

    private Integer rating;

    @ElementCollection
    @CollectionTable(name = "location_special_dishes", joinColumns = @JoinColumn(name = "location_id"))
    @Column(name = "dish_id")
    private List<String> specialDishesId;
}
