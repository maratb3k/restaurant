package com.epam.edp.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cart", indexes = {
        @Index(name = "idx_cart_reservation_id", columnList = "reservation_id"),
        @Index(name = "idx_cart_user_id", columnList = "user_id")
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @Column(nullable = false, updatable = false)
    private String id = UUID.randomUUID().toString();

    @Column(name = "reservation_id", nullable = false)
    private String reservationId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @ElementCollection
    @CollectionTable(name = "cart_orders", joinColumns = @JoinColumn(name = "cart_id"))
    private List<FoodOrder> orders = new ArrayList<>();

    public Cart(String userId, String reservationId) {
        this.userId = userId;
        this.reservationId = reservationId;
    }

    public void clear() {
        orders = new ArrayList<>();
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }
}
