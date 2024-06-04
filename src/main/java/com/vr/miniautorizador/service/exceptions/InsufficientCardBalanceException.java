package com.vr.miniautorizador.service.exceptions;

public class InsufficientCardBalanceException extends RuntimeException {
    public InsufficientCardBalanceException() {
        super("SALDO_INSUFICIENTE");
    }
}
