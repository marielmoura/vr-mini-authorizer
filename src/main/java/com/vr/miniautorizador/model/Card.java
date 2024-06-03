package com.vr.miniautorizador.model;

import com.vr.miniautorizador.dto.NewCardRequestDTO;
import com.vr.miniautorizador.dto.TransactionRequestDTO;
import jakarta.persistence.*;

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
        this.password = newCardCandidate.senha();
    }

    public String getNumber() {
        return number;
    }

    public NewCardRequestDTO toDTO() {
        return new NewCardRequestDTO(number, password);
    }

    public boolean isValidPassword(TransactionRequestDTO transactionRequestDTO) {
        return this.password.equals(transactionRequestDTO.getPassword());
    }
}
