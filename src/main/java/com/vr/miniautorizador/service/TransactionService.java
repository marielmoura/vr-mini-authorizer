package com.vr.miniautorizador.service;

import com.vr.miniautorizador.dto.TransactionRequestDTO;
import com.vr.miniautorizador.model.Card;
import com.vr.miniautorizador.model.Transaction;
import com.vr.miniautorizador.repository.TransactionRepository;
import com.vr.miniautorizador.service.exceptions.*;
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
    public Transaction create(@NotNull TransactionRequestDTO transactionRequestDTO) throws CardNotFoundException, InvalidCardPasswordException, InsufficientCardBalanceException, TransactionAmountZeroException {
        Transaction transactionCandidate = authorize(transactionRequestDTO);
        return transactionRepository.save(transactionCandidate);
    }

    @Transactional
    @NotNull
    protected Transaction authorize(@NotNull TransactionRequestDTO transactionRequestDTO) {
        CardService.checkNumberValidity(transactionRequestDTO.getCardNumber());
        checkAmountValidity(transactionRequestDTO);
        Card validCardFound = cardService.findValid(transactionRequestDTO.getCardNumber());
        validCardFound.checkPasswordValidity(transactionRequestDTO);
        checkCardBalance(transactionRequestDTO);
        return new Transaction(validCardFound, transactionRequestDTO.getAmount());
    }

    private static void checkAmountValidity(@NotNull TransactionRequestDTO transactionRequestDTO) throws TransactionAmountZeroException {
        if(!transactionRequestDTO.isAmountValid()){
            throw new TransactionAmountZeroException();
        }
    }

    @Transactional
    protected void checkCardBalance(@NotNull TransactionRequestDTO transactionRequestDTO) throws CardNotFoundException, InvalidCardPasswordException {
        if(transactionRequestDTO.isWithdraw()){
            Double cardBalance = transactionRepository.sumAmountsByCardNumber(transactionRequestDTO.getCardNumber());
            boolean isCardBalanceEnough = cardBalance + transactionRequestDTO.getAmount() >= 0;
            if(!isCardBalanceEnough) {
                throw new InsufficientCardBalanceException();
            }
        }
    }
}