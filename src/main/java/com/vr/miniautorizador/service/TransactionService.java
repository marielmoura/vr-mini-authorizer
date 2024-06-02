package com.vr.miniautorizador.service;

import com.vr.miniautorizador.model.Card;
import com.vr.miniautorizador.model.Transaction;
import com.vr.miniautorizador.model.TransactionType;
import com.vr.miniautorizador.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction create(Card card, double amount, TransactionType transactionType) {
        Transaction transaction = new Transaction(card, amount, transactionType);
        return transactionRepository.save(transaction);
    }
}