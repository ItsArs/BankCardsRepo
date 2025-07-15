package com.bankcards;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "John", roles = "USER")
    public void getUserCards_ReturnsMaskedNumbers() throws Exception {
        mockMvc.perform(get("/api/card/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].cardNumber").value(containsString("****")))
                .andExpect(jsonPath("$.content[0].balance").isNumber());
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    public void getCrudWithCard() throws Exception {

        String cardCreateBody = """
        {
            "holderId": 2
        }
        """;
        ResultActions result = mockMvc.perform(post("/api/admin/card/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cardCreateBody))
                .andExpect(status().isCreated());

        String response = result.andReturn().getResponse().getContentAsString();
        JsonNode jsonNode = new ObjectMapper().readTree(response);

        Long cardId = jsonNode.path("id").asLong();

        mockMvc.perform(get("/api/admin/card/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].status").value("NOT_ACTIVE"));

        mockMvc.perform(patch("/api/admin/card/{cardId}/activate", cardId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/admin/card/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].status").value("ACTIVE"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAllCards_AdminAccess_ReturnsAllCards() throws Exception {
        mockMvc.perform(get("/api/admin/card/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @WithMockUser(username = "John", roles = "USER")
    public void sendBlockRequest() throws Exception {

        String blockRequestBody = """
        {
            "id": 2,
            "cause": "lost it"
        }
        """;
        mockMvc.perform(post("/api/card/block-request/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(blockRequestBody))
                .andExpect(status().isCreated());
    }


    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    public void checkAllPendingBlockRequests() throws Exception {
        mockMvc.perform(get("/api/admin/card/block-request/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].status").value(everyItem(equalTo("PENDING"))));
    }


}