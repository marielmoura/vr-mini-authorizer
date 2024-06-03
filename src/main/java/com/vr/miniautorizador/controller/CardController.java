package com.vr.miniautorizador.controller;

import com.vr.miniautorizador.dto.NewCardRequestDTO;
import com.vr.miniautorizador.service.TransactionService;
import com.vr.miniautorizador.service.exceptions.CardAlreadyExistsException;
import com.vr.miniautorizador.service.exceptions.CardNotFoundException;
import com.vr.miniautorizador.service.CardService;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/cartoes")
public class CardController {

    private final CardService cardService;
    private final TransactionService transactionService;

    public CardController(CardService cardService, TransactionService transactionService) {
        this.cardService = cardService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<NewCardRequestDTO> create(@Valid @RequestBody NewCardRequestDTO newCardCandidate) {
        try {
            return new ResponseEntity<>(cardService.create(newCardCandidate), HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (CardAlreadyExistsException e) {
            return new ResponseEntity<>(newCardCandidate, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<Double> getAmount(@PathVariable String numeroCartao) {
        try {
            return new ResponseEntity<>(transactionService.getCardBalance(numeroCartao), HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (CardNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

