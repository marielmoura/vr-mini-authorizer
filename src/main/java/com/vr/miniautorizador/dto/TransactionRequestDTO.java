package com.vr.miniautorizador.dto;

import jakarta.validation.constraints.*;

public class TransactionRequestDTO {
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

    public TransactionRequestDTO(String numeroCartao, String senhaCartao, double valor) {
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