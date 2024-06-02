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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> create(@Valid @RequestBody CardDTO newCardCandidate) {
        try {
            String statusMessage = cardService.create(newCardCandidate);
            return new ResponseEntity<>(statusMessage, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

