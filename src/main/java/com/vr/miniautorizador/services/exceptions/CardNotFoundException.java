package com.vr.miniautorizador.services.exceptions;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException() { super("CARTAO_INEXISTENTE"); }
}
