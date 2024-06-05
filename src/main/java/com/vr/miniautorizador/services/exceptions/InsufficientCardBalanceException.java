package com.vr.miniautorizador.services.exceptions;

public class InsufficientCardBalanceException extends RuntimeException {
    public InsufficientCardBalanceException() {
        super("SALDO_INSUFICIENTE");
    }
}
