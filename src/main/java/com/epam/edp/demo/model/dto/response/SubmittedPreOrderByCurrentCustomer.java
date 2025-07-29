package com.epam.edp.demo.model.dto.response;

//import com.epam.edp.demo.model.entity.FoodOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
