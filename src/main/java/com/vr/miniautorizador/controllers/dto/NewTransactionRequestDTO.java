package com.vr.miniautorizador.controllers.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NewTransactionRequestDTO {
    @NotBlank(message = "O número do cartão não deve estar em branco")
    @NotNull(message = "O número do cartão é obrigatório")
    private String numeroCartao;
    @NotBlank(message = "A senha do cartão não deve estar em branco")
    @NotNull(message = "A senha do cartão é obrigatória")
    private String senhaCartao;
    @NotNull(message = "O valor do cartão é obrigatório")
    @DecimalMin(value = "-9999999.99", message = "Valor deve ser numérico")
    @DecimalMax(value = "9999999.99", message = "Valor deve ser numérico")
    private double valor;

    public NewTransactionRequestDTO(String numeroCartao, String senhaCartao, double valor) {
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

    public boolean isWithdraw() {
        return valor < 0;
    }

    public boolean isAmountValid() {
        return this.valor != 0;
    }
}