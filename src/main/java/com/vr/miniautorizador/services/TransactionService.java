package com.vr.miniautorizador.services;

import com.vr.miniautorizador.controllers.dto.NewTransactionRequestDTO;
import com.vr.miniautorizador.models.Card;
import com.vr.miniautorizador.models.Transaction;
import com.vr.miniautorizador.repositories.TransactionRepository;
import com.vr.miniautorizador.services.exceptions.CardNotFoundException;
import com.vr.miniautorizador.services.exceptions.InsufficientCardBalanceException;
import com.vr.miniautorizador.services.exceptions.InvalidCardPasswordException;
import com.vr.miniautorizador.services.exceptions.TransactionAmountZeroException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CardService cardService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CardService cardService) {
        this.transactionRepository = transactionRepository;
        this.cardService = cardService;
    }

    @Transactional
    public Transaction create(@NotNull NewTransactionRequestDTO newTransactionRequestDTO) throws CardNotFoundException, InvalidCardPasswordException, InsufficientCardBalanceException, TransactionAmountZeroException {
        Transaction transactionCandidate = authorize(newTransactionRequestDTO);
        return transactionRepository.save(transactionCandidate);
    }


    @NotNull
    protected Transaction authorize(@NotNull NewTransactionRequestDTO newTransactionRequestDTO) {
        CardService.checkNumberValidity(newTransactionRequestDTO.getCardNumber());
        checkAmountValidity(newTransactionRequestDTO);
        Card validCardFound = cardService.findValid(newTransactionRequestDTO.getCardNumber());
        validCardFound.checkPasswordValidity(newTransactionRequestDTO);
        checkCardBalance(newTransactionRequestDTO);
        return new Transaction(validCardFound, newTransactionRequestDTO.getAmount());
    }

    private static void checkAmountValidity(@NotNull NewTransactionRequestDTO newTransactionRequestDTO) throws TransactionAmountZeroException {
        if(!newTransactionRequestDTO.isAmountValid()){
            throw new TransactionAmountZeroException();
        }
    }

    protected void checkCardBalance(@NotNull NewTransactionRequestDTO newTransactionRequestDTO) throws CardNotFoundException, InvalidCardPasswordException {
        if(newTransactionRequestDTO.isWithdraw()){
            Double cardBalance = transactionRepository.sumAmountsByCardNumber(newTransactionRequestDTO.getCardNumber());
            boolean isCardBalanceEnough = cardBalance + newTransactionRequestDTO.getAmount() >= 0;
            if(!isCardBalanceEnough) {
                throw new InsufficientCardBalanceException();
            }
        }
    }
}