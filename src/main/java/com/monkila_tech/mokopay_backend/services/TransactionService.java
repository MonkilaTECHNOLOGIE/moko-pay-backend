package com.monkila_tech.mokopay_backend.services;

import java.util.Date;
import java.util.List;

import com.monkila_tech.mokopay_backend.models.Transaction;
import com.monkila_tech.mokopay_backend.models.TransactionStatus;


public interface TransactionService {
    Transaction saveTransaction(Transaction transaction) throws Exception;

    List<Transaction> fetchTransactionList() throws Exception;

    List<Transaction> fetchTransactionStatus(TransactionStatus transactionStatus) throws Exception;

    List<Transaction> fetchTransactionListByUserId(Long userId) throws Exception;

    List<Transaction> fetchTransactionByDateDebutFin(Date dateDebut, Date dateFin) throws Exception;

    List<Transaction> fetchTransactionByDate() throws Exception;

    Transaction payeTransaction(Transaction transaction) throws Exception;

    Transaction getTransactionById(Long transactionId) throws Exception;

    Boolean deleteTransactionById(Long transactionId) throws Exception;

}
