package com.monkila_tech.mokopay_backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "factures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero; 

    private String description;

    private BigDecimal montant;

    private LocalDateTime dateEmission;

    private LocalDateTime dateEcheance;

    private Boolean payee;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction; 
}
