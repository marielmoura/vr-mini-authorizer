package com.vr.miniautorizador.dto;

import com.vr.miniautorizador.model.Card;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.CreditCardNumber;

public class TransactionRequest {
    @NotBlank(message = "O número do cartão não deve estar em branco")
    @NotNull(message = "O número do cartão é obrigatório")
    private String numeroCartao;
    @NotBlank(message = "A senha do cartão não deve estar em branco")
    @NotNull(message = "A senha do cartão é obrigatória")
    private String senhaCartao;
    @NotBlank @NotNull
    private double valor;

    public TransactionRequest(String numeroCartao, String senhaCartao, double valor) {
        this.numeroCartao = numeroCartao;
        this.senhaCartao = senhaCartao;
        this.valor = valor;
    }

    public String getCardNumber() {
        return numeroCartao;
    }

    public String getPassword() {
        return senhaCartao;
    }

    public double getAmount() {
        return valor;
    }
}