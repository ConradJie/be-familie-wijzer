package com.jie.befamiliewijzer.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RelationTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldKeepRelation() {
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

        Person jannie = new Person();
        jannie.setId(13);
        jannie.setGivenNames("Jannie");
        jannie.setSurname("Doe");
        jannie.setSex("F");
        Child kid = new Child();
        kid.setId(30);
        kid.setPerson(jannie);
        Set<Child> kids = new HashSet<>();
        kids.add(kid);

        Event marriage = new Event();
        marriage.setId(1);
        marriage.setEventType("MARRIAGE");
        marriage.setDescription("Wedding");
        marriage.setText("..");
        marriage.setBeginDate(LocalDate.of(2020, 1, 1));
        marriage.setEndDate(LocalDate.of(2020, 1, 1));
        List<Event> happenings = new ArrayList<>();
        happenings.add(marriage);

        Relation relation = new Relation();
        relation.setId(11);
        relation.setChildren(kids);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setEvents(happenings);
        kid.setRelation(relation);

        Integer id = relation.getId();
        Set<Child> children = relation.getChildren();
        Person person = relation.getPerson();
        Person spouse = relation.getSpouse();
        List<Event> events = relation.getEvents();

        assertEquals(11, id);
        assertEquals(john, person);
        assertEquals(jane, spouse);
        assertEquals(happenings, events);
        assertEquals(kids, children);
    }
}