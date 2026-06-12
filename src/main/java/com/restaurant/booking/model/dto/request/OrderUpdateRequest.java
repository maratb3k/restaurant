package com.restaurant.booking.model.dto.request;

import com.restaurant.booking.model.entity.FoodOrder;
import lombok.Data;

import java.util.List;

@Data
public class OrderUpdateRequest {
    private String orderId;
    private List<FoodOrder> foodOrders;
}
