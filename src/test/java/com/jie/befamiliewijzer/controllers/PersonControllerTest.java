package com.jie.befamiliewijzer.controllers;

import com.jie.befamiliewijzer.dtos.PersonDto;
import com.jie.befamiliewijzer.dtos.PersonInputDto;
import com.jie.befamiliewijzer.filter.JwtRequestFilter;
import com.jie.befamiliewijzer.services.CustomUserDetailsService;
import com.jie.befamiliewijzer.services.PersonService;
import com.jie.befamiliewijzer.utils.JwtUtil;
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

@WebMvcTest(PersonController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class PersonControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtRequestFilter jwtRequestFilter;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    PersonService personService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetPerson() throws Exception {
        PersonDto dto = new PersonDto();
        dto.id = 11;
        dto.givenNames = "John";
        dto.surname = "Doe";
        dto.sex = "M";

        Mockito.when(personService.getPerson(anyInt())).thenReturn(dto);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/12"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.givenNames", is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sex", is("M")));

    }

    @Test
    void testGetAllPersonsByName() throws Exception {
        PersonDto john = new PersonDto();
        john.id = 11;
        john.givenNames = "John";
        john.surname = "Doe";
        john.sex = "M";

        PersonDto jane = new PersonDto();
        jane.id = 12;
        jane.givenNames = "Jane";
        jane.surname = "Doe";
        jane.sex = "F";

        List<PersonDto> dtos = new ArrayList<>();
        dtos.add(john);
        dtos.add(jane);

        Mockito.when(personService.getAllPersons()).thenReturn(dtos);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].givenNames", is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sex", is("M")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].givenNames", is("Jane")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sex", is("F")));
    }

    @Test
    void testGetAllPersons() throws Exception {
        PersonDto john = new PersonDto();
        john.id = 11;
        john.givenNames = "John";
        john.surname = "Doe";
        john.sex = "M";

        PersonDto jane = new PersonDto();
        jane.id = 12;
        jane.givenNames = "Jane";
        jane.surname = "Doe";
        jane.sex = "F";

        List<PersonDto> dtos = new ArrayList<>();
        dtos.add(john);
        dtos.add(jane);

        Mockito.when(personService.getAllPersonsByName(anyString())).thenReturn(dtos);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/namecontains/Doe"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].givenNames", is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sex", is("M")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].givenNames", is("Jane")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sex", is("F")));
    }

    @Test
    void testGetAllPersonsContainsGivenNamesSurname() throws Exception {
        PersonDto john = new PersonDto();
        john.id = 11;
        john.givenNames = "John";
        john.surname = "Doe";
        john.sex = "M";

        PersonDto jane = new PersonDto();
        jane.id = 12;
        jane.givenNames = "Jane";
        jane.surname = "Doe";
        jane.sex = "F";

        List<PersonDto> dtos = new ArrayList<>();
        dtos.add(john);
        dtos.add(jane);

        Mockito.when(personService.getAllPersonsContainsGivenNamesAndSurname(anyString(), anyString())).thenReturn(dtos);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/contains?givenNames=J&surname=Doe"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].givenNames", is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sex", is("M")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].givenNames", is("Jane")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sex", is("F")));
    }

    @Test
    void TestGetAllPersonsContainsGivenNames() throws Exception {
        PersonDto john = new PersonDto();
        john.id = 11;
        john.givenNames = "John";
        john.surname = "Doe";
        john.sex = "M";

        PersonDto jane = new PersonDto();
        jane.id = 12;
        jane.givenNames = "Jane";
        jane.surname = "Doe";
        jane.sex = "F";

        List<PersonDto> dtos = new ArrayList<>();
        dtos.add(john);
        dtos.add(jane);

        Mockito.when(personService.getAllPersonsContainsGivenNames(anyString())).thenReturn(dtos);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/contains?givenNames=J"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].givenNames", is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sex", is("M")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].givenNames", is("Jane")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sex", is("F")));
    }

    @Test
    void testGetAllPersonsContainsSurname() throws Exception {
        PersonDto john = new PersonDto();
        john.id = 11;
        john.givenNames = "John";
        john.surname = "Doe";
        john.sex = "M";

        PersonDto jane = new PersonDto();
        jane.id = 12;
        jane.givenNames = "Jane";
        jane.surname = "Doe";
        jane.sex = "F";

        List<PersonDto> dtos = new ArrayList<>();
        dtos.add(john);
        dtos.add(jane);

        Mockito.when(personService.getAllPersonsContainsSurname(anyString())).thenReturn(dtos);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/contains?surname=Doe"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].givenNames", is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sex", is("M")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].givenNames", is("Jane")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sex", is("F")));
    }

    @Test
    void testGetAllPersonsContains() throws Exception {
        PersonDto john = new PersonDto();
        john.id = 11;
        john.givenNames = "John";
        john.surname = "Doe";
        john.sex = "M";

        PersonDto jane = new PersonDto();
        jane.id = 12;
        jane.givenNames = "Jane";
        jane.surname = "Doe";
        jane.sex = "F";

        List<PersonDto> dtos = new ArrayList<>();
        dtos.add(john);
        dtos.add(jane);

        Mockito.when(personService.getAllPersons()).thenReturn(dtos);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/contains"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].givenNames", is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sex", is("M")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].givenNames", is("Jane")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sex", is("F")));
    }

    @Test
    void testGetAllChildrenFromPerson() throws Exception {
        PersonDto johnny = new PersonDto();
        johnny.id = 13;
        johnny.givenNames = "Johnny";
        johnny.surname = "Doe";
        johnny.sex = "M";
        List<PersonDto> dtos = new ArrayList<>();
        dtos.add(johnny);

        Mockito.when(personService.getAllChildrenFromPerson(anyInt())).thenReturn(dtos);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/children/11"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(13)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].givenNames", is("Johnny")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sex", is("M")));
    }

    @Test
    void testGetParentrsFromPerson() throws Exception {
        PersonDto john = new PersonDto();
        john.id = 11;
        john.givenNames = "John";
        john.surname = "Doe";
        john.sex = "M";

        PersonDto jane = new PersonDto();
        jane.id = 12;
        jane.givenNames = "Jane";
        jane.surname = "Doe";
        jane.sex = "F";

        List<PersonDto> dtos = new ArrayList<>();
        dtos.add(john);
        dtos.add(jane);

        Mockito.when(personService.getParentsPerson(anyInt())).thenReturn(dtos);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/parents/13"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].givenNames", is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sex", is("M")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].givenNames", is("Jane")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sex", is("F")));
    }

    @Test
    void testgGetPersonsInRelationsWithoutSpouses() throws Exception {
        PersonDto personDto = new PersonDto();
        personDto.id = 11;
        personDto.givenNames = "John";
        personDto.surname = "Doe";
        personDto.sex = "M";
        List<PersonDto> dtos = new ArrayList<>();
        dtos.add(personDto);

        Mockito.when(personService.getPersonsInRelationsWithoutSpouses()).thenReturn(dtos);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/nospouses"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].givenNames", is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sex", is("M")));
    }

    @Test
    void testGetPersonsWithoutRelationsOrChildOf() throws Exception {
        PersonDto personDto = new PersonDto();
        personDto.id = 11;
        personDto.givenNames = "John";
        personDto.surname = "Doe";
        personDto.sex = "M";
        List<PersonDto> dtos = new ArrayList<>();
        dtos.add(personDto);

        Mockito.when(personService.getPersonsWithoutRelationsOrChildOf()).thenReturn(dtos);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/solo"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].givenNames", is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sex", is("M")));
    }

    @Test
    void testCreatePerson() throws Exception {

        //PersonInputDto
        String jsonString = """
                {
                "givenNames": "John",
                "surname": "Doe",
                "sex": "M"
                }
                """;

        PersonDto personDto = new PersonDto();
        personDto.id = 11;
        personDto.givenNames = "John";
        personDto.surname = "Doe";
        personDto.sex = "M";

        Mockito.when(personService.createPerson(any(PersonInputDto.class))).thenReturn(personDto);
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.givenNames", is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sex", is("M")));
    }

    @Test
    void testUpdatePerson() throws Exception {

        //PersonInputDto
        String jsonString = """
                {
                "givenNames": "John",
                "surname": "Doe",
                "sex": "M"
                }
                """;

        PersonDto personDto = new PersonDto();
        personDto.id = 11;
        personDto.givenNames = "John";
        personDto.surname = "Doe";
        personDto.sex = "M";

        Mockito.when(personService.updatePerson(anyInt(), any(PersonInputDto.class))).thenReturn(personDto);
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/persons/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.givenNames", is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sex", is("M")));
    }

    @Test
    void testDeletePerson() throws Exception {

        personService.deletePerson(anyInt());

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/persons/11"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}