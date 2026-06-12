package com.restaurant.booking.repository;

import com.restaurant.booking.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {

    @Query("SELECT l.id FROM Location l")
    List<String> findAllIds();

}
