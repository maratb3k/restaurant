package com.epam.edp.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "waiters", indexes = {
        @Index(name = "idx_waiter_email", columnList = "email"),
        @Index(name = "idx_waiter_location_id", columnList = "location_id")
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Waiter {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @ElementCollection
    @CollectionTable(name = "waiter_time_slots", joinColumns = @JoinColumn(name = "waiter_id"))
    @MapKeyColumn(name = "slot_key")
    @Column(name = "value")
    private Map<String, Integer> timeSlots = new HashMap<>();

    @Column(name = "location_id", nullable = false)
    private String locationId;
}
