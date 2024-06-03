package com.vr.miniautorizador.controller;

import com.vr.miniautorizador.dto.TransactionRequestDTO;
import com.vr.miniautorizador.service.CardNotFoundException;
import com.vr.miniautorizador.service.InvalidPasswordException;
import com.vr.miniautorizador.service.TransactionService;
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
    public ResponseEntity<?> create(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        try {
            transactionService.create(transactionRequestDTO);
            return new ResponseEntity<>("OK", HttpStatus.CREATED);
        } catch (InvalidPasswordException | CardNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}