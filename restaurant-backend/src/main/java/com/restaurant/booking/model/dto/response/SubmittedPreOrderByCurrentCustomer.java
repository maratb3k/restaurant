package com.restaurant.booking.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubmittedPreOrderByCurrentCustomer {
    private String cartId;
    private String locationName;
    private String tableNumber;
    private String reservationDate;
    private String reservationTimeSlot;
//    private List<FoodOrder> foodOrders;
    private Double totalPayment;
}
