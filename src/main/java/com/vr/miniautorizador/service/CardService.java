package com.vr.miniautorizador.service;

import com.vr.miniautorizador.dto.CardDTO;
import com.vr.miniautorizador.model.Card;
import com.vr.miniautorizador.model.Transaction;
import com.vr.miniautorizador.model.TransactionType;
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
    private static final String CARD_NOT_FOUND = "Cartão não encontrado";
    private static final String CARD_CREATION_ERROR = "Erro ao criar cartão";
    private static final String CARD_AMOUNT_MESSAGE = "O saldo do cartão é %s";
    private static final String CARD_CREATION_SUCCESS_MESSAGE = "Parabéns! Seu novo cartão %s com saldo %s de foi criado com sucesso e tem o número %s. Agora é necessário desbloqueá-lo";

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
    public String create(CardDTO newCardCandidate) {
        try {
            Card newCard = cardRepository.save(new Card(newCardCandidate));
            double initialValue = Double.parseDouble(Objects.requireNonNull(env.getProperty(INITIAL_CARD_BALANCE)));
            Transaction newTransactionCandidate = new Transaction(newCard, initialValue, TransactionType.DEPOSIT);
            transactionRepository.save(newTransactionCandidate);
            return String.format(CARD_CREATION_SUCCESS_MESSAGE, newCard.getType(), initialValue, formatCreditCardNumber(newCard.getNumber()));
        } catch (Exception e) {
            throw new CardServiceException(CARD_CREATION_ERROR, e);
        }
    }

    @Transactional
    public String getBalance(Long cardId) {
        try {
            Card card = cardRepository.findById(cardId)
                    .orElseThrow(() -> new CardServiceException(CARD_NOT_FOUND));
            Double cardAmount = transactionService.getAmount(card);
            return String.format(CARD_AMOUNT_MESSAGE, cardAmount);
        } catch (Exception e) {
            throw new CardServiceException(e.getMessage(), e);
        }
    }

    private String formatCreditCardNumber(String number) {
        return number.replaceAll(".{4}(?!$)", "$0-");
    }

    private static class CardServiceException extends RuntimeException {
        CardServiceException(String message, Exception cause) {
            super(message, cause);
        }

        CardServiceException(String message) {
            super(message);
        }
    }
}