package com.vr.miniautorizador.service;

import com.vr.miniautorizador.dto.NewCardRequestDTO;
import com.vr.miniautorizador.dto.TransactionRequestDTO;
import com.vr.miniautorizador.model.Card;
import com.vr.miniautorizador.model.Transaction;
import com.vr.miniautorizador.repository.CardRepository;
import com.vr.miniautorizador.repository.TransactionRepository;
import com.vr.miniautorizador.service.exceptions.CardAlreadyExistsException;
import com.vr.miniautorizador.service.exceptions.CardNotFoundException;
import com.vr.miniautorizador.service.exceptions.InvalidCardPasswordException;
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

    @Autowired
    public CardService(Environment env, CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.env = env;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public NewCardRequestDTO create(NewCardRequestDTO newCardCandidate) {
        checkForExistingCard(newCardCandidate);

        Card newCard = cardRepository.save(newCardCandidate.toModel());
        double initialValue = Double.parseDouble(Objects.requireNonNull(env.getProperty(INITIAL_CARD_BALANCE)));

        transactionRepository.save(new Transaction(newCard, initialValue));
        return newCard.toDTO();
    }

    private void checkForExistingCard(NewCardRequestDTO newCardCandidate) {
        cardRepository.findByNumber(newCardCandidate.numeroCartao())
                .ifPresent(CardAlreadyExistsException::new);
    }

    Card findValidCard(String cardNumber) throws CardNotFoundException {
        return cardRepository.findByNumber(cardNumber)
                .orElseThrow(CardNotFoundException::new);
    }

    void checkCardPassword(Card foundCard, TransactionRequestDTO transactionRequestDTO) throws InvalidCardPasswordException {
        if(!foundCard.isValidPassword(transactionRequestDTO))
            throw new InvalidCardPasswordException();
    }
}