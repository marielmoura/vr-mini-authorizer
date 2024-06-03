package com.vr.miniautorizador.service.exceptions;

public class TransactionAmountZeroException extends Throwable {
    public TransactionAmountZeroException() {
        super("ZERO_IS_NOT_A_VALID_VALUE_FOR_TRANSACTION");
    }
}
