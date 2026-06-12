package com.restaurant.booking.model.entity;

import com.restaurant.booking.model.constant.PreOrderState;
import com.restaurant.booking.model.constant.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reservations", indexes = {
        @Index(name = "idx_reservation_user_id", columnList = "user_id"),
        @Index(name = "idx_waiter_id", columnList = "assigned_waiter_id")
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @Column(nullable = false, updatable = false)
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "table_number", nullable = false)
    private Integer tableNumber;

    @Column(name = "guest_number", nullable = false)
    private Integer guestNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "feedback_id")
    private String feedbackId;

    @Column(name = "location_id", nullable = false)
    private String locationId;

    @Column(name = "cart_id")
    private String cartId;

    @ElementCollection
    @CollectionTable(name = "reservation_orders", joinColumns = @JoinColumn(name = "reservation_id"))
    @Column(name = "order_id")
    private List<String> orderIdList;

    @Column(name = "assigned_waiter_id")
    private String assignedWaiterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private PreOrderState preOrderState;
}
