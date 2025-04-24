package com.monkila_tech.mokopay_backend.services;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monkila_tech.mokopay_backend.models.Transaction;
import com.monkila_tech.mokopay_backend.models.TransactionStatus;
import com.monkila_tech.mokopay_backend.repository.TransactionRepository;
import com.monkila_tech.websocket.TransactionNotifier;



@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired 
    private TransactionNotifier notifier;

    @Override
    public Transaction saveTransaction(Transaction transaction) throws Exception {
        Transaction saved = transactionRepository.save(transaction);
        notifier.notifyClients(saved);
        return saved;
    }

    @Override
    public List<Transaction> fetchTransactionList() throws Exception {
        return (List<Transaction>) transactionRepository.findAll();
    }


    @Override
    public Transaction updateTransaction(Transaction transaction) throws Exception {

        Transaction transactionDB = transactionRepository.findById(transaction.getId())
                .get();

        if (Objects.nonNull(transaction.getStatus())) {
            transactionDB.setStatus(transaction.getStatus());
        }

        Transaction updated = transactionRepository.save(transactionDB);
        notifier.notifyClients(updated);

        return updated;
    }

    @Override
    public Transaction getTransactionById(Long transactionId) throws Exception {
        return transactionRepository.findById(transactionId).get();
    }

    @Override
    public Boolean deleteTransactionById(Long transactionId) throws Exception {

        Optional<Transaction> transaction = this.transactionRepository.findById(transactionId);

        if (transaction.isEmpty())
            return false;

        this.transactionRepository.deleteById(transactionId);

        Optional<Transaction> transactionChecked = this.transactionRepository.findById(transactionId);

        if (transactionChecked.isEmpty())
            return true;
        return false;

    }

    @Override
    public List<Transaction> fetchTransactionListByUserId(Long userId) throws Exception {
        return this.transactionRepository.findByTransactionByUserId(userId);
    }

    @Override
    public List<Transaction> fetchTransactionByDate() throws Exception {
        return this.transactionRepository.findByDate().get();
    }

    @Override
    public List<Transaction> fetchTransactionByDateDebutFin(Date dateDebut, Date dateFin) throws Exception {
       return this.transactionRepository.findByDateEmissionBetween(dateDebut, dateFin);
    }

    @Override
    public List<Transaction> fetchTransactionStatus(TransactionStatus transactionStatus) throws Exception {
       return this.transactionRepository.findByStatus(transactionStatus);
    }
}
