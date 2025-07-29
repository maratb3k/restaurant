package com.epam.edp.demo.controller;

import com.epam.edp.demo.model.dto.response.ApiResponse;
import com.epam.edp.demo.model.entity.AuthUser;
import com.epam.edp.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse> addDishToCart(
            @RequestParam String dishId,
            @RequestParam String reservationId
    ) {
        return ResponseEntity
                .status(201)
                .body(cartService.addDishToCart(dishId, reservationId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getCart(
            @AuthenticationPrincipal AuthUser currentUser
    ) {
        return ResponseEntity
                .status(200)
                .body(cartService.getCartsByUserId(currentUser.getId()));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<ApiResponse> getCartById(@PathVariable String cartId) {
        ApiResponse response = cartService.findCartById(cartId);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateCart(
            @RequestParam String cartId,
            @RequestParam String dishId,
            @RequestParam Integer updatedQuantityOfDish
    ) {
        return ResponseEntity
                .status(201)
                .body(cartService.updateDishesQuantityInCart(cartId, dishId, updatedQuantityOfDish));
    }


    @PostMapping("/submit")
    public ResponseEntity<ApiResponse> submitCart(
            @RequestParam String cartId
    ) {
        return ResponseEntity
                .status(201)
                .body(cartService.submitPreOrder(cartId));
    }
}
