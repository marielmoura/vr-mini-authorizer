package com.vr.miniautorizador.service;

public class CardNotFoundException extends RuntimeException {
    CardNotFoundException() { super("CARTAO_INEXISTENTE"); }
}
