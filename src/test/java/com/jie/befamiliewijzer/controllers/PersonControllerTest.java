package com.jie.befamiliewijzer.controllers;

import com.jie.befamiliewijzer.dtos.PersonDto;
import com.jie.befamiliewijzer.services.PersonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;

@WebMvcTest(PersonController.class)
@ActiveProfiles("test")
class PersonControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PersonService personService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

//    @Disabled
//    @Test
//    void getCorrectPerson() throws Exception {
//        PersonDto dto = new PersonDto();
//        dto.id = 12;
//        dto.givenNames = "John";
//        dto.surname = "Doe";
//        dto.sex = "M";
//
//        Mockito.when(personService.getPerson(anyInt())).thenReturn(dto);
//        this.mockMvc
//                .perform(MockMvcRequestBuilders.get("/persons/12"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.givenNames",is("John")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.surname",is("Doe")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.sex",is("M")));
//
//    }
}