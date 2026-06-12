package com.restaurant.booking.repository;

import com.restaurant.booking.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    List<Cart> findByUserId(String userId);

    List<Cart> findByReservationId(String reservationId);
}
