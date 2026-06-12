package com.restaurant.booking.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "feedbacks", indexes = {
        @Index(name = "idx_l_id", columnList = "location_id")
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {

    @Id
    @Column(nullable = false, updatable = false)
    private String id = UUID.randomUUID().toString();

    @Column(name = "cuisine_comment")
    private String cuisineComment;

    @Column(name = "cuisine_rating")
    private Integer cuisineRating;

    @Column(name = "service_comment")
    private String serviceComment;

    @Column(name = "service_rating")
    private Integer serviceRating;

    private LocalDate date = LocalDate.now();

    @Column(name = "feedback_giver_email")
    private String feedbackGiverEmail; // person who posted the feedback

    @Column(name = "location_id", nullable = false)
    private String locationId;

    @Column(name = "reservation_id")
    private String reservationId;
}
