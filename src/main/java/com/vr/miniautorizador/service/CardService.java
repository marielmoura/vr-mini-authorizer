package com.vr.miniautorizador.service;

import com.vr.miniautorizador.dto.CardDTO;
import com.vr.miniautorizador.model.Card;
import com.vr.miniautorizador.repository.CardRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    public String create(CardDTO newCardCandidate) {
        try {
            Card card = new Card(newCardCandidate);
            Card newCard = cardRepository.save(card);
            return String.format("Parabéns! Seu novo cartão %s foi criado com sucesso e tem o número %s. Agora é necessário desbloqueá-lo",newCard.getType(), formatCreditCardNumber(newCard.getNumber()));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar cartão", e);
        }
    }

    private String formatCreditCardNumber(String number) {
        return number.replaceAll(".{4}(?!$)", "$0-");
    }
}
