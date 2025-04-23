package com.monkila_tech.mokopay_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monkila_tech.mokopay_backend.models.Transaction;
import com.monkila_tech.mokopay_backend.repository.TransactionRepository;
import com.monkila_tech.mokopay_backend.services.TransactionService;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    Transaction transaction;
    
}
