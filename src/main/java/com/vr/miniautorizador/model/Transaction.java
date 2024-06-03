package com.vr.miniautorizador.model;

import jakarta.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    private double amount;

    public Transaction() {}

    public Transaction(Card card, double amount) {
        this.card = card;
        this.amount = amount;
    }
}
