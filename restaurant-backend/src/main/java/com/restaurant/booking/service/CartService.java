package com.restaurant.booking.service;

import com.restaurant.booking.exception.DuplicateDishInCartException;
import com.restaurant.booking.exception.EntityNotFountException;
import com.restaurant.booking.mapper.CartMapper;
import com.restaurant.booking.model.constant.OrderType;
import com.restaurant.booking.model.dto.response.ApiResponse;
import com.restaurant.booking.model.entity.*;
import com.restaurant.booking.repository.CartRepository;
import com.restaurant.booking.repository.DishRepository;
import com.restaurant.booking.repository.OrderRepository;
import com.restaurant.booking.repository.ReservationRepository;
import com.restaurant.booking.util.ResponseBuilder;
import com.restaurant.booking.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final ReservationRepository reservationRepository;
    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;
    private final CartMapper cartMapper;

    public ApiResponse findCartById(String id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFountException("Cart not found"));

        return ResponseBuilder.build(200, true, cartMapper.toResponse(cart));
    }

    public ApiResponse addDishToCart(String dishId, String reservationId) {
        log.info("Adding dish to cart...");
        AuthUser currentUser = Utils.getCurrentUser();

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFountException("Reservation not found with id : %s".formatted(reservationId)));

        // cart will be created when dish is added to cart for the first time
        Cart cart = null;
        if (reservation.getCartId() == null || reservation.getCartId().isBlank()) {
            cart = new Cart(currentUser.getId(), reservationId);
            cartRepository.save(cart);
            reservation.setCartId(cart.getId());

        } else {
            cart = cartRepository.findById(reservation.getCartId())
                    .orElseThrow(() -> new EntityNotFountException("Cart not found with id: %s".formatted(reservation.getCartId())));

            // check if same dish is being added to cart more than once
            for (FoodOrder order : cart.getOrders()) {
                if (order.getDishId().equals(dishId)) {
                    throw new DuplicateDishInCartException("You cannot add one dish to cart twice!");
                }
            }
        }

        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new EntityNotFountException("Dish not found with id: %s".formatted(dishId)));

        List<FoodOrder> orders = cart.getOrders();
        orders.add(new FoodOrder(dishId, 1));
        cart.setOrders(orders);

        cartRepository.save(cart);
        reservationRepository.save(reservation);

        return ResponseBuilder.build(201, true, "Dish successfully added to cart");
    }

    public ApiResponse getCartsByUserId(String userId) {
        log.info("Retrieving cart with user id: {}", userId);

        List<Cart> allCarts = cartRepository.findByUserId(userId);

        List<Cart> carts = allCarts.stream().filter(c -> !c.getOrders().isEmpty()).toList();

        if (carts.isEmpty()) {
            return ResponseBuilder.build(404, true, "Carts not found");
        }

        return ResponseBuilder.build(200, true, cartMapper.toResponses(carts));
    }

    public ApiResponse updateDishesQuantityInCart(String cartId, String dishId, Integer updatedQuantity) {
        try {
            log.info("Updating dishes quantity in cart...");

            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new EntityNotFountException("Cart not found with id: %s".formatted(cartId)));

            for (FoodOrder order : cart.getOrders()) {
                if (Objects.equals(order.getDishId(), dishId)) {
                    if (updatedQuantity == 0) {
                        cart.getOrders().remove(order);
                    }
                    order.setQuantity(updatedQuantity);
                    break;
                }
            }

            cartRepository.save(cart);

            return ResponseBuilder.build(200, true, "Dish quantities updated successfully");

        } catch (Exception e) {
            return ResponseBuilder.build(500, false, "Internal error while updating dish quantities in cart");
        }
    }

    public ApiResponse submitPreOrder(String cartId) {
        log.info("Submitting pre order...");
        AuthUser currentUser = Utils.getCurrentUser();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFountException("Cart not found with id: %s".formatted(cartId)));

        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .orderType(OrderType.PRE_ORDER)
                .reservationId(cart.getReservationId())
                .userId(currentUser.getId())
                .orders(cart.getOrders())
                .build();

        orderRepository.save(order);

        cart.clear();
        cartRepository.save(cart);

        return ResponseBuilder.build(200, true, "Order submitted successfully");
    }

}