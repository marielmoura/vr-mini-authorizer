package com.vr.miniautorizador.service;

import com.vr.miniautorizador.dto.CardDTO;
import com.vr.miniautorizador.model.Card;
import com.vr.miniautorizador.model.Transaction;
import com.vr.miniautorizador.model.TransactionType;
import com.vr.miniautorizador.repository.CardRepository;
import com.vr.miniautorizador.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class CardService {

    @Autowired
    private Environment env;

    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    public CardService(CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public String create(CardDTO newCardCandidate) {
        try {
            Card newCard = cardRepository.save(new Card(newCardCandidate));
            double initialValue = Double.parseDouble(Objects.requireNonNull(env.getProperty("vr.miniauthorizer.initial-card-balance")));
            Transaction newTransactionCandidate = new Transaction(newCard, initialValue, TransactionType.DEPOSIT);
            transactionRepository.save(newTransactionCandidate);
            return String.format("Parabéns! Seu novo cartão %s com saldo %s de foi criado com sucesso e tem o número %s. Agora é necessário desbloqueá-lo",newCard.getType(), initialValue, formatCreditCardNumber(newCard.getNumber()));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar cartão", e);
        }
    }

    private String formatCreditCardNumber(String number) {
        return number.replaceAll(".{4}(?!$)", "$0-");
    }
}
