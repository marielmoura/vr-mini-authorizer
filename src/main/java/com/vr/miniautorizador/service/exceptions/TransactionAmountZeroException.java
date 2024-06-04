package com.vr.miniautorizador.service.exceptions;

public class TransactionAmountZeroException extends RuntimeException {
    public TransactionAmountZeroException() {
        super("VALOR_INVALIDO");
    }
}
