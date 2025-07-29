package com.epam.edp.demo.model.entity;

import com.epam.edp.demo.model.constant.OrderType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This entity stores orders that each reservation made
 * When cart is submitted, food in side cart will be stored in orders
 */

@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_order_reservation_id", columnList = "reservation_id"),
        @Index(name = "idx_order_user_id", columnList = "user_id")
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @Column(nullable = false, updatable = false)
    private String id = UUID.randomUUID().toString();

    @Column(name = "reservation_id")
    private String reservationId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    private List<FoodOrder> orders = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType;
}
