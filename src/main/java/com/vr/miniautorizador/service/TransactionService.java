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

    @Transactional
    public Double getAmount(Card card) {
        return transactionRepository.sumAmounts(card.getId());
    }

    @Transactional
    public Transaction deposit(Card card, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("The deposit amount must be positive.");
        }

        return create(card, amount, TransactionType.DEPOSIT);
    }

    @Transactional
    public Transaction withdraw(Card card, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("The withdrawal amount must be positive.");
        }
        double existingBalance = getAmount(card);
        if (existingBalance < amount) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        return create(card, -amount, TransactionType.WITHDRAWAL);
    }
}