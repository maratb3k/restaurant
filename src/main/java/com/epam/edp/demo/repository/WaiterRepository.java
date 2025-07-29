package com.epam.edp.demo.repository;

import com.epam.edp.demo.model.entity.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaiterRepository extends JpaRepository<Waiter, String> {

    boolean existsByEmail(String email);
    Optional<Waiter> findByEmail(String userEmail);
    List<Waiter> findAllByLocationId(String locationId);
}
