package com.bankcards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "John", roles = "USER")
    public void transferBetweenCards_Success() throws Exception {
        String requestBody = """
        {
            "fromCardId": 1,
            "toCardId": 2,
            "amount": 100.0
        }
        """;

        BigDecimal bd1 = getBalance(1);
        BigDecimal bd2 = getBalance(2);
        BigDecimal amount = new BigDecimal("100.00");

        mockMvc.perform(post("/api/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer done!"));

        assertBalance(1, bd1.subtract(amount));
        assertBalance(2, bd2.add(amount));
    }

    private BigDecimal getBalance(long cardId) throws Exception {
        String response = mockMvc.perform(get("/api/card/balance/" + cardId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return new BigDecimal(response);
    }

    private void assertBalance(long cardId, BigDecimal expected) throws Exception {
        String actual = mockMvc.perform(get("/api/card/balance/" + cardId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        BigDecimal actualDecimal = new BigDecimal(actual);
        assertThat(actualDecimal).isEqualByComparingTo(expected);
    }

    @Test
    @WithMockUser(username = "John", roles = "USER")
    public void transferBetweenCards_InsufficientBalance_Returns400() throws Exception {
        String requestBody = """
        {
            "fromCardId": 1,
            "toCardId": 2,
            "amount": 100000.0
        }
        """;

        mockMvc.perform(post("/api/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Not enough money! You need more:"));
    }
}
