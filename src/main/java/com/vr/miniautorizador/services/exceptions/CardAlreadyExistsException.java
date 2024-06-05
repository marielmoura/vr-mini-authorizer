package com.vr.miniautorizador.services.exceptions;

public class CardAlreadyExistsException extends RuntimeException {
    public CardAlreadyExistsException() {
        super("CARTAO_JA_EXISTENTE");
    }
}
