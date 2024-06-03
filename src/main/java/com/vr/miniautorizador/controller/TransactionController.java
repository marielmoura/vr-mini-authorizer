package com.vr.miniautorizador.controller;

import com.vr.miniautorizador.dto.TransactionRequest;
import com.vr.miniautorizador.model.Transaction;
import com.vr.miniautorizador.service.InvalidPasswordException;
import com.vr.miniautorizador.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> create(@RequestBody TransactionRequest transactionRequest) {
        try {
            transactionService.create(transactionRequest);
            return new ResponseEntity<>("OK", HttpStatus.CREATED);
        } catch (InvalidPasswordException e) {
            throw new RuntimeException(e);
        }
    }
}