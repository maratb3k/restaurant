package com.epam.edp.demo.repository;

import com.epam.edp.demo.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    List<Reservation> findAllByUserId(String userId);

    List<Reservation> findAllByAssignedWaiterId(String waiterId);

    List<Reservation> findByAssignedWaiterIdAndDateBetween(String waiterId, LocalDate start, LocalDate end);

    List<Reservation> findByLocationIdAndDateBetween(String locationId, LocalDate start, LocalDate end);
}
