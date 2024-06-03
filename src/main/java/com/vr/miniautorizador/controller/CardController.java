package com.vr.miniautorizador.controller;

import com.vr.miniautorizador.dto.CardDTO;
import com.vr.miniautorizador.model.Card;
import com.vr.miniautorizador.service.CardService;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CardDTO newCardCandidate) {
        try {
            String statusMessage = cardService.create(newCardCandidate);
            return new ResponseEntity<>(statusMessage, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<String> getAmount(@PathVariable Long cardId) {
        try {
            String statusMessage = cardService.getBalance(cardId);
            return new ResponseEntity<>(statusMessage, HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

