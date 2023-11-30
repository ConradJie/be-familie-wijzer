package com.jie.befamiliewijzer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.hamcrest.Matchers.is;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class PersonControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldCreateCorrectPerson() throws Exception {
        String requestJson = """
                {
                    "givenNames" : "John",
                    "surname" : "Doe",
                    "sex" :  "M"
                }
                """;

        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/persons")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.givenNames",is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname",is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sex",is("M")));
    }

    @Test
    void shouldGetCorrectPerson() throws Exception {
        String requestJson = """
                {
                    "givenNames" : "Jane",
                    "surname" : "Doe",
                    "sex" :  "F"
                }
                """;

        MvcResult result = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/persons")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String createdId = result.getResponse().getContentAsString();
        createdId=createdId.substring(6,createdId.indexOf(','));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/" + createdId)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.givenNames",is("Jane")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname",is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sex",is("F")));
    }

}
