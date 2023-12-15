package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.EventDto;
import com.jie.befamiliewijzer.dtos.EventInputDto;
import com.jie.befamiliewijzer.dtos.EventMonthDayDto;
import com.jie.befamiliewijzer.dtos.EventTypeDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.exceptions.UnprocessableEntityException;
import com.jie.befamiliewijzer.models.*;
import com.jie.befamiliewijzer.repositories.EventRepository;
import com.jie.befamiliewijzer.repositories.PersonRepository;
import com.jie.befamiliewijzer.repositories.RelationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
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

        LocalDate date = LocalDate.of(2023, 5, 1);

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
        assertNull(dto.relationId);
    }

    @Test
    void testGetEventFromRelation() {
        Multimedia multimedia = new Multimedia();
        multimedia.setId(44);
        multimedia.setDescription("Test media");
        multimedia.setFilename("Test.pdf");
        List<Multimedia> multimediaList = new ArrayList<>();
        multimediaList.add(multimedia);

        LocalDate date = LocalDate.of(2023, 5, 1);

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
        assertNull(dto.personId);
        assertEquals(60, dto.relationId);
    }

    @Test
    void testGetAllEventsFromPerson() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        LocalDate date = LocalDate.of(2023, 5, 1);

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
        assertNull(dto.relationId);
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

        LocalDate date = LocalDate.of(2023, 5, 1);

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
        assertNull(dto.personId);
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
        LocalDate date = LocalDate.of(2023, 5, 1);

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
        relation.setChildren(new HashSet<>());

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
        assertNull(dto.personId);
        assertEquals(60, dto.relationId);

    }

    @Test
    void testGetAllCelebrateEventsOnMonthDay() {
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

        Relation relationJohnJane = new Relation();
        relationJohnJane.setId(60);
        relationJohnJane.setPerson(john);
        relationJohnJane.setSpouse(jane);
        relationJohnJane.setChildren(new HashSet<>());
        relationJohnJane.setEvents(null);

        LocalDate dateMarriageJohnJane = LocalDate.of(2000, 5, 1);
        Event marriageJohnJane = new Event();
        marriageJohnJane.setId(21);
        marriageJohnJane.setEventType("MARRIAGE");
        marriageJohnJane.setDescription("Wedding");
        marriageJohnJane.setText("");
        marriageJohnJane.setBeginDate(dateMarriageJohnJane);
        marriageJohnJane.setEndDate(dateMarriageJohnJane);
        marriageJohnJane.setPerson(null);
        marriageJohnJane.setRelation(relationJohnJane);

        LocalDate dateDivorceJohnJane = LocalDate.of(2010, 11, 1);
        Event divorce = new Event();
        divorce.setId(22);
        divorce.setEventType("DIVORCE");
        divorce.setDescription("Seperation");
        divorce.setText("");
        divorce.setBeginDate(dateDivorceJohnJane);
        divorce.setEndDate(dateDivorceJohnJane);
        divorce.setPerson(null);
        divorce.setRelation(relationJohnJane);

        LocalDate datebirthJohn = LocalDate.of(1980, 5, 1);
        Event birthJohn = new Event();
        birthJohn.setId(23);
        birthJohn.setEventType("BIRTH");
        birthJohn.setDescription("Birthday");
        birthJohn.setText("");
        birthJohn.setBeginDate(datebirthJohn);
        birthJohn.setEndDate(datebirthJohn);
        birthJohn.setPerson(john);
        birthJohn.setRelation(null);

        LocalDate dateDeathJohn = LocalDate.of(2010, 4, 1);
        Event deathJohn = new Event();
        deathJohn.setId(24);
        deathJohn.setEventType("BIRTH");
        deathJohn.setDescription("Birthday");
        deathJohn.setText("");
        deathJohn.setBeginDate(dateDeathJohn);
        deathJohn.setEndDate(dateDeathJohn);
        deathJohn.setPerson(john);
        deathJohn.setRelation(null);

        Person frank = new Person();
        frank.setId(13);
        frank.setGivenNames("Frank");
        frank.setSurname("Foe");
        frank.setSex("M");

        LocalDate dateBirthFrank = LocalDate.of(1981, 5, 1);
        Event birthFrank = new Event();
        birthFrank.setId(23);
        birthFrank.setEventType("BIRTH");
        birthFrank.setDescription("Birthday");
        birthFrank.setText("");
        birthFrank.setBeginDate(dateBirthFrank);
        birthFrank.setEndDate(dateBirthFrank);
        birthFrank.setPerson(frank);
        birthFrank.setRelation(null);

        Relation relationFankJane = new Relation();
        relationFankJane.setId(61);
        relationFankJane.setPerson(frank);
        relationFankJane.setSpouse(jane);
        relationFankJane.setChildren(new HashSet<>());
        relationFankJane.setEvents(null);

        LocalDate dateMarriageFrankJane = LocalDate.of(2004, 5, 1);
        Event marriageFrankJane = new Event();
        marriageFrankJane.setId(24);
        marriageFrankJane.setEventType("MARRIAGE");
        marriageFrankJane.setDescription("Wedding");
        marriageFrankJane.setText("Frank - Jane");
        marriageFrankJane.setBeginDate(dateMarriageFrankJane);
        marriageFrankJane.setEndDate(dateMarriageFrankJane);
        marriageFrankJane.setPerson(null);
        marriageFrankJane.setRelation(relationFankJane);

        EventMonthDayDto wedding = new EventMonthDayDto();
        wedding.id = 61;
        wedding.eventType = "MARRIAGE";
        wedding.description = "Wedding";
        wedding.text = "Frank - Jane";
        wedding.date = dateMarriageFrankJane;
        wedding.personId = 13;
        wedding.relationId = 61;
        wedding.givenNames = "Frank";
        wedding.surname = "Foe";
        wedding.spouseGivenNames = "Jane";
        wedding.spouseSurname = "Doe";

        EventMonthDayDto birthdayFrank = new EventMonthDayDto();
        birthdayFrank.id = 21;
        birthdayFrank.eventType = "BIRTH";
        birthdayFrank.description = "Birthday";
        birthdayFrank.text = "";
        birthdayFrank.date = dateBirthFrank;
        birthdayFrank.personId = 13;
        birthdayFrank.relationId = null;
        birthdayFrank.givenNames = "Frank";
        birthdayFrank.surname = "Foe";
        birthdayFrank.spouseGivenNames = "";
        birthdayFrank.spouseSurname = "";

        List<EventMonthDayDto> list = new ArrayList<>();
        list.add(wedding);
        list.add(birthdayFrank);

        List<Event> events = new ArrayList<>();
        events.add(marriageJohnJane);
        events.add(birthJohn);
        events.add(birthFrank);
        events.add(marriageFrankJane);
        when(eventRepository.findAllEventsOnMonthDay(anyInt(), anyInt())).thenReturn(events);
        when(eventRepository.findEventByRelationIdAndEventType(60, "DIVORCE")).thenReturn(Optional.of(divorce));
        when(eventRepository.findByPersonIdAndEventType(11, "DEATH")).thenReturn(Optional.of(deathJohn));
        when(relationRepository.findById(61)).thenReturn(Optional.of(relationFankJane));
        when(personRepository.findById(13)).thenReturn(Optional.of(frank));
        when(personRepository.findById(12)).thenReturn(Optional.of(jane));

        List<EventMonthDayDto> dtos = eventService.getAllCelebrateEventsOnMonthDay(5, 1);
        EventMonthDayDto dto0 = dtos.get(0);
        EventMonthDayDto dto1 = dtos.get(1);

        assertEquals(23, dto0.id);
        assertEquals("BIRTH", dto0.eventType);
        assertEquals("Birthday", dto0.description);
        assertEquals("", dto0.text);
        assertEquals(13, dto0.personId);
        assertEquals("Frank", dto0.givenNames);
        assertEquals("Foe", dto0.surname);

        assertEquals(24, dto1.id);
        assertEquals("MARRIAGE", dto1.eventType);
        assertEquals("Wedding", dto1.description);
        assertEquals(61, dto1.relationId);
        assertEquals("Frank", dto1.givenNames);
        assertEquals("Foe", dto1.surname);
        assertEquals("Jane", dto1.spouseGivenNames);
        assertEquals("Doe", dto1.spouseSurname);

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

        LocalDate date = LocalDate.of(2023, 5, 1);

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
        assertNull(dto.relationId);
    }

    @Test
    void testCreateEventFromRelation() {
        LocalDate date = LocalDate.of(2023, 5, 1);

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
        assertNull(dto.personId);
        assertEquals(60, dto.relationId);

    }

    @Test
    void testCreateEventFromRelationInvalidEventType() {
        LocalDate date = LocalDate.of(2023, 5, 1);

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
        LocalDate date = LocalDate.of(2023, 5, 1);

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
        assertNull(dto.relationId);

    }

    @Test
    void testCreateEventFromPersonInvalidEventType() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        LocalDate dateOfDeath = LocalDate.of(1999, 1, 1);
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
        inputDto.beginDate = LocalDate.of(2000, 1, 1);
        inputDto.endDate = LocalDate.of(1999, 1, 1);
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
        birth.setBeginDate(LocalDate.of(2000, 1, 1));
        birth.setEndDate(LocalDate.of(2000, 1, 1));
        birth.setPerson(john);

        EventInputDto inputDto = new EventInputDto();
        inputDto.eventType = "BIRTH";
        inputDto.description = "Before";
        inputDto.beginDate = LocalDate.of(1999, 12, 1);
        inputDto.endDate = LocalDate.of(1999, 12, 1);
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
        birth.setBeginDate(LocalDate.of(2000, 1, 1));
        birth.setEndDate(LocalDate.of(2000, 1, 1));
        birth.setPerson(john);

        Event death = new Event();
        death.setId(31);
        death.setEventType("DEATH");
        death.setDescription("Date of Death");
        death.setBeginDate(LocalDate.of(2020, 12, 1));
        death.setEndDate(LocalDate.of(2020, 12, 1));
        death.setPerson(john);

        LocalDate newDate = LocalDate.of(2020, 11, 30);
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
        assertNull(dto.relationId);

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
        birth.setBeginDate(LocalDate.of(2000, 1, 1));
        birth.setEndDate(LocalDate.of(2000, 1, 1));
        birth.setPerson(john);

        Event death = new Event();
        death.setId(31);
        death.setEventType("DEATH");
        death.setDescription("Date of Death");
        death.setBeginDate(LocalDate.of(2020, 12, 1));
        death.setEndDate(LocalDate.of(2020, 12, 1));
        death.setPerson(john);

        LocalDate newDate = LocalDate.of(1998, 11, 30);
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
        birth.setBeginDate(LocalDate.of(2000, 1, 1));
        birth.setEndDate(LocalDate.of(2000, 1, 1));
        birth.setPerson(john);

        Event others = new Event();
        others.setId(31);
        others.setEventType("OTHERS");
        others.setDescription("Other event");
        others.setBeginDate(LocalDate.of(2020, 12, 1));
        others.setEndDate(LocalDate.of(2020, 12, 1));
        others.setPerson(john);

        LocalDate newDate = LocalDate.of(2018, 11, 30);
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

        Assertions.assertEquals("The date of death occurs before previous events", thrown.getMessage());

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
        birth.setBeginDate(LocalDate.of(2000, 1, 1));
        birth.setEndDate(LocalDate.of(2000, 1, 1));
        birth.setPerson(john);

        Event death = new Event();
        death.setId(31);
        death.setEventType("DEATH");
        death.setDescription("Date of Death");
        death.setBeginDate(LocalDate.of(2018, 12, 1));
        death.setEndDate(LocalDate.of(2018, 12, 1));
        death.setPerson(john);

        LocalDate newDate = LocalDate.of(2020, 11, 30);
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

        Assertions.assertEquals("The event occurs after the date of death", thrown.getMessage());

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
        others.setBeginDate(LocalDate.of(2000, 1, 1));
        others.setEndDate(LocalDate.of(2000, 1, 1));
        others.setPerson(john);

        Event AnOther = new Event();
        AnOther.setId(30);
        AnOther.setEventType("OTHERS");
        AnOther.setDescription("Other event");
        AnOther.setBeginDate(LocalDate.of(2005, 1, 1));
        AnOther.setEndDate(LocalDate.of(2005, 1, 1));
        AnOther.setPerson(john);


        LocalDate newDate = LocalDate.of(2006, 11, 30);
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

        Assertions.assertEquals("The date of birth occurs after previous events", thrown.getMessage());

    }

    @Test
    void testUpdateEventFromRelation() {
        LocalDate date = LocalDate.of(2023, 5, 1);

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
        relation.setChildren(new HashSet<>());
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
        assertNull(dto.personId);
        assertEquals(60, dto.relationId);

    }

    @Test
    void testUpdateEventFromRelationInvalidEventType() {
        LocalDate date = LocalDate.of(2023, 5, 1);

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
        LocalDate date = LocalDate.of(2023, 5, 1);

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
        LocalDate date = LocalDate.of(2023, 5, 1);

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