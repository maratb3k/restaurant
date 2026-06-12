package com.restaurant.booking.mapper;

import com.restaurant.booking.exception.EntityNotFountException;
import com.restaurant.booking.model.dto.response.FoodOrderResponse;
import com.restaurant.booking.model.dto.response.PreOrderResponse;
import com.restaurant.booking.model.entity.*;
import com.restaurant.booking.repository.DishRepository;
import com.restaurant.booking.repository.LocationRepository;
import com.restaurant.booking.repository.OrderRepository;
import com.restaurant.booking.repository.ReservationRepository;
import com.restaurant.booking.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
