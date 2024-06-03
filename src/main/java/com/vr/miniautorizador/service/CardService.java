package com.vr.miniautorizador.service;

import com.vr.miniautorizador.dto.NewCardRequest;
import com.vr.miniautorizador.model.Card;
import com.vr.miniautorizador.model.Transaction;
import com.vr.miniautorizador.repository.CardRepository;
import com.vr.miniautorizador.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CardService {

    private static final String INITIAL_CARD_BALANCE = "vr.miniauthorizer.initial-card-balance";
    private final Environment env;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    @Autowired
    public CardService(Environment env, CardRepository cardRepository, TransactionRepository transactionRepository, TransactionService transactionService) {
        this.env = env;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
    }

    @Transactional
    public NewCardRequest create(NewCardRequest newCardCandidate) {
        cardRepository.findByNumber(newCardCandidate.numeroCartao()).ifPresent(card -> {
            throw new CardAlreadyExistsException(card.getNumber());
        });
        Card newCard = cardRepository.save(newCardCandidate.toModel());
        double initialValue = Double.parseDouble(Objects.requireNonNull(env.getProperty(INITIAL_CARD_BALANCE)));
        Transaction newTransactionCandidate = new Transaction(newCard, initialValue);
        transactionRepository.save(newTransactionCandidate);
        return newCard.toDTO();
    }

    @Transactional
    public Double getBalance(String cardNumber) {
        Card card = cardRepository.findByNumber(cardNumber).orElseThrow(CardNotFoundException::new);
        return transactionService.getAmount(card);
    }
}