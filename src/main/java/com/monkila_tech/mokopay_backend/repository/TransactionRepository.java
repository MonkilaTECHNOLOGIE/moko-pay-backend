package com.monkila_tech.mokopay_backend.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.monkila_tech.mokopay_backend.models.Transaction;
import com.monkila_tech.mokopay_backend.models.TransactionStatus;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

     @Query("SELECT f FROM Transaction f WHERE f.payee=true")
     Optional<List<Transaction>> findTransactionPayee();

     @Query("SELECT f FROM Transaction f ORDER BY p.createdAt DESC")
     Optional<List<Transaction>> findByDate();

     @Query("SELECT f FROM Transaction f WHERE f.createdAt BETWEEN :dateDebut AND :dateFin")
     List<Transaction> findByDateEmissionBetween(@Param("dateDebut") Date dateDebut,
                                        @Param("dateFin") Date dateFin);
    
     @Query("SELECT f FROM Transaction f WHERE f.user.id =:dateFin")
     List<Transaction> findByTransactionByUserId(@Param("userId") Long userId);

     List<Transaction> findByStatus(TransactionStatus transactionStatus);
}
