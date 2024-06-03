package com.vr.miniautorizador.service.exceptions;

public class InvalidCardPasswordException extends Throwable {
    public InvalidCardPasswordException() {
        super("SENHA_INVALIDA");
    }
}
