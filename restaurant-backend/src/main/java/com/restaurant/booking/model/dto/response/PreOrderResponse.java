package com.restaurant.booking.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PreOrderResponse {
    private String orderId;
    private String locationName;
    private String tableNumber;
    private String reservationDate;
    private String reservationTimeSlot;
    private List<FoodOrderResponse> foodOrders;
    private Double totalPayment;
}
