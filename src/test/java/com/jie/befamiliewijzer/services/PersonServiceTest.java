package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.ChildPersonDto;
import com.jie.befamiliewijzer.dtos.PersonDto;
import com.jie.befamiliewijzer.dtos.PersonInputDto;
import com.jie.befamiliewijzer.dtos.RelationSpouseDto;
import com.jie.befamiliewijzer.exceptions.UnprocessableEntityException;
import com.jie.befamiliewijzer.models.Child;
import com.jie.befamiliewijzer.models.Person;
import com.jie.befamiliewijzer.models.Relation;
import com.jie.befamiliewijzer.repositories.ChildRepository;
import com.jie.befamiliewijzer.repositories.PersonRepository;
import com.jie.befamiliewijzer.repositories.RelationRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Mock
    PersonRepository personRepository;
    @Mock
    RelationRepository relationRepository;
    @Mock
    RelationService relationService;
    @Mock
    ChildService childService;
    @Mock
    ChildRepository childRepository;

    @InjectMocks
    PersonService personService;

    @Test
    void TestGetCorrectPerson() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");
        Optional<Person> personOptional = Optional.of(john);
        when(personRepository.findById(anyInt())).thenReturn(personOptional);

        PersonDto dto = personService.getPerson(11);

        assertEquals(11, dto.id);
        assertEquals("John", dto.givenNames);
        assertEquals("Doe", dto.surname);
        assertEquals("M", dto.sex);
    }

    @Test
    void TestGetAllPersons() {
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

        Person ronald = new Person();
        ronald.setId(13);
        ronald.setGivenNames("Ronald");
        ronald.setSurname("Roe");
        ronald.setSex("M");

        List<Person> persons = new ArrayList<>();
        persons.add(john);
        persons.add(jane);
        persons.add(ronald);
        when(personRepository.findAll()).thenReturn(persons);

        List<PersonDto> list = personService.getAllPersons();
        PersonDto p0 = list.get(0);
        PersonDto p1 = list.get(1);
        PersonDto p2 = list.get(2);
        assertEquals(11, p0.id);
        assertEquals("John", p0.givenNames);
        assertEquals("Doe", p0.surname);
        assertEquals("M", p0.sex);
        assertEquals(12, p1.id);
        assertEquals("Jane", p1.givenNames);
        assertEquals("Doe", p1.surname);
        assertEquals("F", p1.sex);
        assertEquals(13, p2.id);
        assertEquals("Ronald", p2.givenNames);
        assertEquals("Roe", p2.surname);
        assertEquals("M", p2.sex);
    }

    @Test
    void testGetAllPersonsByName() {
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

        List<Person> persons = new ArrayList<>();
        persons.add(john);
        persons.add(jane);
        when(personRepository.
                findAllByGivenNamesIsContainingIgnoreCaseOrSurnameContainingIgnoreCase(anyString(), anyString())).thenReturn(persons);

        List<PersonDto> list = personService.getAllPersonsByName("Doe");

        PersonDto p0 = list.get(0);
        PersonDto p1 = list.get(1);
        assertEquals(11, p0.id);
        assertEquals("John", p0.givenNames);
        assertEquals("Doe", p0.surname);
        assertEquals("M", p0.sex);
        assertEquals(12, p1.id);
        assertEquals("Jane", p1.givenNames);
        assertEquals("Doe", p1.surname);
        assertEquals("F", p1.sex);
    }

    @Test
    void testGetAllPersonsContainsGivenNamesAndSurname() {
        Person jane = new Person();
        jane.setId(12);
        jane.setGivenNames("Jane");
        jane.setSurname("Doe");
        jane.setSex("F");
        List<Person> persons = new ArrayList<>();
        persons.add(jane);
        when(personRepository
                .findAllByGivenNamesIsContainingIgnoreCaseAndSurnameIsContainingIgnoreCaseOrderByGivenNames(anyString(), anyString())).thenReturn(persons);

        List<PersonDto> list = personService.getAllPersonsContainsGivenNamesAndSurname("Jane", "Doe");
        PersonDto p0 = list.get(0);
        assertEquals(12, p0.id);
        assertEquals("Jane", p0.givenNames);
        assertEquals("Doe", p0.surname);
        assertEquals("F", p0.sex);
    }

    @Test
    void testGetAllPersonsContainsGivenNames() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");
        List<Person> persons = new ArrayList<>();
        persons.add(john);
        when(personRepository
                .findAllByGivenNamesIsContainingIgnoreCaseOrderByGivenNames(anyString())).thenReturn(persons);

        List<PersonDto> list = personService.getAllPersonsContainsGivenNames("John");

        PersonDto p0 = list.get(0);
        assertEquals(11, p0.id);
        assertEquals("John", p0.givenNames);
        assertEquals("Doe", p0.surname);
        assertEquals("M", p0.sex);
    }

    @Test
    void testGetAllPersonsContainsSurname() {
        Person ronald = new Person();
        ronald.setId(13);
        ronald.setGivenNames("Ronald");
        ronald.setSurname("Roe");
        ronald.setSex("M");
        List<Person> persons = new ArrayList<>();
        persons.add(ronald);
        when(personRepository
                .findAllBySurnameContainingIgnoreCaseOrderBySurname(anyString())).thenReturn(persons);

        List<PersonDto> list = personService.getAllPersonsContainsSurname("Roe");

        PersonDto p0 = list.get(0);
        assertEquals("Ronald", p0.givenNames);
        assertEquals("Roe", p0.surname);
        assertEquals("M", p0.sex);
    }

    @Test
    void testGetAllChildrenFromPerson() {
        //Arrange
        RelationSpouseDto jane = new RelationSpouseDto();
        jane.id = 1;
        jane.personId = 11;
        jane.spouseId = 12;
        jane.spouseGivenNames = "Jane";
        jane.spouseSurname = "Doe";
        jane.spouseSex = "F";
        List<RelationSpouseDto> spouses = new ArrayList<>();
        spouses.add(jane);

        ChildPersonDto johnny = new ChildPersonDto();
        johnny.id = 3;
        johnny.personId = 13;
        johnny.relationId = 1;
        johnny.givenNames = "Johnny";
        johnny.surname = "Doe";
        johnny.sex = "M";
        List<ChildPersonDto> childPersonDtos = new ArrayList<>();
        childPersonDtos.add(johnny);

        when(relationService
                .getAllRelationsFromPersonId(anyInt())).thenReturn(spouses);
        when(childService.getAlChildrenFromRelation(anyInt())).thenReturn(childPersonDtos);

        //Act
        List<PersonDto> list = personService.getAllChildrenFromPerson(1);

        //Assert
        PersonDto dto = list.get(0);
        assertEquals(13, dto.id);
        assertEquals("Johnny", dto.givenNames);
        assertEquals("Doe", dto.surname);
        assertEquals("M", dto.sex);
    }

    @Test
    void TestGetParentsPerson() {
        //Arrange
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

        Relation parents = new Relation();
        parents.setId(1);
        parents.setPerson(john);
        parents.setSpouse(jane);

        Child johnny = new Child();
        johnny.setId(13);
        johnny.setRelation(parents);
        Optional<Child> childOptional = Optional.of(johnny);

        when(childRepository.findByPersonId(anyInt())).thenReturn(childOptional);

        //Act
        List<PersonDto> list = personService.getParentsPerson(13);

        //Assert
        PersonDto p0 = list.get(0);
        PersonDto p1 = list.get(1);
        assertEquals("John", p0.givenNames);
        assertEquals("Doe", p0.surname);
        assertEquals("M", p0.sex);
        assertEquals("Jane", p1.givenNames);
        assertEquals("Doe", p1.surname);
        assertEquals("F", p1.sex);
    }

    @Test
    void testCreatePerson() {
        PersonInputDto inputDto = new PersonInputDto();
        inputDto.givenNames = "John";
        inputDto.surname = "Doe";
        inputDto.sex = "M";

        Person person = new Person();
        person.setId(1);
        person.setGivenNames("John");
        person.setSurname("Doe");
        person.setSex("M");

        when(personRepository.existsByGivenNamesAndSurnameAndSex(anyString(), anyString(), anyString()))
                .thenReturn(false);

        PersonDto dto = personService.createPerson(inputDto);

        assertEquals("John", dto.givenNames);
        assertEquals("Doe", dto.surname);
        assertEquals("M", dto.sex);
    }

    @Test
    void testCreatePersonWrongSexType() {
        PersonInputDto inputDto = new PersonInputDto();
        inputDto.givenNames = "John";
        inputDto.surname = "Doe";
        inputDto.sex = "H";

        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class, () -> {
            PersonDto dto = personService.createPerson(inputDto);
        });

        Assertions.assertEquals("The sex type could not be processed", thrown.getMessage());

    }

    @Test
    void testCreatePersonAlreadyExists() {
        PersonInputDto inputDto = new PersonInputDto();
        inputDto.givenNames = "John";
        inputDto.surname = "Doe";
        inputDto.sex = "M";

        when(personRepository.existsByGivenNamesAndSurnameAndSex(anyString(), anyString(), anyString())).thenReturn(true);

        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class, () -> {
            PersonDto dto = personService.createPerson(inputDto);
        });

        Assertions.assertEquals("there already exists such a person with the same given names and surname and gender", thrown.getMessage());

    }

    @Test
    void testUpdatePerson() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        PersonInputDto inputDto = new PersonInputDto();
        inputDto.givenNames = "John";
        inputDto.surname = "Doe";
        inputDto.sex = "M";

        when(personRepository.findById(anyInt())).thenReturn(Optional.of(john));
        when(personRepository.save(any(Person.class))).thenReturn(john);

        PersonDto dto = personService.updatePerson(11, inputDto);

        assertEquals(11, dto.id);
        assertEquals("John", dto.givenNames);
        assertEquals("Doe", dto.surname);
        assertEquals("M", dto.sex);
    }

    @Test
    void testUpdatePersonInvalidSexType() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        PersonInputDto inputDto = new PersonInputDto();
        inputDto.givenNames = "John";
        inputDto.surname = "Doe";
        inputDto.sex = "O";

        when(personRepository.findById(anyInt())).thenReturn(Optional.of(john));

        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class, () -> {
            PersonDto dto = personService.updatePerson(11, inputDto);
        });
        Assertions.assertEquals("The sex type could not be processed", thrown.getMessage());
    }

    @Test
    void testDeletePerson() {
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
        kid.setId(20);
        kid.setPerson(johnny);
        Set<Child> kids = new HashSet<>();
        kids.add(kid);

        Relation relation = new Relation();
        relation.setId(10);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setChildren(kids);

        List<Relation> relations = new ArrayList<>();
        relations.add(relation);

        Relation relationResult = new Relation();
        relationResult.setId(10);
        relationResult.setPerson(jane);
        relationResult.setSpouse(null);
        relationResult.setChildren(kids);

        when(personRepository.existsById(anyInt())).thenReturn(true);
        when(relationRepository.findAllByPersonIdOrSpouseId(anyInt(), anyInt())).thenReturn(relations);
        when(relationRepository.save(any(Relation.class))).thenReturn(relationResult);

        personService.deletePerson(11);

        Mockito.verify(personRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void testDeletePersonSpouse() {
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
        kid.setId(20);
        kid.setPerson(johnny);
        Set<Child> kids = new HashSet<>();
        kids.add(kid);

        Relation relation = new Relation();
        relation.setId(10);
        relation.setPerson(john);
        relation.setSpouse(jane);
        relation.setChildren(kids);

        List<Relation> relations = new ArrayList<>();
        relations.add(relation);

        Relation relationResult = new Relation();
        relationResult.setId(10);
        relationResult.setPerson(john);
        relationResult.setSpouse(null);
        relationResult.setChildren(kids);

        when(personRepository.existsById(anyInt())).thenReturn(true);
        when(relationRepository.findAllByPersonIdOrSpouseId(anyInt(), anyInt())).thenReturn(relations);
        when(relationRepository.save(any(Relation.class))).thenReturn(relationResult);

        personService.deletePerson(12);

        Mockito.verify(personRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void testDeletePersonSingleParent() {
        Person john = new Person();
        john.setId(11);
        john.setGivenNames("John");
        john.setSurname("Doe");
        john.setSex("M");

        Person johnny = new Person();
        johnny.setId(13);
        johnny.setGivenNames("Johnny");
        johnny.setSurname("Doe");
        johnny.setSex("M");

        Child kid = new Child();
        kid.setId(20);
        kid.setPerson(johnny);
        Set<Child> kids = new HashSet<>();
        kids.add(kid);

        Relation relation = new Relation();
        relation.setId(10);
        relation.setPerson(john);
        relation.setSpouse(null);
        relation.setChildren(kids);

        List<Relation> relations = new ArrayList<>();
        relations.add(relation);

        Relation relationResult = new Relation();
        relationResult.setId(10);
        relationResult.setPerson(null);
        relationResult.setSpouse(null);
        relationResult.setChildren(kids);

        when(personRepository.existsById(anyInt())).thenReturn(true);
        when(relationRepository.findAllByPersonIdOrSpouseId(anyInt(), anyInt())).thenReturn(relations);
        when(relationRepository.save(any(Relation.class))).thenReturn(relationResult);
        when(childRepository.findByPersonId(anyInt())).thenReturn(Optional.of(kid));

        personService.deletePerson(11);

        Mockito.verify(relationService, times(1)).deleteRelation(anyInt());
        Mockito.verify(childService, times(1)).deleteChildFromRelation(anyInt(),anyInt());
        Mockito.verify(personRepository, times(1)).deleteById(anyInt());
    }

}