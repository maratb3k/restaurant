package com.epam.edp.demo.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CartResponse {
    private String cartId;
    private String locationName;
    private String tableNumber;
    private String reservationDate;
    private String reservationTimeSlot;
    private List<FoodOrderResponse> foodOrders;
    private Double totalPayment;
}
