package com.jie.befamiliewijzer.controllers;

import com.jie.befamiliewijzer.dtos.EventDto;
import com.jie.befamiliewijzer.dtos.EventInputDto;
import com.jie.befamiliewijzer.dtos.EventTypeDto;
import com.jie.befamiliewijzer.services.EventService;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@WebMvcTest(EventController.class)
@ActiveProfiles("test")
class EventControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    EventService eventService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetEventTypes() throws Exception {
        List<EventTypeDto> dtos = new ArrayList<>();
        EventTypeDto birth = new EventTypeDto();
        birth.eventType = "BIRTH";
        EventTypeDto death = new EventTypeDto();
        death.eventType = "DEATH";
        EventTypeDto marriage = new EventTypeDto();
        marriage.eventType = "MARRIAGE";
        EventTypeDto divorce = new EventTypeDto();
        divorce.eventType = "DIVORCE";
        EventTypeDto migration = new EventTypeDto();
        migration.eventType = "MIGRATION";
        EventTypeDto celebration = new EventTypeDto();
        celebration.eventType = "CELEBRATION";
        EventTypeDto others = new EventTypeDto();
        others.eventType = "OTHERS";

        dtos.add(birth);
        dtos.add(death);
        dtos.add(marriage);
        dtos.add(divorce);
        dtos.add(celebration);
        dtos.add(migration);
        dtos.add(others);

        Mockito.when(eventService.getEventTypes()).thenReturn(dtos);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/eventtypes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].eventType", is("BIRTH")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].eventType", is("DEATH")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].eventType", is("MARRIAGE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].eventType", is("DIVORCE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].eventType", is("CELEBRATION")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[5].eventType", is("MIGRATION")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[6].eventType", is("OTHERS")));

    }

    @Test
    void testGetPerosnEventTypes() throws Exception {
        List<EventTypeDto> dtos = new ArrayList<>();
        EventTypeDto birth = new EventTypeDto();
        birth.eventType = "BIRTH";
        EventTypeDto death = new EventTypeDto();
        death.eventType = "DEATH";
        EventTypeDto migration = new EventTypeDto();
        migration.eventType = "MIGRATION";
        EventTypeDto celebration = new EventTypeDto();
        celebration.eventType = "CELEBRATION";
        EventTypeDto others = new EventTypeDto();
        others.eventType = "OTHERS";

        dtos.add(birth);
        dtos.add(death);
        dtos.add(celebration);
        dtos.add(migration);
        dtos.add(others);

        Mockito.when(eventService.getPersonEventTypes()).thenReturn(dtos);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/eventtypes/person"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].eventType", is("BIRTH")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].eventType", is("DEATH")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].eventType", is("CELEBRATION")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].eventType", is("MIGRATION")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].eventType", is("OTHERS")));
    }

    @Test
    void testGetRelationEventTypes() throws Exception {
        List<EventTypeDto> dtos = new ArrayList<>();
        EventTypeDto marriage = new EventTypeDto();
        marriage.eventType = "MARRIAGE";
        EventTypeDto divorce = new EventTypeDto();
        divorce.eventType = "DIVORCE";
        EventTypeDto others = new EventTypeDto();
        others.eventType = "OTHERS";

        dtos.add(marriage);
        dtos.add(divorce);
        dtos.add(others);

        Mockito.when(eventService.getRelationEventTypes()).thenReturn(dtos);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/eventtypes/relation"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].eventType", is("MARRIAGE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].eventType", is("DIVORCE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].eventType", is("OTHERS")));
    }

    @Test
    void testGetEventFromPerson() throws Exception {
        EventDto dto = new EventDto();
        dto.id = 30;
        dto.eventType = "OTHERS";
        dto.description = "Test";
        dto.text = "Testing";
        dto.beginDate = new Date(2000, Calendar.JANUARY, 1);
        dto.endDate = new Date(2000, Calendar.JANUARY, 1);
        dto.personId = 11;
        dto.relationId = null;

        Mockito.when(eventService.getEventFromPerson(11, 30)).thenReturn(dto);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/11/events/30"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.eventType", is("OTHERS")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("Test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", is("Testing")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.beginDate", is("3899-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is("3899-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.personId", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.relationId", IsNull.nullValue()));
    }

    @Test
    void testGetEventFromRelation() throws Exception {
        EventDto dto = new EventDto();
        dto.id = 30;
        dto.eventType = "OTHERS";
        dto.description = "Test";
        dto.text = "Testing";
        dto.beginDate = new Date(2000, Calendar.JANUARY, 1);
        dto.endDate = new Date(2000, Calendar.JANUARY, 1);
        dto.personId = null;
        dto.relationId = 60;

        Mockito.when(eventService.getEventFromRelation(60, 30)).thenReturn(dto);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/relations/60/events/30"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.eventType", is("OTHERS")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("Test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", is("Testing")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.beginDate", is("3899-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is("3899-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.personId", IsNull.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.relationId", is(60)));

    }

    @Test
    void testGetAllEventsFromPerson() throws Exception {
        EventDto birth = new EventDto();
        birth.id = 30;
        birth.eventType = "BIRTH";
        birth.description = "Day of Birth";
        birth.text = "Hurrah";
        birth.beginDate = new Date(2000, Calendar.JANUARY, 1);
        birth.endDate = new Date(2000, Calendar.JANUARY, 1);
        birth.personId = 11;
        birth.relationId = null;

        EventDto others = new EventDto();
        others.id = 31;
        others.eventType = "OTHERS";
        others.description = "Test";
        others.text = "Testing";
        others.beginDate = new Date(2001, Calendar.JANUARY, 1);
        others.endDate = new Date(2001, Calendar.JANUARY, 1);
        others.personId = 11;
        others.relationId = null;

        List<EventDto> dtos = new ArrayList<>();
        dtos.add(birth);
        dtos.add(others);

        Mockito.when(eventService.getAllEventsFromPerson(11)).thenReturn(dtos);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/11/events"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].eventType", is("BIRTH")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", is("Day of Birth")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", is("Hurrah")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].beginDate", is("3899-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].endDate", is("3899-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].personId", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].relationId", IsNull.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(31)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].eventType", is("OTHERS")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description", is("Test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].text", is("Testing")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].beginDate", is("3900-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].endDate", is("3900-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].personId", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].relationId", IsNull.nullValue()));
    }

    @Test
    void testGetAllRelationEventsFromPerson() throws Exception {
        EventDto marriage = new EventDto();
        marriage.id = 30;
        marriage.eventType = "MARRIAGE";
        marriage.description = "Marriage";
        marriage.text = "Wedding";
        marriage.beginDate = new Date(2000, Calendar.JANUARY, 1);
        marriage.endDate = new Date(2000, Calendar.JANUARY, 1);
        marriage.personId = null;
        marriage.relationId = 60;

        EventDto others = new EventDto();
        others.id = 31;
        others.eventType = "DIVORCE";
        others.description = "Divorce";
        others.text = "Seperation";
        others.beginDate = new Date(2020, Calendar.JANUARY, 1);
        others.endDate = new Date(2020, Calendar.JANUARY, 1);
        others.personId = null;
        others.relationId = 60;

        List<EventDto> dtos = new ArrayList<>();
        dtos.add(marriage);
        dtos.add(others);

        Mockito.when(eventService.getAllRelationEventsFromPerson(11)).thenReturn(dtos);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/11/relationEvents"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].eventType", is("MARRIAGE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", is("Marriage")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", is("Wedding")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].beginDate", is("3899-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].endDate", is("3899-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].personId", IsNull.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].relationId", is(60)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(31)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].eventType", is("DIVORCE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description", is("Divorce")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].text", is("Seperation")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].beginDate", is("3919-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].endDate", is("3919-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].personId", IsNull.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].relationId", is(60)));
    }

    @Test
    void testGetAllEventsFromRelation() throws Exception {
        EventDto marriage = new EventDto();
        marriage.id = 30;
        marriage.eventType = "MARRIAGE";
        marriage.description = "Marriage";
        marriage.text = "Wedding";
        marriage.beginDate = new Date(2000, Calendar.JANUARY, 1);
        marriage.endDate = new Date(2000, Calendar.JANUARY, 1);
        marriage.personId = null;
        marriage.relationId = 60;

        EventDto others = new EventDto();
        others.id = 31;
        others.eventType = "DIVORCE";
        others.description = "Divorce";
        others.text = "Seperation";
        others.beginDate = new Date(2020, Calendar.JANUARY, 1);
        others.endDate = new Date(2020, Calendar.JANUARY, 1);
        others.personId = null;
        others.relationId = 60;

        List<EventDto> dtos = new ArrayList<>();
        dtos.add(marriage);
        dtos.add(others);

        Mockito.when(eventService.getAllEventsFromRelation(60)).thenReturn(dtos);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/relations/60/events"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].eventType", is("MARRIAGE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", is("Marriage")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", is("Wedding")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].beginDate", is("3899-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].endDate", is("3899-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].personId", IsNull.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].relationId", is(60)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(31)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].eventType", is("DIVORCE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description", is("Divorce")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].text", is("Seperation")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].beginDate", is("3919-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].endDate", is("3919-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].personId", IsNull.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].relationId", is(60)));
    }

    @Test
    void testCreateEventFromPerson() throws Exception {
        //EventDto
        String jsonString = """
                        {
                        "eventType": "BIRTH",
                        "description": "Day of Birth",
                        "text": "Hurrah",
                        "beginDate": "2020-01-01",
                        "endDate": "2020-01-01",
                        "personId": 11,
                        "relationId": null
                        }
                """;

        Date date = new Date(2020, Calendar.JANUARY, 1);
        EventDto dto = new EventDto();
        dto.id = 30;
        dto.eventType = "BIRTH";
        dto.description = "Day of Birth";
        dto.text = "Hurrah";
        dto.beginDate = date;
        dto.endDate = date;
        dto.personId = 11;
        dto.relationId = null;

        Mockito.when(eventService.createEventFromPerson(anyInt(), any(EventInputDto.class))).thenReturn(dto);
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/persons/11/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.eventType", is("BIRTH")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("Day of Birth")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", is("Hurrah")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.beginDate", is("3919-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is("3919-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.personId", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.relationId", IsNull.nullValue()));
    }

    @Test
    void testcCeateEventFromRelation() throws Exception {
        //EventDto
        String jsonString = """
                        {
                        "eventType": "MARRIAGE",
                        "description": "Marriage",
                        "text": "Wedding",
                        "beginDate": "2020-01-01",
                        "endDate": "2020-01-01",
                        "personId": null,
                        "relationId": 60
                        }
                """;

        Date date = new Date(2020, Calendar.JANUARY, 1);
        EventDto dto = new EventDto();
        dto.id = 60;
        dto.eventType = "MARRIAGE";
        dto.description = "Marriage";
        dto.text = "Wedding";
        dto.beginDate = date;
        dto.endDate = date;
        dto.personId = null;
        dto.relationId = 60;

        Mockito.when(eventService.createEventFromRelation(anyInt(), any(EventInputDto.class))).thenReturn(dto);
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/relations/11/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(60)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.eventType", is("MARRIAGE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("Marriage")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", is("Wedding")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.beginDate", is("3919-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is("3919-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.personId", IsNull.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.relationId", is(60)));
    }

    @Test
    void testUpdateEventFromPerson() throws Exception {
        //EventDto
        String jsonString = """
                        {
                        "eventType": "BIRTH",
                        "description": "Day of Birth",
                        "text": "Hurrah",
                        "beginDate": "2020-01-01",
                        "endDate": "2020-01-01",
                        "personId": 11,
                        "relationId": null
                        }
                """;

        Date date = new Date(2020, Calendar.JANUARY, 1);
        EventDto dto = new EventDto();
        dto.id = 30;
        dto.eventType = "BIRTH";
        dto.description = "Day of Birth";
        dto.text = "Hurrah";
        dto.beginDate = date;
        dto.endDate = date;
        dto.personId = 11;
        dto.relationId = null;

        Mockito.when(eventService.updateEventFromPerson(anyInt(), anyInt(), any(EventInputDto.class))).thenReturn(dto);
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/persons/11/events/30")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.eventType", is("BIRTH")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("Day of Birth")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", is("Hurrah")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.beginDate", is("3919-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is("3919-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.personId", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.relationId", IsNull.nullValue()));
    }

    @Test
    void testUpdateEventFromRelation() throws Exception {
        //EventDto
        String jsonString = """
                        {
                        "eventType": "MARRIAGE",
                        "description": "Marriage",
                        "text": "Wedding",
                        "beginDate": "2020-01-01",
                        "endDate": "2020-01-01",
                        "personId": null,
                        "relationId": 60
                        }
                """;

        Date date = new Date(2020, Calendar.JANUARY, 1);
        EventDto dto = new EventDto();
        dto.id = 60;
        dto.eventType = "MARRIAGE";
        dto.description = "Marriage";
        dto.text = "Wedding";
        dto.beginDate = date;
        dto.endDate = date;
        dto.personId = null;
        dto.relationId = 60;

        Mockito.when(eventService.updateEventFromRelation(anyInt(), anyInt(), any(EventInputDto.class))).thenReturn(dto);
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/relations/60/events/30")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(60)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.eventType", is("MARRIAGE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("Marriage")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", is("Wedding")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.beginDate", is("3919-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate", is("3919-12-31T23:00:00.000+00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.personId", IsNull.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.relationId", is(60)));
    }

    @Test
    void testDeleteEventFromPerson() throws Exception {

        eventService.deleteEventFromPerson(11, 30);

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/persons/11/events/30"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    void testDeleteEventFromRelation() throws Exception {

        eventService.deleteEventFromRelation(60, 30);

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/relations/60/events/30"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

}
