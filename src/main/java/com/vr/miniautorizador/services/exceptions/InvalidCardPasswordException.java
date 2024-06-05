package com.vr.miniautorizador.services.exceptions;

public class InvalidCardPasswordException extends RuntimeException {
    public InvalidCardPasswordException() {
        super("SENHA_INVALIDA");
    }
}
