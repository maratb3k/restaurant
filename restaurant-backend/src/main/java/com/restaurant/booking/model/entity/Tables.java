package com.restaurant.booking.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tables", indexes = {
        @Index(name = "idx_location_id", columnList = "location_id")
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tables {

    @Id
    @Column(nullable = false, updatable = false)
    private String id = UUID.randomUUID().toString();

    @Column(name = "table_number", nullable = false)
    private Integer tableNumber;

    @Column(name = "table_capacity", nullable = false)
    private Integer tableCapacity;

    @Column(name = "location_id", nullable = false)
    private String locationId;

    @ElementCollection
    @CollectionTable(name = "table_time_slots", joinColumns = @JoinColumn(name = "table_id"))
    private List<TimeSlot> timeSlots = new ArrayList<>();
}
