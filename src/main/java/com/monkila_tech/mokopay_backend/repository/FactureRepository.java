package com.monkila_tech.mokopay_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.monkila_tech.mokopay_backend.models.Facture;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

     @Query("SELECT f FROM Facture f WHERE f.payee=true")
     Optional<List<Facture>> findFacturePayee();

     @Query("SELECT f FROM Facture f ORDER BY p.createdAt DESC")
     Optional<List<Facture>> findByDate();
    
}
