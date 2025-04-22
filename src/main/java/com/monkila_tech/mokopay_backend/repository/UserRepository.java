package com.monkilatech.madeinrdc.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.monkilatech.madeinrdc.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.phone=:phone")
    Optional<User> findByPhone(@Param("phone") String phone);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
