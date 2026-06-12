package com.restaurant.booking.mapper;

import com.restaurant.booking.exception.EntityNotFountException;
import com.restaurant.booking.model.dto.response.CartResponse;
import com.restaurant.booking.model.dto.response.FoodOrderResponse;
import com.restaurant.booking.model.entity.*;
import com.restaurant.booking.repository.DishRepository;
import com.restaurant.booking.repository.LocationRepository;
import com.restaurant.booking.repository.ReservationRepository;
import com.restaurant.booking.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartMapper {

    private final ReservationRepository reservationRepository;
    private final LocationRepository locationRepository;
    private final DishRepository dishRepository;

    public CartResponse toResponse(Cart cart) {
        Reservation reservation = reservationRepository.findById(cart.getReservationId())
                .orElseThrow(() -> new EntityNotFountException("Reservation with id: %s not found".formatted(cart.getReservationId())));

        Location location = locationRepository.findById(reservation.getLocationId())
                .orElseThrow(() -> new EntityNotFountException("Location not found with id: %s".formatted(reservation.getLocationId())));

        List<FoodOrderResponse> foodOrderResponses = new ArrayList<>();

        double totalPrice = 0;
        for (FoodOrder order : cart.getOrders()) {
            Dish dish = dishRepository.findById(order.getDishId())
                    .orElseThrow(() -> new EntityNotFountException("Dish not found"));

            foodOrderResponses.add(
                    FoodOrderResponse.builder()
                            .foodName(dish.getName())
                            .quantity(order.getQuantity())
                            .expectedReadyTime(dish.getExpectedReadyTime())
                            .totalPrice(order.getQuantity() *  dish.getPrice())
                            .build()
            );

            totalPrice +=  order.getQuantity() * dish.getPrice();
        }

        return CartResponse.builder()
                .cartId(cart.getId())
                .locationName(location.getAddress())
                .tableNumber(String.valueOf(reservation.getTableNumber()))
                .reservationDate(reservation.getDate().toString())
                .reservationTimeSlot(Utils.formatTimeSlot(reservation.getStartTime(), reservation.getEndTime()))
                .totalPayment(totalPrice)
                .foodOrders(foodOrderResponses)
                .build();
    }


    public List<CartResponse> toResponses(List<Cart> carts) {
        return carts.stream().map(this::toResponse).toList();
    }
}
