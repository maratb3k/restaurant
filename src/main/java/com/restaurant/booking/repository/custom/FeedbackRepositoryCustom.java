package com.restaurant.booking.repository.custom;

import com.restaurant.booking.model.entity.Feedback;

import java.util.List;

public interface FeedbackRepositoryCustom {
    List<Feedback> getFeedbacksByLocationId(int size, boolean ascending, String locationId);
}
