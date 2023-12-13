package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.RelationDto;
import com.jie.befamiliewijzer.dtos.RelationInputDto;
import com.jie.befamiliewijzer.dtos.RelationSpouseDto;
import com.jie.befamiliewijzer.exceptions.ResourceAlreadyExistsException;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.models.Child;
import com.jie.befamiliewijzer.models.Event;
import com.jie.befamiliewijzer.models.Person;
import com.jie.befamiliewijzer.models.Relation;
import com.jie.befamiliewijzer.repositories.ChildRepository;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelationServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Mock
    RelationRepository relationRepository;
    @Mock
    PersonRepository personRepository;
    @Mock
    ChildRepository childRepository;
    @InjectMocks
    RelationService relationService;

    @Test
    void testgetRelation() {
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

        Child johnny = new Child();
        johnny.setId(13);
        Set<Child> kids = new HashSet<>();
        kids.add(johnny);

        Event marriage = new Event();
        marriage.setId(10);
        marriage.setEventType("MARRIAGE");
        marriage.setDescription("Wedding");
        marriage.setText("..");
        marriage.setBeginDate(new Date(2020, Calendar.JANUARY, 1));
        marriage.setEndDate(new Date(2020, Calendar.JANUARY, 1));
        List<Event> happenings = new ArrayList<>();
        happenings.add(marriage);

        Relation relation = new Relation();
        relation.setId(10);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setChildren(kids);
        relation.setEvents(happenings);
        johnny.setRelation(relation);
        when(relationRepository.findById(anyInt())).thenReturn(Optional.of(relation));

        RelationDto dto = relationService.getRelation(10);

        assertEquals(10, dto.id);
        assertEquals(11, dto.personId);
        assertEquals(12, dto.spouseId);
    }

    @Test
    void testGetRelationByPersonIdAndSpouseId() {
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
        relation.setId(10);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setEvents(null);
        relation.setChildren(null);

        when(personRepository.existsById(anyInt())).thenReturn(true);
        when(relationRepository.findByPersonIdAndSpouseId(anyInt(), anyInt())).thenReturn(Optional.of(relation));

        RelationDto dto = relationService.getRelationByPersonIdAndSpouseId(11, 12);

        assertEquals(10, dto.id);
        assertEquals(11, dto.personId);
        assertEquals(12, dto.spouseId);
    }

    @Test
    void testGetRelationByPersonIdAndSpouseIdisNull() {
        when(personRepository.existsById(11)).thenReturn(true);
        when(personRepository.existsById(12)).thenReturn(false);
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            relationService.getRelationByPersonIdAndSpouseId(11, 12);
        });
        Assertions.assertEquals("The spouse do not exists", thrown.getMessage());

    }

    @Test
    void testGetRelationByPersonIdisNullAndSpouseId() {
        when(personRepository.existsById(11)).thenReturn(false);
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            relationService.getRelationByPersonIdAndSpouseId(11, 12);
        });
        Assertions.assertEquals("The person do not exists", thrown.getMessage());

    }

    @Test
    void testGetAllRelationsFromPersonId() {
        List<Relation> relations = new ArrayList<>();
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
        relation.setId(10);
        relation.setPerson(john);
        relation.setSpouse(jane);

        relations.add(relation);

        when(relationRepository.findAllByPersonIdOrSpouseId(anyInt(), anyInt())).thenReturn(relations);

        //Act
        List<RelationSpouseDto> dtos = relationService.getAllRelationsFromPersonId(11);

        //Assert
        RelationSpouseDto dto = dtos.get(0);
        assertEquals(10, dto.id);
        assertEquals(11, dto.personId);
        assertEquals(12, dto.spouseId);
        assertEquals("Jane", dto.spouseGivenNames);
        assertEquals("Doe", dto.spouseSurname);
        assertEquals("F", dto.spouseSex);
    }

    @Test
    void testGetAllRelationsFromPersonIdSwitch() {
        List<Relation> relations = new ArrayList<>();
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
        relation.setId(10);
        relation.setPerson(jane);
        relation.setSpouse(john);

        relations.add(relation);

        when(relationRepository.findAllByPersonIdOrSpouseId(anyInt(), anyInt())).thenReturn(relations);

        //Act
        List<RelationSpouseDto> dtos = relationService.getAllRelationsFromPersonId(11);

        //Assert
        RelationSpouseDto dto = dtos.get(0);
        assertEquals(10, dto.id);
        assertEquals(11, dto.personId);
        assertEquals(12, dto.spouseId);
        assertEquals("Jane", dto.spouseGivenNames);
        assertEquals("Doe", dto.spouseSurname);
        assertEquals("F", dto.spouseSex);
    }

    @Test
    void testGetSingleParentFromPersonId() {
        List<Relation> relations = new ArrayList<>();
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Relation relation = new Relation();
        relation.setId(10);
        relation.setPerson(john);
        relation.setSpouse(null);

        relations.add(relation);

        when(relationRepository.findAllByPersonIdOrSpouseId(anyInt(), anyInt())).thenReturn(relations);

        //Act
        List<RelationSpouseDto> dtos = relationService.getAllRelationsFromPersonId(11);

        //Assert
        RelationSpouseDto dto = dtos.get(0);
        assertEquals(10, dto.id);
        assertEquals(11, dto.personId);
        assertNull(dto.spouseId);
        assertEquals("-", dto.spouseGivenNames);
        assertEquals("-", dto.spouseSurname);
        assertEquals("X", dto.spouseSex);
    }

    @Test
    void testGetAllRelations() {
        List<Relation> relations = new ArrayList<>();
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
        relation.setId(10);
        relation.setPerson(john);
        relation.setSpouse(jane);

        relations.add(relation);

        when(relationRepository.findAll()).thenReturn(relations);

        List<RelationDto> list = relationService.getAllRelations();
        RelationDto dto0 = list.get(0);
        assertEquals(10, dto0.id);
        assertEquals(11, dto0.personId);
        assertEquals(12, dto0.spouseId);
    }

    @Test
    void testCreateRelation() {
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
        relation.setId(10);
        relation.setPerson(john);
        relation.setSpouse(jane);

        RelationInputDto inputDto = new RelationInputDto();
        inputDto.personId = 11;
        inputDto.spouseId = 12;

        when(personRepository.existsById(anyInt())).thenReturn(true);
        when(personRepository.findById(11)).thenReturn(Optional.of(john));
        when(personRepository.findById(12)).thenReturn(Optional.of(jane));
        when(relationRepository.existsByPersonIdAndSpouseId(anyInt(), anyInt())).thenReturn(false);
        when(relationRepository.save(any(Relation.class))).thenReturn(relation);

        RelationDto dto = relationService.createRelation(inputDto);

        assertEquals(10, dto.id);
        assertEquals(11, dto.personId);
        assertEquals(12, dto.spouseId);
    }

    @Test
    void testCreateRelationNonExistingPersons() {
        RelationInputDto inputDto = new RelationInputDto();
        inputDto.personId = 11;
        inputDto.spouseId = 11;

        when(personRepository.existsById(anyInt())).thenReturn(false);

        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            RelationDto dto = relationService.createRelation(inputDto);
        });
        Assertions.assertEquals("The person(s) do not exists", thrown.getMessage());

    }

    @Test
    void testCreateRelationSamePerson() {
        RelationInputDto inputDto = new RelationInputDto();
        inputDto.personId = 11;
        inputDto.spouseId = 11;

        when(personRepository.existsById(anyInt())).thenReturn(true);

        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            RelationDto dto = relationService.createRelation(inputDto);
        });
        Assertions.assertEquals("The person and spouse are the same person", thrown.getMessage());

    }

    @Test
    void testCreateRelationAlreadyExists() {
        RelationInputDto inputDto = new RelationInputDto();
        inputDto.personId = 11;
        inputDto.spouseId = 12;

        when(personRepository.existsById(anyInt())).thenReturn(true);
        when(relationRepository.existsByPersonIdAndSpouseId(11,12)).thenReturn(false);
        when(relationRepository.existsByPersonIdAndSpouseId(12,11)).thenReturn(true);
        ResourceAlreadyExistsException thrown = Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> {
            RelationDto dto = relationService.createRelation(inputDto);
        });
        Assertions.assertEquals("The relation already Exists", thrown.getMessage());

    }

    @Test
    void testUpdateRelationSetSpouseNull() {
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
        relation.setId(10);
        relation.setPerson(john);
        relation.setSpouse(jane);

        Relation relationResult = new Relation();
        relationResult.setId(10);
        relationResult.setPerson(john);
        relationResult.setSpouse(null);

        RelationInputDto inputDto = new RelationInputDto();
        inputDto.personId = 11;
        inputDto.spouseId = null;

        when(relationRepository.findById(anyInt())).thenReturn(Optional.of(relation));
        when(personRepository.findById(11)).thenReturn(Optional.of(john));
        when(relationRepository.save(any(Relation.class))).thenReturn(relationResult);

        RelationDto dto = relationService.updateRelation(10, inputDto);

        assertEquals(10, dto.id);
        assertEquals(11, dto.personId);
        assertNull(dto.spouseId);
    }

    @Test
    void testUpdateRelationSetPersonNull() {
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
        relation.setId(10);
        relation.setPerson(john);
        relation.setSpouse(jane);

        Relation relationResult = new Relation();
        relationResult.setId(10);
        relationResult.setPerson(jane);
        relationResult.setSpouse(null);

        RelationInputDto inputDto = new RelationInputDto();
        inputDto.personId = null;
        inputDto.spouseId = 12;

        when(relationRepository.findById(anyInt())).thenReturn(Optional.of(relation));
        when(personRepository.findById(12)).thenReturn(Optional.of(jane));
        when(relationRepository.save(any(Relation.class))).thenReturn(relationResult);

        RelationDto dto = relationService.updateRelation(10, inputDto);

        assertEquals(10, dto.id);
        assertEquals(12, dto.personId);
        assertNull(dto.spouseId);
    }

    @Test
    void testDeleteRelation() {
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

        Person johnny = new Person();
        johnny.setId(13);
        johnny.setGivenNames("Johnny");
        johnny.setSurname("Doe");
        johnny.setSex("M");

        Child kid = new Child();
        kid.setId(13);
        kid.setPerson(johnny);
        Set<Child> kids = new HashSet<>();
        kids.add(kid);

        List<Event> eventList = new ArrayList<>();
        Event marriage = new Event();
        marriage.setId(20);
        marriage.setEventType("MARRIAGE");
        marriage.setDescription("Wedding");
        marriage.setText("..");
        marriage.setBeginDate(new Date(2023, Calendar.JANUARY, 1));
        marriage.setEndDate(new Date(2023, Calendar.JANUARY, 1));

        Relation relation = new Relation();
        relation.setId(10);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setChildren(kids);
        relation.setEvents(eventList);
        marriage.setRelation(relation);

        when(relationRepository.existsById(anyInt())).thenReturn(true);
        when(relationRepository.findById(anyInt())).thenReturn(Optional.of(relation));
        when(childRepository.save(any(Child.class))).thenReturn(kid);

        relationService.deleteRelation(10);

        Mockito.verify(relationRepository, times(1)).deleteById(anyInt());

    }

    @Test
    void testRemovePersonFromRelation() {
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
        relation.setId(10);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setChildren(new HashSet<>());

        Relation relationResult = new Relation();
        relationResult.setId(10);
        relationResult.setPerson(jane);
        relationResult.setSpouse(null);

        when(relationRepository.findById(anyInt())).thenReturn(Optional.of(relation));
        when(relationRepository.save(any(Relation.class))).thenReturn(relationResult);

        relationService.removePersonFromRelation(10, 11);
    }

    @Test
    void testRemoveSpouseFromRelation() {
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
        relation.setId(10);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setChildren(new HashSet<>());

        Relation relationResult = new Relation();
        relationResult.setId(10);
        relationResult.setPerson(john);
        relationResult.setSpouse(null);

        when(relationRepository.findById(anyInt())).thenReturn(Optional.of(relation));
        when(relationRepository.save(any(Relation.class))).thenReturn(relationResult);

        relationService.removePersonFromRelation(10, 12);
    }

    @Test
    void testRemoveSinglePersonFromRelation() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Relation relation = new Relation();
        relation.setId(10);
        relation.setPerson(john);
        relation.setSpouse(null);
        relation.setChildren(new HashSet<>());

        Relation relationResult = new Relation();
        relationResult.setId(10);
        relationResult.setPerson(null);
        relationResult.setSpouse(null);


        when(relationRepository.findById(anyInt())).thenReturn(Optional.of(relation));
        when(relationRepository.save(any(Relation.class))).thenReturn(relationResult);

        when(relationRepository.existsById(anyInt())).thenReturn(true);

        relationService.removePersonFromRelation(10, 11);
        Mockito.verify(relationRepository, times(1)).deleteById(anyInt());
    }

}