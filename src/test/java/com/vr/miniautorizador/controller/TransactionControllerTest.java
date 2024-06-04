package com.vr.miniautorizador.controller;

import com.vr.miniautorizador.dto.TransactionRequestDTO;
import com.vr.miniautorizador.service.TransactionService;
import com.vr.miniautorizador.service.exceptions.CardNotFoundException;
import com.vr.miniautorizador.service.exceptions.InsufficientCardBalanceException;
import com.vr.miniautorizador.service.exceptions.InvalidCardPasswordException;
import com.vr.miniautorizador.service.exceptions.TransactionAmountZeroException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    private MockMvc mockMvc;
    private String jsonRequest;

    @Mock
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        TransactionController transactionController = new TransactionController(transactionService);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        jsonRequest = "{ \"numeroCartao\": \"4810239597849161\", \"senhaCartao\": \"1234\", \"valor\": 0 }";
    }

    @Test
    public void whenTransactionAmountIsZero_thenReturnValorInvalido() throws Exception, InsufficientCardBalanceException, TransactionAmountZeroException, InvalidCardPasswordException {
        when(transactionService.create(any(TransactionRequestDTO.class))).thenThrow(new TransactionAmountZeroException());
        assertTransactionResponse(jsonRequest, "VALOR_INVALIDO");
    }

    @Test
    public void whenCardNotFound_thenReturnCartaoInexistente() throws Exception, InsufficientCardBalanceException, TransactionAmountZeroException, InvalidCardPasswordException {
        when(transactionService.create(any(TransactionRequestDTO.class))).thenThrow(new CardNotFoundException());
        assertTransactionResponse(jsonRequest, "CARTAO_INEXISTENTE");
    }

    @Test
    public void whenInvalidCardPassword_thenReturnSenhaInvalida() throws Exception, InsufficientCardBalanceException, TransactionAmountZeroException, InvalidCardPasswordException {
        when(transactionService.create(any(TransactionRequestDTO.class))).thenThrow(new InvalidCardPasswordException());
        assertTransactionResponse(jsonRequest, "SENHA_INVALIDA");
    }

    @Test
    public void whenInsufficientCardBalance_thenReturnSaldoInsuficiente() throws Exception, InsufficientCardBalanceException, TransactionAmountZeroException, InvalidCardPasswordException {
        when(transactionService.create(any(TransactionRequestDTO.class))).thenThrow(new InsufficientCardBalanceException());
        assertTransactionResponse(jsonRequest, "SALDO_INSUFICIENTE");
    }

    @Test
    public void whenCardTransactionIsSuccessful_thenReturnOk() throws Exception {
        assertTransactionResponse(jsonRequest, "OK", true);
    }

    private void assertTransactionResponse(String json, String expectedResponseContent) throws Exception {
        assertTransactionResponse(json, expectedResponseContent, false);
    }

    private void assertTransactionResponse(String json, String expectedResponseContent, boolean isOk) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/transacoes")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(isOk ? status().isCreated() : status().isUnprocessableEntity())
                .andExpect(content().string(expectedResponseContent));
    }
}