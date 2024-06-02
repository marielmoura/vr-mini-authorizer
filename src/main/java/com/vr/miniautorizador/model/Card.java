package com.vr.miniautorizador.model;

import com.vr.miniautorizador.dto.CardDTO;
import com.vr.miniautorizador.service.CardGenerator;
import jakarta.persistence.*;
import org.springframework.data.relational.core.mapping.Table;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String number;

    private String password;

    private String type;

    private boolean status;

    public Card() {}

    public Card(CardDTO newCardCandidate) {
        this.number = CardGenerator.generateCardNumber();
        this.type = newCardCandidate.getType().name();
        this.password = newCardCandidate.getPassword();
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }
}
