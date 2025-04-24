package com.monkila_tech.mokopay_backend.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monkila_tech.mokopay_backend.models.Facture;
import com.monkila_tech.mokopay_backend.payload.response.StatusResponse;
import com.monkila_tech.mokopay_backend.services.FactureService;

@RestController
@RequestMapping("/api/factures")
public class FactureController {

    @Autowired
    FactureService factureService;

    @SuppressWarnings("rawtypes")
    @PostMapping("/save")
    public ResponseEntity<StatusResponse> saveFacture(@RequestBody Facture facture) {
        StatusResponse statusResponse = new StatusResponse();

        try {
            Facture factureDb = factureService.saveFacture(facture);
            statusResponse.setData(factureDb);
            statusResponse.setMessage("Facture créée avec succès");
            statusResponse.setStatus(HttpStatus.CREATED.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors de la création de la facture : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/all")
    public ResponseEntity<StatusResponse> fetchFactureList() throws Exception {
        StatusResponse statusResponse = new StatusResponse();

        try {
            List<Facture> factureDb = factureService.fetchFactureList();
            statusResponse.setData(factureDb);
            statusResponse.setMessage("La liste des Facture");
            statusResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors de la recuperation des factures : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/payees")
    public ResponseEntity<StatusResponse> fetchFacturePaye() throws Exception {
       
        StatusResponse statusResponse = new StatusResponse();

        try {
            List<Facture> factureDb = factureService.fetchFacturePaye();
            statusResponse.setData(factureDb);
            statusResponse.setMessage("La liste des Facture payées");
            statusResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors de la recuperation des factures : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/user/{userId}")
    public ResponseEntity<StatusResponse> fetchFactureListByUserId(@PathVariable Long userId) throws Exception {
        
        StatusResponse statusResponse = new StatusResponse();

        try {
            List<Facture> factureDb = factureService.fetchFactureListByUserId(userId);
            statusResponse.setData(factureDb);
            statusResponse.setMessage("La liste des Facture de l'utilisateur");
            statusResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors de la recuperation des factures : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/dates")
    public ResponseEntity<StatusResponse> fetchFactureByDateDebutFin(
            @RequestParam("dateDebut") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,
            @RequestParam("dateFin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin) throws Exception {
       
        StatusResponse statusResponse = new StatusResponse();

        try {
            List<Facture> factureDb = factureService.fetchFactureByDateDebutFin(dateDebut, dateFin);
            statusResponse.setData(factureDb);
            statusResponse.setMessage("La liste des Facture par une plage de date");
            statusResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors de la recuperation des factures : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/today")
    public ResponseEntity<StatusResponse> fetchFactureByDate() throws Exception {
        StatusResponse statusResponse = new StatusResponse();

        try {
            List<Facture> factureDb = factureService.fetchFactureByDate();
            statusResponse.setData(factureDb);
            statusResponse.setMessage("La liste des Facture recente");
            statusResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors de la recuperation des factures : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/payer")
    public ResponseEntity<StatusResponse> payeFacture(@RequestBody Facture facture) throws Exception {
        StatusResponse statusResponse = new StatusResponse();

        try {
            Facture factureDb = factureService.payeFacture(facture);
            statusResponse.setData(factureDb);
            statusResponse.setMessage("La Facture à été avec success");
            statusResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors du paiement de la facture : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/{id}")
    public ResponseEntity<StatusResponse> getFactureById(@PathVariable("id") Long id) throws Exception {
        StatusResponse statusResponse = new StatusResponse();

        try {
            Facture factureDb = factureService.getFactureById(id);
            statusResponse.setData(factureDb);
            statusResponse.setMessage("Les informations de la facture");
            statusResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors de la recuperation des informations de la facture : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public ResponseEntity<StatusResponse> deleteFactureById(@PathVariable("id") Long id) throws Exception {
        StatusResponse statusResponse = new StatusResponse();

        try {
            Boolean factureDb = factureService.deleteFactureById(id);
            statusResponse.setData(factureDb);
            statusResponse.setMessage("La facture a été supprimée avec success");
            statusResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors de la suppression de la facture : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
