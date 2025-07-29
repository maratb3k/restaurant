package com.epam.edp.demo.repository.custom;

import com.epam.edp.demo.model.entity.Feedback;

import java.util.List;

public interface FeedbackRepositoryCustom {
    List<Feedback> getFeedbacksByLocationId(int size, boolean ascending, String locationId);
}
