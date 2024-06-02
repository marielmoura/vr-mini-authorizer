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

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public Transaction() {}

    public Transaction(Card card, double amount, TransactionType transactionType) {
        this.card = card;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    // Getters and setters...
}
