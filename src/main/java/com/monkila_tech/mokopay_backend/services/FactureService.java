package com.monkila_tech.mokopay_backend.services;

import java.util.Date;
import java.util.List;

import com.monkila_tech.mokopay_backend.models.Facture;

public interface FactureService {

    Facture saveFacture(Facture facture) throws Exception;

    List<Facture> fetchFactureList() throws Exception;

    List<Facture> fetchFacturePaye() throws Exception;

    List<Facture> fetchFactureListByUserId(Long userId) throws Exception;

    List<Facture> fetchFactureByDateDebutFin(Date dateDebut, Date dateFin) throws Exception;

    List<Facture> fetchFactureByDate() throws Exception;

    Facture payeFacture(Facture facture) throws Exception;

    Facture getFactureById(Long factureId) throws Exception;

    Boolean deleteFactureById(Long factureId) throws Exception;

}
