package com.restaurant.booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffMetric {
    private String waiterId;
    private String waiterEmail;
    private int workingHours;
    private int ordersProcessed;
    private double deltaOrdersPercent;
    private double avgServiceFeedback;
    private int minServiceFeedback;
    private double deltaMinFeedbackPercent;
}
