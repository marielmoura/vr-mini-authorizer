package com.vr.miniautorizador.service;

public class CardNotFoundException extends RuntimeException {
    CardNotFoundException() { super("Cartão não encontrado."); }
}
