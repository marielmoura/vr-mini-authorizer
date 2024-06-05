package com.vr.miniautorizador.controllers.dto;

import com.vr.miniautorizador.models.Card;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.CreditCardNumber;

public record NewCardRequestDTO(
        @NotNull(message = "O número do cartão não pode ser nulo") @NotBlank(message = "O número do cartão não pode ser em branco") @CreditCardNumber(message = "Número do cartão incorreto. O cartão deve ser válido, conter 16 números e não ter caracteres especiais") String numeroCartao,
        @NotNull(message = "A senha do cartão não pode ser nula") @NotBlank(message = "A senha do cartão não pode ser em branco") String senha
)
{
    public Card toModel() {
        return new Card(this);
    }
}
