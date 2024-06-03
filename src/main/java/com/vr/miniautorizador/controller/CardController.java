package com.vr.miniautorizador.controller;

import com.vr.miniautorizador.dto.NewCardRequestDTO;
import com.vr.miniautorizador.service.CardAlreadyExistsException;
import com.vr.miniautorizador.service.CardNotFoundException;
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

    public CardController(CardService cardService) {
        this.cardService = cardService;
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
            return new ResponseEntity<>(cardService.getBalance(numeroCartao), HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (CardNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

