package com.epam.edp.demo.repository;

import com.epam.edp.demo.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findAllByUserId(String userId);

    @Query("SELECT o FROM Order o WHERE o.reservationId IN :ids")
    List<Order> findByReservationIdIn(@Param("ids") List<Long> reservationIds);
}
