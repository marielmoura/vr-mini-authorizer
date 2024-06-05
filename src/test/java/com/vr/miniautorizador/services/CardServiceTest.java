package com.vr.miniautorizador.services;

import com.vr.miniautorizador.controllers.dto.NewCardRequestDTO;
import com.vr.miniautorizador.models.Card;
import com.vr.miniautorizador.repositories.CardRepository;
import com.vr.miniautorizador.repositories.TransactionRepository;
import com.vr.miniautorizador.services.exceptions.CardAlreadyExistsException;
import com.vr.miniautorizador.services.exceptions.InvalidCardNumberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    private static final String VALID_CARD_NUMBER = "4810239597849161";
    private static final String INVALID_CARD_NUMBER = "481023959784916";
    private static final String VALID_PASSWORD = "1234";

    @Mock
    private Environment env;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private CardService cardService;

    private NewCardRequestDTO newValidCardRequest;
    private Card validCard;

    @BeforeEach
    public void setup() {
        newValidCardRequest = new NewCardRequestDTO(VALID_CARD_NUMBER, VALID_PASSWORD);
        validCard = new Card(newValidCardRequest);
    }

    @Test
    void shouldCreateNewCard() {
        when(cardRepository.save(any(Card.class))).thenReturn(validCard);
        Card newCardCreated = cardService.create(newValidCardRequest).toModel();
        assertEquals(newValidCardRequest.toModel(), newCardCreated);
    }

    @Test
    void shouldNotCreateInvalidNewCard() {
        NewCardRequestDTO newInvalidCardRequest = new NewCardRequestDTO(INVALID_CARD_NUMBER, VALID_PASSWORD);
        assertThatExceptionOfType(InvalidCardNumberException.class)
                .isThrownBy(() -> cardService.create(newInvalidCardRequest))
                .withMessage("NUMERO_CARTAO_INVALIDO");
    }

    @Test
    void shouldNotCreateCardAlreadyExists() {
        when(cardRepository.findByNumber(anyString())).thenReturn(Optional.of(validCard));
        assertThatExceptionOfType(CardAlreadyExistsException.class)
                .isThrownBy(() -> cardService.create(newValidCardRequest))
                .withMessage("CARTAO_JA_EXISTENTE");
    }

    @Test
    void shouldFindValidCard() {
        when(cardRepository.findByNumber(VALID_CARD_NUMBER)).thenReturn(Optional.ofNullable(validCard));
        Card foundCard = cardService.findValid(VALID_CARD_NUMBER);
        assertEquals(validCard, foundCard);
    }

    @Test
    void shouldGetBalance() {
        when(transactionRepository.sumAmountsByCardNumber(VALID_CARD_NUMBER)).thenReturn(100.0);
        when(cardRepository.findByNumber(anyString())).thenReturn(Optional.of(validCard));
        Double cardBalance = cardService.getBalance(VALID_CARD_NUMBER);
        assertEquals(100.0, cardBalance);
    }
}