package com.restaurant.booking.repository;

import com.restaurant.booking.model.entity.AuthUser;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, String> {

    Optional<AuthUser> findByEmail(String email);

    Optional<AuthUser> findByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}
