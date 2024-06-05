package com.vr.miniautorizador.services.exceptions;

public class InvalidCardNumberException extends RuntimeException {
    public InvalidCardNumberException() {
        super("NUMERO_CARTAO_INVALIDO");
    }
}
