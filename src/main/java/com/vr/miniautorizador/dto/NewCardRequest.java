package com.vr.miniautorizador.dto;

import com.vr.miniautorizador.model.Card;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.CreditCardNumber;

public record NewCardRequest(
        @NotNull(message = "O número do cartão não pode ser nulo") @NotBlank(message = "O número do cartão não pode ser em branco") @CreditCardNumber(message = "Número do cartão inválido. O cartão deve conter 16 números, sem caracteres especiais") String numeroCartao,
        @NotNull(message = "A senha do cartão não pode ser nula") @NotBlank(message = "A senha do cartão não pode ser em branco") String senha
)
{
    public Card toModel() {
        return new Card(this);
    }
}
