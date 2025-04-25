package com.monkila_tech.mokopay_backend.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.monkila_tech.mokopay_backend.models.Facture;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

     @Query("SELECT f FROM Facture f WHERE f.payee=true")
     Optional<List<Facture>> findFacturePayee();

     @Query("SELECT f FROM Facture f ORDER BY f.createdAt DESC")
     Optional<List<Facture>> findByDate();

     @Query("SELECT f FROM Facture f WHERE f.createdAt BETWEEN :dateDebut AND :dateFin")
     List<Facture> findByDateEmissionBetween(@Param("dateDebut") Date dateDebut,
                                        @Param("dateFin") Date dateFin);
    
     @Query("SELECT f FROM Facture f WHERE f.client.id =:clientID")
     List<Facture> findByFactureByUserId(@Param("clientId") Long clientId);
    
}
