package com.vr.miniautorizador.service;

import com.vr.miniautorizador.dto.TransactionRequestDTO;
import com.vr.miniautorizador.model.Card;
import com.vr.miniautorizador.model.Transaction;
import com.vr.miniautorizador.repository.CardRepository;
import com.vr.miniautorizador.repository.TransactionRepository;
import com.vr.miniautorizador.service.exceptions.InsufficientCardBalanceException;
import com.vr.miniautorizador.service.exceptions.InvalidCardPasswordException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CardService cardService;

    public TransactionService(TransactionRepository transactionRepository, CardRepository cardRepository, CardService cardService) {
        this.transactionRepository = transactionRepository;
        this.cardService = cardService;
    }

    @Transactional
    public Transaction create(TransactionRequestDTO transactionRequestDTO) throws InvalidCardPasswordException, InsufficientCardBalanceException {
        Card foundCard = cardService.findValidCard(transactionRequestDTO.getCardNumber());
        cardService.checkCardPassword(foundCard, transactionRequestDTO);
        checkCardBalance(transactionRequestDTO);

        Transaction transactionCandidate = new Transaction(foundCard, transactionRequestDTO.getAmount());
        return transactionRepository.save(transactionCandidate);
    }

    @Transactional
    protected void checkCardBalance(TransactionRequestDTO transactionRequestDTO) throws InsufficientCardBalanceException {
        if(isCardBalanceEnough(transactionRequestDTO))
            throw new InsufficientCardBalanceException();
    }

    protected boolean isCardBalanceEnough(TransactionRequestDTO transactionRequestDTO) {
        Double cardBalance = transactionRepository.sumAmountsByCardNumber(transactionRequestDTO.getCardNumber());
        boolean isCardBalanceEnough = cardBalance + transactionRequestDTO.getAmount() >= 0;
        return !transactionRequestDTO.isDeposit() && !isCardBalanceEnough;
    }

    public Double getCardBalance(String cardNumber) {
        Card card = cardService.findValidCard(cardNumber);
        return transactionRepository.sumAmountsByCardNumber(card.getNumber());
    }
}