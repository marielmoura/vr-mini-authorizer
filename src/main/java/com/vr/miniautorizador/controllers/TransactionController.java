package com.vr.miniautorizador.controllers;

import com.vr.miniautorizador.controllers.dto.NewTransactionRequestDTO;
import com.vr.miniautorizador.services.TransactionService;
import com.vr.miniautorizador.services.exceptions.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody NewTransactionRequestDTO transactionCandidate) {
        try {
            transactionService.create(transactionCandidate);
            return new ResponseEntity<>("OK", HttpStatus.CREATED);
        } catch (InvalidCardNumberException | CardNotFoundException | InvalidCardPasswordException | InsufficientCardBalanceException | TransactionAmountZeroException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}