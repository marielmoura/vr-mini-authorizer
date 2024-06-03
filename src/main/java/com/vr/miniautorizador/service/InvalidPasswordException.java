package com.vr.miniautorizador.service;

public class InvalidPasswordException extends Throwable {
    public InvalidPasswordException() {
        super("Senha inválida.");
    }
}
