package com.vr.miniautorizador.services.exceptions;

public class TransactionAmountZeroException extends RuntimeException {
    public TransactionAmountZeroException() {
        super("VALOR_INVALIDO");
    }
}
