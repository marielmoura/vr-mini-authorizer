package com.vr.miniautorizador.controller;

import com.vr.miniautorizador.dto.TransactionRequestDTO;
import com.vr.miniautorizador.service.exceptions.CardNotFoundException;
import com.vr.miniautorizador.service.exceptions.InsufficientCardBalanceException;
import com.vr.miniautorizador.service.exceptions.InvalidCardPasswordException;
import com.vr.miniautorizador.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacoes")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TransactionRequestDTO transactionCandidate) {
        try {
            transactionService.create(transactionCandidate);
            return new ResponseEntity<>("OK", HttpStatus.CREATED);
        } catch (InvalidCardPasswordException | InsufficientCardBalanceException | CardNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}