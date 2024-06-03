package com.vr.miniautorizador.service;

import com.vr.miniautorizador.dto.TransactionRequestDTO;
import com.vr.miniautorizador.model.Card;
import com.vr.miniautorizador.model.Transaction;
import com.vr.miniautorizador.repository.CardRepository;
import com.vr.miniautorizador.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    public TransactionService(TransactionRepository transactionRepository, CardRepository cardRepository) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
    }

    @Transactional
    public Transaction create(TransactionRequestDTO transactionRequestDTO) throws InvalidPasswordException {
        Card foundCard = cardRepository.findByNumber(transactionRequestDTO.getCardNumber())
                .orElseThrow(CardNotFoundException::new);

        boolean validPassword = foundCard.isValidPassword(transactionRequestDTO.getPassword());
        return validPassword ? create(foundCard, transactionRequestDTO.getAmount()) : throwInvalidPasswordException();
    }

    private static Transaction throwInvalidPasswordException() throws InvalidPasswordException {
        throw new InvalidPasswordException();
    }

    public Transaction create(Card card, double amount) {
        Transaction transaction = new Transaction(card, amount);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Double getAmount(Card card) {
        return transactionRepository.sumAmounts(card.getId());
    }

//    @Transactional
//    public Transaction deposit(Card card, double amount) {
//        if (amount < 0) {
//            throw new IllegalArgumentException("The deposit amount must be positive.");
//        }
//
//        return create(card, amount);
//    }
//
//    @Transactional
//    public Transaction withdraw(Card card, double amount) {
//        if (amount < 0) {
//            throw new IllegalArgumentException("The withdrawal amount must be positive.");
//        }
//        double existingBalance = getAmount(card);
//        if (existingBalance < amount) {
//            throw new IllegalArgumentException("Insufficient balance.");
//        }
//
//        return create(card, -amount);
//    }
}