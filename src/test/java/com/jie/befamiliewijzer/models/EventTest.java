package com.jie.befamiliewijzer.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.jie.befamiliewijzer.models.Event.*;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldKeepEvent() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Multimedia testMedia = new Multimedia();
        testMedia.setId(1);
        testMedia.setDescription("Test");
        testMedia.setFilename("Test.pdf");
        List<Multimedia> testMediaList = new ArrayList<>();
        testMediaList.add(testMedia);

        Date begin = new Date(2023, 5, 1);
        Date end = new Date(2023, 5, 1);
        Event event = new Event();
        event.setId(10);
        event.setEventType("OTHERS");
        event.setDescription("Test");
        event.setText("Testing");
        event.setBeginDate(begin);
        event.setEndDate(end);
        event.setPerson(john);
        event.setRelation(null);
        event.setMultimedias(testMediaList);

        Set<String> eventTypes = new HashSet<>(Arrays.asList("BIRTH", "DEATH", "MIGRATION", "CELEBRATION", "OTHERS", "MARRIAGE", "DIVORCE"));

        Integer id = event.getId();
        String eventType = event.getEventType();
        String description = event.getDescription();
        String text = event.getText();
        Date beginDate = event.getBeginDate();
        Date endDate = event.getEndDate();
        Person person = event.getPerson();
        Relation relation = event.getRelation();
        List<Multimedia> multimediaList = event.getMultimedias();

        Set<String> personEventTypes = getEventTypes();

        assertEquals(10, id);
        assertEquals("OTHERS", eventType);
        assertEquals("Test", description);
        assertEquals("Testing", text);
        assertEquals(begin, beginDate);
        assertEquals(end, endDate);
        assertEquals(john, person);
        assertEquals(null, relation);
        assertEquals(testMediaList, multimediaList);
        assertEquals(eventTypes, personEventTypes);
    }
}