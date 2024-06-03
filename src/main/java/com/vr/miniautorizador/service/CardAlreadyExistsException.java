package com.vr.miniautorizador.service;

public class CardAlreadyExistsException extends RuntimeException {
    CardAlreadyExistsException(String number) {
        super(String.format("Cartão %s já existe.", number));
    }
}
