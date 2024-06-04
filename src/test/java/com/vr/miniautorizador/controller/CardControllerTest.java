package com.vr.miniautorizador.controller;

import com.vr.miniautorizador.dto.NewCardRequestDTO;
import com.vr.miniautorizador.service.CardService;
import com.vr.miniautorizador.service.TransactionService;
import com.vr.miniautorizador.service.exceptions.CardAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CardService cardService;

    @Mock
    private TransactionService transactionService;

    private NewCardRequestDTO newCardRequestDTO;

    @BeforeEach
    void setUp() {
        CardController cardController = new CardController(cardService);
        newCardRequestDTO = new NewCardRequestDTO("4810239597849161", "1234");
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
    }

    @Test
    public void createCard_withValidData_shouldReturn201() throws Exception {
        when(cardService.create(newCardRequestDTO)).thenReturn(newCardRequestDTO);
        executePost(
                status().isCreated());
    }

    @Test
    public void createCard_withExistingCard_shouldReturn422() throws Exception {
        when(cardService.create(newCardRequestDTO)).thenThrow(new CardAlreadyExistsException());
        executePost(
                status().isUnprocessableEntity());
    }

    @Test
    public void getBalance_ofValidCard_shouldReturn200() throws Exception {
        when(cardService.getBalance("6549873025634501")).thenReturn(500.0);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/cartoes/6549873025634501")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("500.0"));
    }

    private void executePost(ResultMatcher status) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/cartoes")
                        .content("{ \"numeroCartao\": \"4810239597849161\", \"senha\": \"1234\" }")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status)
                .andExpect(jsonPath("$.numeroCartao").value("4810239597849161"))
                .andExpect(jsonPath("$.senha").value("1234"));
    }
}