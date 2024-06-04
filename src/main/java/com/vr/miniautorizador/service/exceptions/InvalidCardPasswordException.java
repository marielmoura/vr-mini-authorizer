package com.vr.miniautorizador.service.exceptions;

public class InvalidCardPasswordException extends RuntimeException {
    public InvalidCardPasswordException() {
        super("SENHA_INVALIDA");
    }
}
