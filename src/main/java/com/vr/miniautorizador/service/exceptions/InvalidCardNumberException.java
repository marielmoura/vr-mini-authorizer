package com.vr.miniautorizador.service.exceptions;

public class InvalidCardNumberException extends RuntimeException {
    public InvalidCardNumberException() {
        super("NUMERO_CARTAO_INVALIDO");
    }
}
