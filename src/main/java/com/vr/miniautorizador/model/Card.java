package com.vr.miniautorizador.model;

import com.vr.miniautorizador.dto.NewCardRequestDTO;
import com.vr.miniautorizador.dto.TransactionRequestDTO;
import jakarta.persistence.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String number;

    private String password;

    public Card() {}

    public Card(NewCardRequestDTO newCardCandidate) {
        this.number = newCardCandidate.numeroCartao();
        this.password = Base64.getEncoder().encodeToString(newCardCandidate.senha().getBytes(StandardCharsets.UTF_8));
    }

    public String getNumber() {
        return number;
    }

    public NewCardRequestDTO toDTO() {
        return new NewCardRequestDTO(number, new String(Base64.getDecoder().decode(password), StandardCharsets.UTF_8));
    }

    public boolean isValidPassword(TransactionRequestDTO transactionRequestDTO) {
        String encodedPasswordToCompare = Base64.getEncoder().encodeToString(transactionRequestDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        return this.password.equals(encodedPasswordToCompare);
    }
}
