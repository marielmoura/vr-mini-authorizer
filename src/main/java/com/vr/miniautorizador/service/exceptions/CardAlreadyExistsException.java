package com.vr.miniautorizador.service.exceptions;

import com.vr.miniautorizador.model.Card;

public class CardAlreadyExistsException extends RuntimeException {
    public CardAlreadyExistsException(Card card) {
        super(String.format("O cartão %s já está cadastrado", card.getNumber()));
    }
}
