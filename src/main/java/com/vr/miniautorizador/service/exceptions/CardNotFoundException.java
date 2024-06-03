package com.vr.miniautorizador.service.exceptions;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException() { super("CARTAO_INEXISTENTE"); }
}
