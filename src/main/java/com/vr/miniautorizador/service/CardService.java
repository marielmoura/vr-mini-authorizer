package com.vr.miniautorizador.service;

import com.vr.miniautorizador.dto.NewCardRequestDTO;
import com.vr.miniautorizador.model.Card;
import com.vr.miniautorizador.model.Transaction;
import com.vr.miniautorizador.repository.CardRepository;
import com.vr.miniautorizador.repository.TransactionRepository;
import com.vr.miniautorizador.service.exceptions.CardAlreadyExistsException;
import com.vr.miniautorizador.service.exceptions.CardNotFoundException;
import com.vr.miniautorizador.service.exceptions.InvalidCardNumberException;
import com.vr.miniautorizador.service.validator.CreditCardValidator;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardService {

    private static final String INITIAL_CARD_BALANCE_PROP = "vr.miniauthorizer.initial-card-balance";
    private static final double INITIAL_CARD_BALANCE = 500.0;
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
    public NewCardRequestDTO create(NewCardRequestDTO newCardCandidate) throws CardAlreadyExistsException {
        checkNumberValidity(newCardCandidate.numeroCartao());
        checkForExistingCard(newCardCandidate);

        Card newCard = cardRepository.save(newCardCandidate.toModel());
        transactionRepository.save(new Transaction(newCard, getInitialCardBalance()));
        return newCard.toDTO();
    }

    private double getInitialCardBalance() {
        String initialCardBalance = env.getProperty(INITIAL_CARD_BALANCE_PROP);
        return initialCardBalance != null ? Double.parseDouble(initialCardBalance) : INITIAL_CARD_BALANCE;
    }

    @Transactional
    protected void checkForExistingCard(@NotNull NewCardRequestDTO newCardCandidate) throws CardAlreadyExistsException {
        Optional<Card> matchingCard = cardRepository.findByNumber(newCardCandidate.numeroCartao());
        if(matchingCard.isPresent()){
            throw new CardAlreadyExistsException();
        }
    }

    static void checkNumberValidity(@NotNull String cardNumber) throws InvalidCardNumberException {
        if(!CreditCardValidator.isValid(cardNumber)){
            throw new InvalidCardNumberException();
        }
    }

    @Transactional
    Card findValid(String cardNumber) throws CardNotFoundException {
        return cardRepository.findByNumber(cardNumber)
                .orElseThrow(CardNotFoundException::new);
    }

    @Transactional
    public Double getBalance(String cardNumber) throws CardNotFoundException {
        Card card = findValid(cardNumber);
        return transactionRepository.sumAmountsByCardNumber(card.getNumber());
    }


}