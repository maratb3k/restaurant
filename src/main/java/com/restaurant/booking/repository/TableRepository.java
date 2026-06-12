package com.restaurant.booking.repository;

import com.restaurant.booking.model.entity.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<Tables, String> {

    Optional<Tables> findByTableNumberAndLocationId(int tableNumber, String locationId);
    List<Tables> findAllByLocationId(String locationId);
}
