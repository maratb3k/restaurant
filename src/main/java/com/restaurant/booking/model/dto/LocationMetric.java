package com.restaurant.booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationMetric {
    private String locationId;
    private int ordersProcessed;
    private double deltaOrdersPercent;
    private double avgCuisineFeedback;
    private int minCuisineFeedback;
    private double deltaAvgFeedbackPercent;
    private double revenue;
    private double deltaRevenuePercent;
}