package com.jie.befamiliewijzer.models;

import jakarta.persistence.Column;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldKeepPerson() {
        Person person = new Person();
        person.setId(11);
        person.setGivenNames("John");
        person.setSurname("Doe");
        person.setSex("M");
        person.setEvents(null);
        person.setChild(null);
        person.setRelations(null);

        Integer id = person.getId();
        String givenNames = person.getGivenNames();
        String surname = person.getSurname();
        String sex = person.getSex();
        Set<Event> eventSet = null;
        Set<Child> childSet = null;
        Set<Relation> relationSet = null;

        assertEquals(11, id);
        assertEquals("John", givenNames);
        assertEquals("Doe", surname);
        assertEquals("M", sex);
        assertEquals(null, eventSet);
        assertEquals(null, childSet);
        assertEquals(null, relationSet);
    }

    @Test
    void shouldKeepPersonEvntsRelationChild() {
        //Arrange
        Event event = new Event();
        event.setId(1);
        event.setEventType("BIRTH");
        event.setDescription("Birthday");
        event.setText("Hurrah");
        event.setBeginDate(LocalDate.of(2023, 11, 1));
        event.setEndDate(LocalDate.of(2023, 11, 1));
        Set<Event> events = new HashSet<>();
        events.add(event);

        Person john = new Person();
        john.setId(10);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Person jane = new Person();
        jane.setId(12);
        jane.setGivenNames("Jane");
        jane.setSurname("Doe");
        jane.setSex("F");

        Relation relation = new Relation();
        relation.setId(1);
        relation.setPerson(john);
        relation.setSpouse(jane);
        Set<Relation> relations = new HashSet<>();
        relations.add(relation);

        Child kid = new Child();
        kid.setId(30);
        kid.setRelation(relation);

        Person person = new Person();
        person.setId(12);
        person.setGivenNames("Jannie");
        person.setSurname("Doe");
        person.setSex("F");
        person.setEvents(events);
        person.setRelations(relations);
        person.setChild(kid);
        kid.setPerson(person);

        //Act
        Integer id = person.getId();
        String givenNames = person.getGivenNames();
        String surname = person.getSurname();
        String sex = person.getSex();
        Set<Event> personEvents = person.getEvents();
        Set<Relation> relationSet = person.getRelations();
        Child child = person.getChild();

        //Asset
        assertEquals(12, id);
        assertEquals("Jannie", givenNames);
        assertEquals("Doe", surname);
        assertEquals("F", sex);
        assertEquals(events, personEvents);
        assertEquals(relations, relationSet);
        assertEquals(kid, child);
    }

    @Test
    void testSexTypes() {
        Set<String> sexTypes = new HashSet<>(Arrays.asList("M", "F", "X"));

        Set<String> set = Person.getSexTypes();

        assertEquals(sexTypes, set);
    }
}