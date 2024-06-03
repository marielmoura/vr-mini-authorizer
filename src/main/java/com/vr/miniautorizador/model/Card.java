package com.vr.miniautorizador.model;

import com.vr.miniautorizador.dto.NewCardRequest;
import jakarta.persistence.*;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String number;

    private String password;

    public Card() {}

    public Card(NewCardRequest newCardCandidate) {
        this.number = newCardCandidate.numeroCartao();
        this.password = newCardCandidate.senha();
    }

    public String getNumber() {
        return number;
    }

    public String getPassword() {
        return password;
    }

    public NewCardRequest toDTO() {
        return new NewCardRequest(number, password);
    }

    public Long getId() {
        return id;
    }

    public boolean isValidPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}
