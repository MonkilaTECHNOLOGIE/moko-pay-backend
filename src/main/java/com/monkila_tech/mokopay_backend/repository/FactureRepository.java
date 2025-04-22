package com.monkila_tech.mokopay_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monkila_tech.mokopay_backend.models.Facture;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {
    
}
