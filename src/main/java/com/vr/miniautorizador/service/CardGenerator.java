package com.vr.miniautorizador.service;

import java.util.Random;

public class CardGenerator {
    public static String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder("4"); // VISA cards start with "4"

        for (int i = 0; i < 15; i++) {
            int digit = random.nextInt(10); // generates a random number between 0 and 9
            cardNumber.append(digit);
        }

        return cardNumber.toString();
    }
}