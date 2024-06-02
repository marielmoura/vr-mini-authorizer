package com.vr.miniautorizador.dto;

import com.vr.miniautorizador.model.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class CardDTO {

    @NotNull(message = "A senha do cartão não pode ser nula")
    @NotBlank(message = "A senha do cartão não pode ser em branco")
    private final String password;

    @NotNull(message = "O tipo do cartão não pode ser nulo")
    private CardType type;

    public CardDTO(String password, CardType type) {
        this.password = password;
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public CardType getType() {
        return type;
    }
}
