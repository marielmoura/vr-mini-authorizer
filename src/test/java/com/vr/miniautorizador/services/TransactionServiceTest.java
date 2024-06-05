package com.vr.miniautorizador.services;

import com.vr.miniautorizador.controllers.dto.NewCardRequestDTO;
import com.vr.miniautorizador.controllers.dto.NewTransactionRequestDTO;
import com.vr.miniautorizador.models.Card;
import com.vr.miniautorizador.models.Transaction;
import com.vr.miniautorizador.repositories.TransactionRepository;
import com.vr.miniautorizador.services.exceptions.InsufficientCardBalanceException;
import com.vr.miniautorizador.services.exceptions.InvalidCardNumberException;
import com.vr.miniautorizador.services.exceptions.InvalidCardPasswordException;
import com.vr.miniautorizador.services.exceptions.TransactionAmountZeroException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    private static final double VALID_TRANSACTION_AMOUNT = 100.0;
    private static final double INVALID_TRANSACTION_AMOUNT = 0;
    private static final String VALID_CARD_NUMBER = "4810239597849161";
    private static final String INVALID_CARD_NUMBER = "481023959784916";
    private static final String VALID_PASSWORD = "1234";
    private static final String INVALID_PASSWORD = "123";

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CardService cardService;
    @InjectMocks
    private TransactionService transactionService;

    private Card validCard;
    private Transaction validTransaction;
    private NewTransactionRequestDTO validTransactionRequest;
    private NewTransactionRequestDTO zeroAmountTransactionRequest;
    private NewTransactionRequestDTO insufficientBalanceTransactionRequest;
    private NewTransactionRequestDTO passwordInvalidTransactionRequest;
    private NewTransactionRequestDTO invalidCardNumberTransactionRequest;

    @BeforeEach
    public void setup() {
        validCard = new Card(new NewCardRequestDTO(VALID_CARD_NUMBER, VALID_PASSWORD));
        validTransaction = new Transaction(validCard, VALID_TRANSACTION_AMOUNT);
        validTransactionRequest = new NewTransactionRequestDTO(VALID_CARD_NUMBER, VALID_PASSWORD, VALID_TRANSACTION_AMOUNT);
        zeroAmountTransactionRequest = new NewTransactionRequestDTO(VALID_CARD_NUMBER, VALID_PASSWORD, INVALID_TRANSACTION_AMOUNT);
        invalidCardNumberTransactionRequest = new NewTransactionRequestDTO(INVALID_CARD_NUMBER, VALID_PASSWORD, VALID_TRANSACTION_AMOUNT);
        passwordInvalidTransactionRequest = new NewTransactionRequestDTO(VALID_CARD_NUMBER, INVALID_PASSWORD, VALID_TRANSACTION_AMOUNT);
        insufficientBalanceTransactionRequest = new NewTransactionRequestDTO(VALID_CARD_NUMBER, VALID_PASSWORD, -501);
    }

    @Test
    void shouldThrowTransactionAmountZeroException_WhenTransactionAmountIsZero() throws TransactionAmountZeroException {
        assertThatExceptionOfType(TransactionAmountZeroException.class)
                .isThrownBy(() -> transactionService.create(zeroAmountTransactionRequest))
                .withMessage("VALOR_INVALIDO");
    }

    @Test
    void shouldThrowInvalidCardNumberException_WhenCardNumberIsInvalid() throws InvalidCardNumberException {
        assertThatExceptionOfType(InvalidCardNumberException.class)
                .isThrownBy(() -> transactionService.create(invalidCardNumberTransactionRequest))
                .withMessage("NUMERO_CARTAO_INVALIDO");
    }

    @Test
    void shouldThrowInvalidCardPasswordException_WhenPasswordIsInvalid() throws InvalidCardPasswordException {
        when(cardService.findValid(passwordInvalidTransactionRequest.getCardNumber())).thenReturn(validCard);
        assertThatExceptionOfType(InvalidCardPasswordException.class)
                .isThrownBy(() -> transactionService.create(passwordInvalidTransactionRequest))
                .withMessage("SENHA_INVALIDA");
    }

    @Test
    void shouldThrowInsufficientCardBalanceException_WhenCardBalanceInsufficient() throws InsufficientCardBalanceException {
        when(cardService.findValid(insufficientBalanceTransactionRequest.getCardNumber())).thenReturn(validCard);
        when(transactionRepository.sumAmountsByCardNumber(insufficientBalanceTransactionRequest.getCardNumber())).thenReturn(500.0);
        assertThatExceptionOfType(InsufficientCardBalanceException.class)
                .isThrownBy(() -> transactionService.create(insufficientBalanceTransactionRequest))
                .withMessage("SALDO_INSUFICIENTE");
    }

    @Test
    void shouldCreateTransactionSuccessfuly_WhenValidRequestProvided() throws TransactionAmountZeroException, InvalidCardPasswordException {
        when(cardService.findValid(validTransactionRequest.getCardNumber())).thenReturn(validCard);
        when(transactionRepository.save(ArgumentMatchers.any())).thenReturn(validTransaction);
        Transaction result = transactionService.create(validTransactionRequest);
        Assertions.assertNotNull(result);
        assertEquals(result.getAmount(), validTransaction.getAmount(), 0.01);
    }

    @Test
    void shouldCalculateCardBalanceCorrectly_WhenRequested() throws InsufficientCardBalanceException {
        when(cardService.getBalance(validCard.getNumber())).thenReturn(100.0);
        Double currentBalance = cardService.getBalance(validCard.getNumber());
        Assertions.assertNotNull(currentBalance, "Balance is null");
        assertEquals(100, currentBalance);
    }
}