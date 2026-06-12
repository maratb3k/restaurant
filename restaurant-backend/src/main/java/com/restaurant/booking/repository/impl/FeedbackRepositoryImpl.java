package com.restaurant.booking.repository.impl;

import com.restaurant.booking.model.entity.Feedback;
import com.restaurant.booking.repository.custom.FeedbackRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FeedbackRepositoryImpl implements FeedbackRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Feedback> getFeedbacksByLocationId(int size, boolean ascending, String locationId) {
        if (locationId == null || locationId.isBlank()) {
            throw new IllegalArgumentException("Location ID cannot be null");
        }

        String jpql = "FROM Feedback f WHERE f.locationId = :locationId ORDER BY f.date " + (ascending ? "ASC" : "DESC");

        return entityManager.createQuery(jpql, Feedback.class)
                .setParameter("locationId", locationId)
                .setMaxResults(size)
                .getResultList();
    }
}
