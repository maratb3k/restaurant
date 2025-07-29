package com.epam.edp.demo.repository;

import com.epam.edp.demo.model.entity.Tables;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<Tables, String> {

    Optional<Tables> findByTableNumberAndLocationId(int tableNumber, String locationId);
    List<Tables> findAllByLocationId(String locationId);
}
