package com.monkila_tech.mokopay_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.monkila_tech.mokopay_backend.models.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.phone=:phone")
    Optional<User> findByPhone(@Param("phone") String phone);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
