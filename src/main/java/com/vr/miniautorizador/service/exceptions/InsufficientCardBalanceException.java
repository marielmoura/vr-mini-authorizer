package com.vr.miniautorizador.service.exceptions;

public class InsufficientCardBalanceException extends Throwable {
    public InsufficientCardBalanceException() {
        super("SALDO_INSUFICIENTE");
    }
}
