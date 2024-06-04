package com.vr.miniautorizador.service.exceptions;

public class CardAlreadyExistsException extends RuntimeException {
    public CardAlreadyExistsException() {
        super("CARTAO_JA_EXISTENTE");
    }
}
