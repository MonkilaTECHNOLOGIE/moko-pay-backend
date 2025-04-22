package com.monkila_tech.mokopay_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monkila_tech.mokopay_backend.models.ERole;
import com.monkila_tech.mokopay_backend.models.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
