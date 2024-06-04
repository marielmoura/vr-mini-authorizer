package com.vr.miniautorizador.model;

import com.vr.miniautorizador.dto.NewCardRequestDTO;
import com.vr.miniautorizador.dto.TransactionRequestDTO;
import com.vr.miniautorizador.service.exceptions.InvalidCardPasswordException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

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

    public void checkPasswordValidity(TransactionRequestDTO transactionRequestDTO) throws InvalidCardPasswordException {
        String encodedPasswordToCompare = Base64.getEncoder().encodeToString(transactionRequestDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!this.password.equals(encodedPasswordToCompare)){
            throw new InvalidCardPasswordException();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Card card = (Card) obj;
        return Objects.equals(number, card.number) &&
                Objects.equals(password, card.password);
    }
}
