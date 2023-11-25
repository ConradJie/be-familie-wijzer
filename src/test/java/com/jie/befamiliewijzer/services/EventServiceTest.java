package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.EventDto;
import com.jie.befamiliewijzer.dtos.EventInputDto;
import com.jie.befamiliewijzer.dtos.EventTypeDto;
import com.jie.befamiliewijzer.dtos.PersonDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.exceptions.UnprocessableEntityException;
import com.jie.befamiliewijzer.models.Event;
import com.jie.befamiliewijzer.models.Multimedia;
import com.jie.befamiliewijzer.models.Person;
import com.jie.befamiliewijzer.models.Relation;
import com.jie.befamiliewijzer.repositories.EventRepository;
import com.jie.befamiliewijzer.repositories.PersonRepository;
import com.jie.befamiliewijzer.repositories.RelationRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Mock
    EventRepository eventRepository;

    @Mock
    PersonRepository personRepository;

    @Mock
    RelationRepository relationRepository;

    @InjectMocks
    EventService eventService;

    @Test
    void testGetEventTypes() {
        Set<String> eventTypes = new HashSet<>(Arrays.asList("BIRTH", "DEATH", "MIGRATION", "CELEBRATION", "OTHERS", "MARRIAGE", "DIVORCE"));

        List<EventTypeDto> dtos = eventService.getEventTypes();
        Set<String> set = new HashSet<>();
        for (EventTypeDto dto : dtos)
            set.add(dto.eventType);

        assertTrue(eventTypes.size() == set.size() && eventTypes.containsAll(set) && set.containsAll(eventTypes));
    }

    @Test
    void testGetPersonEventTypes() {
        Set<String> eventTypes = new HashSet<>(Arrays.asList("BIRTH", "DEATH", "MIGRATION", "CELEBRATION", "OTHERS"));

        List<EventTypeDto> dtos = eventService.getPersonEventTypes();
        Set<String> set = new HashSet<>();
        for (EventTypeDto dto : dtos)
            set.add(dto.eventType);

        assertTrue(eventTypes.size() == set.size() && eventTypes.containsAll(set) && set.containsAll(eventTypes));
    }

    @Test
    void testGetRelationEventTypes() {
        Set<String> eventTypes = new HashSet<>(Arrays.asList("MARRIAGE", "DIVORCE", "OTHERS"));

        List<EventTypeDto> dtos = eventService.getRelationEventTypes();
        Set<String> set = new HashSet<>();
        for (EventTypeDto dto : dtos)
            set.add(dto.eventType);

        assertTrue(eventTypes.size() == set.size() && eventTypes.containsAll(set) && set.containsAll(eventTypes));
    }

    @Test
    void testGetEventFromPerson() {
        Multimedia multimedia = new Multimedia();
        multimedia.setId(44);
        multimedia.setDescription("Test media");
        multimedia.setFilename("Test.pdf");
        List<Multimedia> multimediaList = new ArrayList<>();
        multimediaList.add(multimedia);

        Date date = new Date(2023, Calendar.MAY, 1);

        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Event event = new Event();
        event.setId(30);
        event.setEventType("OTHERS");
        event.setDescription("Test");
        event.setText("Testing");
        event.setBeginDate(date);
        event.setEndDate(date);
        event.setPerson(john);
        event.setRelation(null);
        event.setMultimedias(multimediaList);

        when(eventRepository.findByPersonIdAndId(anyInt(), anyInt())).thenReturn(Optional.of(event));

        EventDto dto = eventService.getEventFromPerson(11, 30);

        assertEquals(30, dto.id);
        assertEquals("OTHERS", dto.eventType);
        assertEquals("Test", dto.description);
        assertEquals("Testing", dto.text);
        assertEquals(date, dto.beginDate);
        assertEquals(date, dto.endDate);
        assertEquals(11, dto.personId);
        assertEquals(null, dto.relationId);
    }

    @Test
    void testGetEventFromRelation() {
        Multimedia multimedia = new Multimedia();
        multimedia.setId(44);
        multimedia.setDescription("Test media");
        multimedia.setFilename("Test.pdf");
        List<Multimedia> multimediaList = new ArrayList<>();
        multimediaList.add(multimedia);

        Date date = new Date(2023, Calendar.MAY, 1);

        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Person jane = new Person();
        jane.setId(12);
        jane.setGivenNames("Jane");
        jane.setSurname("Doe");
        jane.setSex("F");

        Relation relation = new Relation();
        relation.setId(60);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setChildren(null);
        relation.setEvents(null);

        Event event = new Event();
        event.setId(30);
        event.setEventType("OTHERS");
        event.setDescription("Test");
        event.setText("Testing");
        event.setBeginDate(date);
        event.setEndDate(date);
        event.setPerson(null);
        event.setRelation(relation);
        event.setMultimedias(multimediaList);

        when(eventRepository.findByRelationIdAndId(anyInt(), anyInt())).thenReturn(Optional.of(event));

        EventDto dto = eventService.getEventFromRelation(60, 30);

        assertEquals(30, dto.id);
        assertEquals("OTHERS", dto.eventType);
        assertEquals("Test", dto.description);
        assertEquals("Testing", dto.text);
        assertEquals(date, dto.beginDate);
        assertEquals(date, dto.endDate);
        assertEquals(null, dto.personId);
        assertEquals(60, dto.relationId);
    }

    @Test
    void testGetAllEventsFromPerson() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Date date = new Date(2023, Calendar.MAY, 1);

        Event test = new Event();
        test.setId(30);
        test.setEventType("OTHERS");
        test.setDescription("Test");
        test.setText("Testing");
        test.setBeginDate(date);
        test.setEndDate(date);
        test.setPerson(john);
        test.setRelation(null);
        test.setMultimedias(null);

        List<Event> events = new ArrayList<>();
        events.add(test);

        when(personRepository.existsById(anyInt())).thenReturn(true);
        when(eventRepository.findEventsByPersonIdOrderByBeginDate(anyInt())).thenReturn(events);

        List<EventDto> list = eventService.getAllEventsFromPerson(11);
        EventDto dto = list.get(0);

        assertEquals(30, dto.id);
        assertEquals("OTHERS", dto.eventType);
        assertEquals("Test", dto.description);
        assertEquals("Testing", dto.text);
        assertEquals(date, dto.beginDate);
        assertEquals(date, dto.endDate);
        assertEquals(11, dto.personId);
        assertEquals(null, dto.relationId);
    }

    @Test
    void testGetAllEventsFromPersonNotExisting() {
        when(personRepository.existsById(anyInt())).thenReturn(false);

        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            List<EventDto> dtos = eventService.getAllEventsFromPerson(11);
        });

        Assertions.assertEquals("The requested person could not be found", thrown.getMessage());

    }

    @Test
    void testGetAllRelationEventsFromPerson() {
        Multimedia multimedia = new Multimedia();
        multimedia.setId(44);
        multimedia.setDescription("Test media");
        multimedia.setFilename("Test.pdf");
        List<Multimedia> multimediaList = new ArrayList<>();
        multimediaList.add(multimedia);

        Date date = new Date(2023, Calendar.MAY, 1);

        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Person jane = new Person();
        jane.setId(12);
        jane.setGivenNames("Jane");
        jane.setSurname("Doe");
        jane.setSex("F");

        Relation relation = new Relation();
        relation.setId(60);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setChildren(null);

        List<Relation> relations = new ArrayList<>();
        relations.add(relation);

        Event event = new Event();
        event.setId(30);
        event.setEventType("OTHERS");
        event.setDescription("Test");
        event.setText("Testing");
        event.setBeginDate(date);
        event.setEndDate(date);
        event.setPerson(null);
        event.setRelation(relation);
        event.setMultimedias(multimediaList);

        List<Event> events = new ArrayList<>();
        events.add(event);
        relation.setEvents(events);

        when(personRepository.existsById(anyInt())).thenReturn(true);
        when(relationRepository.findAllByPersonIdOrSpouseId(anyInt(), anyInt())).thenReturn(relations);
        when(eventRepository.findEventsByRelationId(anyInt())).thenReturn(events);

        List<EventDto> dtos = eventService.getAllRelationEventsFromPerson(11);
        EventDto dto = dtos.get(0);

        assertEquals(30, dto.id);
        assertEquals("OTHERS", dto.eventType);
        assertEquals("Test", dto.description);
        assertEquals("Testing", dto.text);
        assertEquals(date, dto.beginDate);
        assertEquals(date, dto.endDate);
        assertEquals(null, dto.personId);
        assertEquals(60, dto.relationId);
    }

    @Test
    void testGetAllRelationEventsFromPersonNonExist() {
        when(personRepository.existsById(anyInt())).thenReturn(false);

        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            List<EventDto> dtos = eventService.getAllRelationEventsFromPerson(60);
        });

        Assertions.assertEquals("The requested person could not be found", thrown.getMessage());

    }

    @Test
    void testGetAllEventsFromRelation() {
        Date date = new Date(2023, Calendar.MAY, 1);

        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Person jane = new Person();
        jane.setId(12);
        jane.setGivenNames("Jane");
        jane.setSurname("Doe");
        jane.setSex("F");

        Relation relation = new Relation();
        relation.setId(60);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setChildren(null);

        List<Relation> relations = new ArrayList<>();
        relations.add(relation);

        Event event = new Event();
        event.setId(30);
        event.setEventType("OTHERS");
        event.setDescription("Test");
        event.setText("Testing");
        event.setBeginDate(date);
        event.setEndDate(date);
        event.setPerson(null);
        event.setRelation(relation);
        event.setMultimedias(null);

        List<Event> events = new ArrayList<>();
        events.add(event);
        relation.setEvents(events);

        when(relationRepository.existsById(anyInt())).thenReturn(true);
        when(eventRepository.findEventsByRelationId(anyInt())).thenReturn(events);

        List<EventDto> dtos = eventService.getAllEventsFromRelation(60);
        EventDto dto = dtos.get(0);

        assertEquals(30, dto.id);
        assertEquals("OTHERS", dto.eventType);
        assertEquals("Test", dto.description);
        assertEquals("Testing", dto.text);
        assertEquals(date, dto.beginDate);
        assertEquals(date, dto.endDate);
        assertEquals(null, dto.personId);
        assertEquals(60, dto.relationId);

    }

    @Test
    void testGetAllEventsFromRelationNotExits() {
        when(relationRepository.existsById(anyInt())).thenReturn(false);

        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            List<EventDto> dtos = eventService.getAllEventsFromRelation(60);
        });

        Assertions.assertEquals("The requested relation could not be found", thrown.getMessage());

    }

    @Test
    void testCreateEventFromPerson() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Date date = new Date(2023, Calendar.MAY, 1);

        Event event = new Event();
        event.setId(30);
        event.setEventType("OTHERS");
        event.setDescription("Test");
        event.setText("Testing");
        event.setPerson(john);
        event.setRelation(null);
        event.setBeginDate(date);
        event.setEndDate(date);

        EventInputDto inputDto = new EventInputDto();
        inputDto.eventType = "OTHERS";
        inputDto.description = "Test";
        inputDto.text = "Testing";
        inputDto.beginDate = date;
        inputDto.endDate = date;
        inputDto.personId = 11;
        inputDto.relationId = null;

        when(personRepository.findById(anyInt())).thenReturn(Optional.of(john));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventDto dto = eventService.createEventFromPerson(11, inputDto);

        assertEquals(30, dto.id);
        assertEquals("OTHERS", dto.eventType);
        assertEquals("Test", dto.description);
        assertEquals("Testing", dto.text);
        assertEquals(date, dto.beginDate);
        assertEquals(date, dto.endDate);
        assertEquals(11, dto.personId);
        assertEquals(null, dto.relationId);
    }

    @Test
    void testCreateEventFromRelation() {
        Date date = new Date(2023, Calendar.MAY, 1);

        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Person jane = new Person();
        jane.setId(12);
        jane.setGivenNames("Jane");
        jane.setSurname("Doe");
        jane.setSex("F");

        Relation relation = new Relation();
        relation.setId(60);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setChildren(null);
        relation.setEvents(null);

        Event event = new Event();
        event.setId(30);
        event.setEventType("OTHERS");
        event.setDescription("Test");
        event.setText("Testing");
        event.setBeginDate(date);
        event.setEndDate(date);
        event.setPerson(null);
        event.setRelation(relation);
        event.setMultimedias(null);

        EventInputDto inputDto = new EventInputDto();
        inputDto.eventType = "OTHERS";
        inputDto.description = "Test";
        inputDto.text = "Testing";
        inputDto.beginDate = date;
        inputDto.endDate = date;
        inputDto.personId = null;
        inputDto.relationId = 60;

        when(relationRepository.findById(anyInt())).thenReturn(Optional.of(relation));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventDto dto = eventService.createEventFromRelation(60, inputDto);

        assertEquals(30, dto.id);
        assertEquals("OTHERS", dto.eventType);
        assertEquals("Test", dto.description);
        assertEquals("Testing", dto.text);
        assertEquals(date, dto.beginDate);
        assertEquals(date, dto.endDate);
        assertEquals(null, dto.personId);
        assertEquals(60, dto.relationId);

    }

    @Test
    void testCreateEventFromRelationInvalidEventType() {
        Date date = new Date(2023, Calendar.MAY, 1);

        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Person jane = new Person();
        jane.setId(12);
        jane.setGivenNames("Jane");
        jane.setSurname("Doe");
        jane.setSex("F");

        Relation relation = new Relation();
        relation.setId(60);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setChildren(null);
        relation.setEvents(null);

        EventInputDto inputDto = new EventInputDto();
        inputDto.eventType = "WRONG";
        inputDto.description = "Test";
        inputDto.text = "Testing";
        inputDto.beginDate = date;
        inputDto.endDate = date;
        inputDto.personId = null;
        inputDto.relationId = 60;

        when(relationRepository.findById(anyInt())).thenReturn(Optional.of(relation));

        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class, () -> {
            EventDto dto = eventService.createEventFromRelation(60, inputDto);
        });

        Assertions.assertEquals("The event type could not be processed", thrown.getMessage());

    }

    @Test
    void testUpdateEventFromPerson() {
        Date date = new Date(2023, Calendar.MAY, 1);

        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Event event = new Event();
        event.setId(30);
        event.setEventType("OTHERS");
        event.setDescription("Test");
        event.setText("Testing");
        event.setBeginDate(date);
        event.setEndDate(date);
        event.setPerson(john);
        event.setRelation(null);
        event.setMultimedias(null);

        Event eventResult = new Event();
        eventResult.setId(30);
        eventResult.setEventType("OTHERS");
        eventResult.setDescription("Test");
        eventResult.setText("Testing");
        eventResult.setBeginDate(date);
        eventResult.setEndDate(date);
        eventResult.setPerson(john);
        eventResult.setRelation(null);
        eventResult.setMultimedias(null);

        EventInputDto inputDto = new EventInputDto();
        inputDto.eventType = "OTHERS";
        inputDto.description = "Test";
        inputDto.text = "Testing";
        inputDto.beginDate = date;
        inputDto.endDate = date;
        inputDto.personId = 11;
        inputDto.relationId = null;

        when(personRepository.findById(anyInt())).thenReturn(Optional.of(john));
        when(eventRepository.findById(anyInt())).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(eventResult);

        EventDto dto = eventService.updateEventFromPerson(60, 30, inputDto);

        assertEquals(30, dto.id);
        assertEquals("OTHERS", dto.eventType);
        assertEquals("Test", dto.description);
        assertEquals("Testing", dto.text);
        assertEquals(date, dto.beginDate);
        assertEquals(date, dto.endDate);
        assertEquals(11, dto.personId);
        assertEquals(null, dto.relationId);

    }

    @Test
    void testCreateEventFromPersonInvalidEventType() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Date dateOfDeath = new Date(1999, Calendar.JANUARY, 1);
        EventInputDto inputDto = new EventInputDto();
        inputDto.eventType = "WRONG";
        inputDto.description = "Before";
        inputDto.beginDate = dateOfDeath;
        inputDto.endDate = dateOfDeath;
        inputDto.personId = 11;

        when(personRepository.findById(anyInt())).thenReturn(Optional.of(john));

        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class, () -> {
            EventDto dto = eventService.createEventFromPerson(11, inputDto);
        });

        Assertions.assertEquals("The event type could not be processed", thrown.getMessage());

    }

    @Test
    void testCreateEventFromPersonInvalidBegibEndDate() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        EventInputDto inputDto = new EventInputDto();
        inputDto.eventType = "BIRTH";
        inputDto.description = "Before";
        inputDto.beginDate = new Date(2000, Calendar.JANUARY, 1);
        inputDto.endDate = new Date(1999, Calendar.JANUARY, 1);
        inputDto.personId = 11;

        when(personRepository.findById(anyInt())).thenReturn(Optional.of(john));

        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class, () -> {
            EventDto dto = eventService.createEventFromPerson(11, inputDto);
        });

        Assertions.assertEquals("The begin date occurred after the end date", thrown.getMessage());

    }

    @Test
    void testCreateEventFromPersonSingleEventAlreadyExist() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Event birth = new Event();
        birth.setId(30);
        birth.setEventType("BIRTH");
        birth.setDescription("Birthday");
        birth.setBeginDate(new Date(2000, Calendar.JANUARY, 1));
        birth.setEndDate(new Date(2000, Calendar.JANUARY, 1));
        birth.setPerson(john);

        EventInputDto inputDto = new EventInputDto();
        inputDto.eventType = "BIRTH";
        inputDto.description = "Before";
        inputDto.beginDate = new Date(1999, Calendar.DECEMBER, 1);
        inputDto.endDate = new Date(1999, Calendar.DECEMBER, 1);
        inputDto.personId = 11;

        when(personRepository.findById(anyInt())).thenReturn(Optional.of(john));
        when(eventRepository.findByPersonIdAndEventType(anyInt(), anyString())).thenReturn(Optional.of(birth));

        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class, () -> {
            EventDto dto = eventService.createEventFromPerson(11, inputDto);
        });

        Assertions.assertEquals("Such an event (BIRTH) already exists", thrown.getMessage());

    }

    @Test
    void testUpdateEventFromPersonBirthDeath() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Event birth = new Event();
        birth.setId(30);
        birth.setEventType("BIRTH");
        birth.setDescription("Birthday");
        birth.setBeginDate(new Date(2000, Calendar.JANUARY, 1));
        birth.setEndDate(new Date(2000, Calendar.JANUARY, 1));
        birth.setPerson(john);

        Event death = new Event();
        death.setId(31);
        death.setEventType("DEATH");
        death.setDescription("Date of Death");
        death.setBeginDate(new Date(2020, Calendar.DECEMBER, 1));
        death.setEndDate(new Date(2020, Calendar.DECEMBER, 1));
        death.setPerson(john);

        Date newDate = new Date(2020, Calendar.NOVEMBER, 30);
        EventInputDto inputDto = new EventInputDto();
        inputDto.eventType = "DEATH";
        inputDto.description = "Date of Death";
        inputDto.beginDate = newDate;
        inputDto.endDate = newDate;

        List<Event> events = new ArrayList<>();
        events.add(birth);
        events.add(death);

        Event eventResult = new Event();
        eventResult.setId(31);
        eventResult.setEventType("DEATH");
        eventResult.setDescription("Date of Death");
        eventResult.setBeginDate(newDate);
        eventResult.setEndDate(newDate);
        eventResult.setPerson(john);

        when(personRepository.findById(anyInt())).thenReturn(Optional.of(john));
        when(eventRepository.findById(anyInt())).thenReturn(Optional.of(death));
        when(eventRepository.findEventsByPersonIdOrderByBeginDate(anyInt())).thenReturn(events);
        when(eventRepository.save(any(Event.class))).thenReturn(eventResult);

        EventDto dto = eventService.updateEventFromPerson(11, 31, inputDto);

        assertEquals(31, dto.id);
        assertEquals("DEATH", dto.eventType);
        assertEquals("Date of Death", dto.description);
        assertEquals(newDate, dto.beginDate);
        assertEquals(newDate, dto.endDate);
        assertEquals(11, dto.personId);
        assertEquals(null, dto.relationId);

    }


    @Test
    void testUpdateEventFromPersonBeforeBirth() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Event birth = new Event();
        birth.setId(30);
        birth.setEventType("BIRTH");
        birth.setDescription("Birthday");
        birth.setBeginDate(new Date(2000, Calendar.JANUARY, 1));
        birth.setEndDate(new Date(2000, Calendar.JANUARY, 1));
        birth.setPerson(john);

        Event death = new Event();
        death.setId(31);
        death.setEventType("DEATH");
        death.setDescription("Date of Death");
        death.setBeginDate(new Date(2020, Calendar.DECEMBER, 1));
        death.setEndDate(new Date(2020, Calendar.DECEMBER, 1));
        death.setPerson(john);

        Date newDate = new Date(1998, Calendar.NOVEMBER, 30);
        EventInputDto inputDto = new EventInputDto();
        inputDto.eventType = "DEATH";
        inputDto.description = "Date of Death";
        inputDto.beginDate = newDate;
        inputDto.endDate = newDate;

        List<Event> events = new ArrayList<>();
        events.add(birth);
        events.add(death);

        when(personRepository.findById(anyInt())).thenReturn(Optional.of(john));
        when(eventRepository.findById(anyInt())).thenReturn(Optional.of(death));
        when(eventRepository.findEventsByPersonIdOrderByBeginDate(anyInt())).thenReturn(events);

        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class, () -> {
            EventDto dto = eventService.updateEventFromPerson(11, 31, inputDto);
        });

        Assertions.assertEquals("The event occurred before the date of birth", thrown.getMessage());

    }


    @Test
    void testCreteEventFromPersonDeathBeforeEvnts() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Event birth = new Event();
        birth.setId(30);
        birth.setEventType("BIRTH");
        birth.setDescription("Birthday");
        birth.setBeginDate(new Date(2000, Calendar.JANUARY, 1));
        birth.setEndDate(new Date(2000, Calendar.JANUARY, 1));
        birth.setPerson(john);

        Event others = new Event();
        others.setId(31);
        others.setEventType("OTHERS");
        others.setDescription("Other event");
        others.setBeginDate(new Date(2020, Calendar.DECEMBER, 1));
        others.setEndDate(new Date(2020, Calendar.DECEMBER, 1));
        others.setPerson(john);

        Date newDate = new Date(2018, Calendar.NOVEMBER, 30);
        EventInputDto inputDto = new EventInputDto();
        inputDto.eventType = "DEATH";
        inputDto.description = "Date of Death";
        inputDto.beginDate = newDate;
        inputDto.endDate = newDate;

        List<Event> events = new ArrayList<>();
        events.add(birth);
        events.add(others);

        when(personRepository.findById(anyInt())).thenReturn(Optional.of(john));
        when(eventRepository.findEventsByPersonIdOrderByBeginDate(anyInt())).thenReturn(events);

        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class, () -> {
            EventDto dto = eventService.createEventFromPerson(11, inputDto);
        });

        Assertions.assertEquals("The date of death occurrs before previous events", thrown.getMessage());

    }

    @Test
    void testCreteEventFromPersonAfterDeath() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Event birth = new Event();
        birth.setId(30);
        birth.setEventType("BIRTH");
        birth.setDescription("Birthday");
        birth.setBeginDate(new Date(2000, Calendar.JANUARY, 1));
        birth.setEndDate(new Date(2000, Calendar.JANUARY, 1));
        birth.setPerson(john);

        Event death = new Event();
        death.setId(31);
        death.setEventType("DEATH");
        death.setDescription("Date of Death");
        death.setBeginDate(new Date(2018, Calendar.DECEMBER, 1));
        death.setEndDate(new Date(2018, Calendar.DECEMBER, 1));
        death.setPerson(john);

        Date newDate = new Date(2020, Calendar.NOVEMBER, 30);
        EventInputDto inputDto = new EventInputDto();
        inputDto.eventType = "OTHERS";
        inputDto.description = "Other event";
        inputDto.beginDate = newDate;
        inputDto.endDate = newDate;

        List<Event> events = new ArrayList<>();
        events.add(birth);
        events.add(death);

        when(personRepository.findById(anyInt())).thenReturn(Optional.of(john));
        when(eventRepository.findEventsByPersonIdOrderByBeginDate(anyInt())).thenReturn(events);

        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class, () -> {
            EventDto dto = eventService.createEventFromPerson(11, inputDto);
        });

        Assertions.assertEquals("The event occurrs after the date of death", thrown.getMessage());

    }

    @Test
    void testCreteEventFromPersonBirthAfterEvent() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Event others = new Event();
        others.setId(30);
        others.setEventType("OTHERS");
        others.setDescription("Other event");
        others.setBeginDate(new Date(2000, Calendar.JANUARY, 1));
        others.setEndDate(new Date(2000, Calendar.JANUARY, 1));
        others.setPerson(john);

        Event AnOther = new Event();
        AnOther.setId(30);
        AnOther.setEventType("OTHERS");
        AnOther.setDescription("Other event");
        AnOther.setBeginDate(new Date(2005, Calendar.JANUARY, 1));
        AnOther.setEndDate(new Date(2005, Calendar.JANUARY, 1));
        AnOther.setPerson(john);


        Date newDate = new Date(2006, Calendar.NOVEMBER, 30);
        EventInputDto inputDto = new EventInputDto();
        inputDto.eventType = "BIRTH";
        inputDto.description = "Date of Birth";
        inputDto.beginDate = newDate;
        inputDto.endDate = newDate;

        List<Event> events = new ArrayList<>();
        events.add(others);
        events.add(AnOther);

        when(personRepository.findById(anyInt())).thenReturn(Optional.of(john));
        when(eventRepository.findEventsByPersonIdOrderByBeginDate(anyInt())).thenReturn(events);

        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class, () -> {
            EventDto dto = eventService.createEventFromPerson(11, inputDto);
        });

        Assertions.assertEquals("The date of birth occurrs after previous events", thrown.getMessage());

    }

    @Test
    void testUpdateEventFromRelation() {
        Date date = new Date(2023, Calendar.MAY, 1);

        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Person jane = new Person();
        jane.setId(12);
        jane.setGivenNames("Jane");
        jane.setSurname("Doe");
        jane.setSex("F");

        Relation relation = new Relation();
        relation.setId(60);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setChildren(null);
        relation.setEvents(null);

        Event event = new Event();
        event.setId(30);
        event.setEventType("OTHERS");
        event.setDescription("Test");
        event.setText("Testing");
        event.setBeginDate(date);
        event.setEndDate(date);
        event.setPerson(null);
        event.setRelation(relation);
        event.setMultimedias(null);

        EventInputDto inputDto = new EventInputDto();
        inputDto.eventType = "OTHERS";
        inputDto.description = "Test";
        inputDto.text = "Testing";
        inputDto.beginDate = date;
        inputDto.endDate = date;
        inputDto.personId = null;
        inputDto.relationId = 60;

        Event eventResult = new Event();
        eventResult.setId(30);
        eventResult.setEventType("OTHERS");
        eventResult.setDescription("Test");
        eventResult.setText("Testing");
        eventResult.setBeginDate(date);
        eventResult.setEndDate(date);
        eventResult.setPerson(null);
        eventResult.setRelation(relation);
        eventResult.setMultimedias(null);

        when(relationRepository.findById(anyInt())).thenReturn(Optional.of(relation));
        when(eventRepository.findById(anyInt())).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(eventResult);

        EventDto dto = eventService.updateEventFromRelation(60, 30, inputDto);

        assertEquals(30, dto.id);
        assertEquals("OTHERS", dto.eventType);
        assertEquals("Test", dto.description);
        assertEquals("Testing", dto.text);
        assertEquals(date, dto.beginDate);
        assertEquals(date, dto.endDate);
        assertEquals(null, dto.personId);
        assertEquals(60, dto.relationId);

    }

    @Test
    void testUpdateEventFromRelationInvalidEventType() {
        Date date = new Date(2023, Calendar.MAY, 1);

        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Person jane = new Person();
        jane.setId(12);
        jane.setGivenNames("Jane");
        jane.setSurname("Doe");
        jane.setSex("F");

        Relation relation = new Relation();
        relation.setId(60);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setChildren(null);
        relation.setEvents(null);

        Event event = new Event();
        event.setId(30);
        event.setEventType("OTHERS");
        event.setDescription("Test");
        event.setText("Testing");
        event.setBeginDate(date);
        event.setEndDate(date);
        event.setPerson(null);
        event.setRelation(relation);

        EventInputDto inputDto = new EventInputDto();
        inputDto.eventType = "WRONG";
        inputDto.description = "Test";
        inputDto.text = "Testing";
        inputDto.beginDate = date;
        inputDto.endDate = date;
        inputDto.personId = null;
        inputDto.relationId = 60;

        when(relationRepository.findById(anyInt())).thenReturn(Optional.of(relation));
        when(eventRepository.findById(anyInt())).thenReturn(Optional.of(event));

        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class, () -> {
            EventDto dto = eventService.updateEventFromRelation(60, 30, inputDto);
        });

        Assertions.assertEquals("The event type could not be processed", thrown.getMessage());

    }

    @Test
    void testDeleteEventFromPerson() {
        Date date = new Date(2023, Calendar.MAY, 1);

        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Event event = new Event();
        event.setId(30);
        event.setEventType("OTHERS");
        event.setDescription("Test");
        event.setText("Testing");
        event.setBeginDate(date);
        event.setEndDate(date);
        event.setPerson(john);
        event.setRelation(null);
        event.setMultimedias(null);

        Event eventResult = new Event();
        eventResult.setId(30);
        eventResult.setEventType("OTHERS");
        eventResult.setDescription("Test");
        eventResult.setText("Testing");
        eventResult.setBeginDate(date);
        eventResult.setEndDate(date);
        eventResult.setPerson(null);
        eventResult.setRelation(null);
        eventResult.setMultimedias(null);

        when(personRepository.existsById(anyInt())).thenReturn(true);
        when(eventRepository.existsById(anyInt())).thenReturn(true);
        when(eventRepository.findById(anyInt())).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(eventResult);

        eventService.deleteEventFromPerson(11, 30);

        Mockito.verify(eventRepository, times(1)).deleteById(anyInt());

    }

    @Test
    void testDeleteEventFromRelation() {
        Date date = new Date(2023, Calendar.MAY, 1);

        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Person jane = new Person();
        jane.setId(12);
        jane.setGivenNames("Jane");
        jane.setSurname("Doe");
        jane.setSex("F");

        Relation relation = new Relation();
        relation.setId(60);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setChildren(null);
        relation.setEvents(null);

        Event event = new Event();
        event.setId(30);
        event.setEventType("OTHERS");
        event.setDescription("Test");
        event.setText("Testing");
        event.setBeginDate(date);
        event.setEndDate(date);
        event.setPerson(null);
        event.setRelation(relation);
        event.setMultimedias(null);

        Event eventResult = new Event();
        eventResult.setId(30);
        eventResult.setEventType("OTHERS");
        eventResult.setDescription("Test");
        eventResult.setText("Testing");
        eventResult.setBeginDate(date);
        eventResult.setEndDate(date);
        eventResult.setPerson(null);
        eventResult.setRelation(null);
        eventResult.setMultimedias(null);

        when(relationRepository.existsById(anyInt())).thenReturn(true);
        when(eventRepository.existsById(anyInt())).thenReturn(true);
        when(eventRepository.findById(anyInt())).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(eventResult);

        eventService.deleteEventFromRelation(60, 30);

        Mockito.verify(eventRepository, times(1)).deleteById(anyInt());

    }
}