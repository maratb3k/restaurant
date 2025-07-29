package com.epam.edp.demo.model.dto.request;

import com.epam.edp.demo.model.entity.FoodOrder;
import lombok.Data;

import java.util.List;

@Data
public class OrderUpdateRequest {
    private String orderId;
    private List<FoodOrder> foodOrders;
}
