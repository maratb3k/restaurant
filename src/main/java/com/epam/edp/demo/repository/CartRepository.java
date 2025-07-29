package com.epam.edp.demo.repository;

import com.epam.edp.demo.model.entity.Cart;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    List<Cart> findByUserId(String userId);

    List<Cart> findByReservationId(String reservationId);
}
