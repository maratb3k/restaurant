package com.epam.edp.demo.mapper;

import com.epam.edp.demo.exception.EntityNotFountException;
import com.epam.edp.demo.model.dto.response.CartResponse;
import com.epam.edp.demo.model.dto.response.FoodOrderResponse;
import com.epam.edp.demo.model.dto.response.PreOrderResponse;
import com.epam.edp.demo.model.entity.*;
import com.epam.edp.demo.repository.DishRepository;
import com.epam.edp.demo.repository.LocationRepository;
import com.epam.edp.demo.repository.OrderRepository;
import com.epam.edp.demo.repository.ReservationRepository;
import com.epam.edp.demo.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderRepository orderRepository;
    private final ReservationRepository reservationRepository;
    private final LocationRepository locationRepository;
    private final DishRepository dishRepository;

    public PreOrderResponse toPreOrderResponse(Order order) {

        Reservation reservation = reservationRepository.findById(order.getReservationId())
                .orElseThrow(() -> new EntityNotFountException("Reservation not found with id: %s".formatted(order.getReservationId())));

        Location location = locationRepository.findById(reservation.getLocationId())
                .orElseThrow(() -> new EntityNotFountException("Location not found with id: %s".formatted(reservation.getLocationId())));

        List<FoodOrderResponse> foodOrderResponses = new ArrayList<>();

        double totalPrice = 0;
        for (FoodOrder foodOrder : order.getOrders()) {
            Dish dish = dishRepository.findById(foodOrder.getDishId())
                    .orElseThrow(() -> new EntityNotFountException("Dish not found"));

            foodOrderResponses.add(
                    FoodOrderResponse.builder()
                            .dishId(dish.getId())
                            .foodName(dish.getName())
                            .quantity(foodOrder.getQuantity())
                            .expectedReadyTime(dish.getExpectedReadyTime())
                            .totalPrice(foodOrder.getQuantity() *  dish.getPrice())
                            .build()
            );

            totalPrice += foodOrder.getQuantity() * dish.getPrice();
        }

        return PreOrderResponse.builder()
                .orderId(order.getId())
                .locationName(location.getAddress())
                .tableNumber(String.valueOf(reservation.getTableNumber()))
                .reservationDate(reservation.getDate().toString())
                .reservationTimeSlot(Utils.formatTimeSlot(reservation.getStartTime(), reservation.getEndTime()))
                .totalPayment(totalPrice)
                .foodOrders(foodOrderResponses)
                .build();

    }

    public List<PreOrderResponse> toResponses(List<Order> preOrders) {
        return preOrders.stream().map(this::toPreOrderResponse).toList();
    }
}
