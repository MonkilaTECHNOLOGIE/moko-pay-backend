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

import com.monkila_tech.mokopay_backend.models.Transaction;
import com.monkila_tech.mokopay_backend.payload.response.StatusResponse;
import com.monkila_tech.mokopay_backend.services.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;


    @SuppressWarnings("rawtypes")
    @PostMapping("/save")
    public ResponseEntity<StatusResponse> saveTransaction(@RequestBody Transaction transaction) {
        StatusResponse statusResponse = new StatusResponse();

        try {
            Transaction transactionDb = transactionService.saveTransaction(transaction);
            statusResponse.setData(transactionDb);
            statusResponse.setMessage("Transaction créée avec succès");
            statusResponse.setStatus(HttpStatus.CREATED.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors de la création de la Transaction : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/all")
    public ResponseEntity<StatusResponse> fetchTransactionList() throws Exception {
        StatusResponse statusResponse = new StatusResponse();

        try {
            List<Transaction> transactionDb = transactionService.fetchTransactionList();
            statusResponse.setData(transactionDb);
            statusResponse.setMessage("La liste des Transaction");
            statusResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors de la recuperation des Transactions : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @SuppressWarnings("rawtypes")
    // @GetMapping("/payees")
    // public ResponseEntity<StatusResponse> fetchTransactionPaye() throws Exception {
       
    //     StatusResponse statusResponse = new StatusResponse();

    //     try {
    //         List<Transaction> TransactionDb = transactionService.fetchTransactionPaye();
    //         statusResponse.setData(TransactionDb);
    //         statusResponse.setMessage("La liste des Transaction payées");
    //         statusResponse.setStatus(HttpStatus.OK.value());
    //         return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    //     } catch (Exception e) {
    //         statusResponse.setMessage("Erreur lors de la recuperation des Transactions : " + e.getMessage());
    //         statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    //         return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    @SuppressWarnings("rawtypes")
    @GetMapping("/user/{userId}")
    public ResponseEntity<StatusResponse> fetchTransactionListByUserId(@PathVariable Long userId) throws Exception {
        
        StatusResponse statusResponse = new StatusResponse();

        try {
            List<Transaction> transactionDb = transactionService.fetchTransactionListByUserId(userId);
            statusResponse.setData(transactionDb);
            statusResponse.setMessage("La liste des Transaction de l'utilisateur");
            statusResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors de la recuperation des Transactions : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/dates")
    public ResponseEntity<StatusResponse> fetchTransactionByDateDebutFin(
            @RequestParam("dateDebut") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,
            @RequestParam("dateFin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin) throws Exception {
       
        StatusResponse statusResponse = new StatusResponse();

        try {
            List<Transaction> transactionDb = transactionService.fetchTransactionByDateDebutFin(dateDebut, dateFin);
            statusResponse.setData(transactionDb);
            statusResponse.setMessage("La liste des Transaction par une plage de date");
            statusResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors de la recuperation des Transactions : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/today")
    public ResponseEntity<StatusResponse> fetchTransactionByDate() throws Exception {
        StatusResponse statusResponse = new StatusResponse();

        try {
            List<Transaction> transactionDb = transactionService.fetchTransactionByDate();
            statusResponse.setData(transactionDb);
            statusResponse.setMessage("La liste des Transaction recente");
            statusResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors de la recuperation des Transactions : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/update-status")
    public ResponseEntity<StatusResponse> payeTransaction(@RequestBody Transaction transaction) throws Exception {
        StatusResponse statusResponse = new StatusResponse();

        try {
            Transaction transactionDb = transactionService.updateTransaction(transaction);
            statusResponse.setData(transactionDb);
            statusResponse.setMessage("La Transaction à été avec success");
            statusResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors du paiement de la Transaction : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/{id}")
    public ResponseEntity<StatusResponse> getTransactionById(@PathVariable("id") Long id) throws Exception {
        StatusResponse statusResponse = new StatusResponse();

        try {
            Transaction transactionDb = transactionService.getTransactionById(id);
            statusResponse.setData(transactionDb);
            statusResponse.setMessage("Les informations de la Transaction");
            statusResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors de la recuperation des informations de la Transaction : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public ResponseEntity<StatusResponse> deleteTransactionById(@PathVariable("id") Long id) throws Exception {
        StatusResponse statusResponse = new StatusResponse();

        try {
            Boolean transactionDb = transactionService.deleteTransactionById(id);
            statusResponse.setData(transactionDb);
            statusResponse.setMessage("La Transaction a été supprimée avec success");
            statusResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setMessage("Erreur lors de la suppression de la Transaction : " + e.getMessage());
            statusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    
}
