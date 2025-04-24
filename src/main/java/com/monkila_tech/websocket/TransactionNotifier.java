package com.monkila_tech.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.monkila_tech.mokopay_backend.models.Transaction;

@Component
public class TransactionNotifier {
    @Autowired private SimpMessagingTemplate messagingTemplate;

    public void notifyClients(Transaction transaction) {
        messagingTemplate.convertAndSend("/topic/transactions", transaction);
    }
}
