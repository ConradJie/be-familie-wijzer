package com.jie.befamiliewijzer.controllers;

import com.jie.befamiliewijzer.dtos.*;
import com.jie.befamiliewijzer.filter.JwtRequestFilter;
import com.jie.befamiliewijzer.services.CustomUserDetailsService;
import com.jie.befamiliewijzer.services.RelationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;

@WebMvcTest(RelationController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class RelationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtRequestFilter jwtRequestFilter;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    RelationService relationService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetRelation() throws Exception {
        RelationDto dto = new RelationDto();
        dto.id = 10;
        dto.personId = 11;
        dto.spouseId = 12;

        Mockito.when(relationService.getRelation(anyInt())).thenReturn(dto);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/relations/10"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.personId", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.spouseId", is(12)));
    }

    @Test
    void testGetRelationsFromPerson() throws Exception {
        RelationSpouseDto dto = new RelationSpouseDto();
        dto.id = 10;
        dto.personId = 11;
        dto.spouseId = 12;
        dto.spouseGivenNames = "Jane";
        dto.spouseSurname = "Doe";
        dto.spouseSex = "F";
        List<RelationSpouseDto> dtos = new ArrayList<>();
        dtos.add(dto);

        Mockito.when(relationService.getAllRelationsFromPersonId(anyInt())).thenReturn(dtos);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/relations/persons/11"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].personId", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].spouseId", is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].spouseGivenNames", is("Jane")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].spouseSurname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].spouseSex", is("F")));
    }

    @Test
    void testGetAllRelations() throws Exception {
        RelationDto dto0 = new RelationDto();
        dto0.id = 10;
        dto0.personId = 11;
        dto0.spouseId = 12;

        RelationDto dto1 = new RelationDto();
        dto1.id = 11;
        dto1.personId = 13;
        dto1.spouseId = 14;
        List<RelationDto> dtos = new ArrayList<>();
        dtos.add(dto0);
        dtos.add(dto1);

        Mockito.when(relationService.getAllRelations()).thenReturn(dtos);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/relations"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].personId", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].spouseId", is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].personId", is(13)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].spouseId", is(14)));
    }

    @Test
    void testCreateRelation() throws Exception {

        //RelationInputDto
        String jsonString = """
                {
                "personId" : 11,
                "spouseId" : 12
                }
                """;

        RelationDto relationDto = new RelationDto();
        relationDto.id = 10;
        relationDto.personId = 11;
        relationDto.spouseId = 12;

        Mockito.when(relationService.createRelation(any(RelationInputDto.class))).thenReturn(relationDto);
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/relations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.personId", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.spouseId", is(12)));
    }

    @Test
    void testUpdateRelation() throws Exception {

        //RelationInputDto
        String jsonString = """
                {
                "id" : 10,
                "personId" : 11,
                "spouseId" : 12
                }
                """;

        RelationDto relationDto = new RelationDto();
        relationDto.id = 10;
        relationDto.personId = 11;
        relationDto.spouseId = 12;

        Mockito.when(relationService.updateRelation(anyInt(), any(RelationInputDto.class))).thenReturn(relationDto);
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/relations/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.personId", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.spouseId", is(12)));
    }

    @Test
    void testDeleteRelation() throws Exception {

        relationService.deleteRelation(anyInt());

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/relations/10"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testRemovePersonFromRelation() throws Exception {

        relationService.removePersonFromRelation(anyInt(), anyInt());

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/relations/10/12"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}