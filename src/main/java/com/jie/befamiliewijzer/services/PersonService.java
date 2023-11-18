package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.PersonDto;
import com.jie.befamiliewijzer.dtos.PersonInputDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.exceptions.UnprocessableEntityException;
import com.jie.befamiliewijzer.models.Child;
import com.jie.befamiliewijzer.models.Person;
import com.jie.befamiliewijzer.models.Relation;
import com.jie.befamiliewijzer.repositories.ChildRepository;
import com.jie.befamiliewijzer.repositories.PersonRepository;
import com.jie.befamiliewijzer.repositories.RelationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final RelationRepository relationRepository;
    private final RelationService relationService;
    private final ChildService childService;
    private final ChildRepository childRepository;

    public PersonService(PersonRepository personRepository,
                         RelationRepository relationRepository,
                         RelationService relationService,
                         ChildService childService,
                         ChildRepository childRepository) {
        this.personRepository = personRepository;
        this.relationRepository = relationRepository;
        this.relationService = relationService;
        this.childService = childService;
        this.childRepository = childRepository;
    }

    public PersonDto getPerson(Integer id) {
        Person person = personRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested person could not be found"));
        return transfer(person);
    }

    public List<PersonDto> getAllPersons() {
        return transfer(personRepository.findAll());
    }

    public List<PersonDto> getAllPersonsByName(String name) {
        return transfer(personRepository
                .findAllByGivenNamesIsContainingIgnoreCaseOrSurnameContainingIgnoreCase(name, name));
    }

    public List<PersonDto> getAllPersonsContainsGivenNamesAndSurname(String givenNames, String surname) {
        return transfer(personRepository
                .findAllByGivenNamesIsContainingIgnoreCaseAndSurnameIsContainingIgnoreCaseOrderByGivenNames(givenNames, surname));
    }

    public List<PersonDto> getAllPersonsContainsGivenNames(String givenNames) {
        return transfer(personRepository.findAllByGivenNamesIsContainingIgnoreCaseOrderByGivenNames(givenNames));
    }

    public List<PersonDto> getAllPersonsContainsSurname(String surname) {
        return transfer(personRepository.findAllBySurnameContainingIgnoreCaseOrderBySurname(surname));
    }

    public PersonDto createPerson(PersonInputDto dto) {
        if (!Person.getSexTypes().contains(dto.sex.toUpperCase())) {
            throw new UnprocessableEntityException("The sex type could not be processed");
        }
        if (personRepository.existsByGivenNamesAndSurnameAndSex(dto.givenNames, dto.surname, dto.sex)) {
            throw new UnprocessableEntityException
                    ("there already exists such a person with the same given names and surname and gender");
        }
        Person person = transfer(dto);
        personRepository.save(person);
        return transfer(person);
    }

    public PersonDto updatePerson(Integer id, PersonInputDto dto) {
        Person person = personRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested person could not be found"));
        if (!Person.getSexTypes().contains(dto.sex.toUpperCase())) {
            throw new UnprocessableEntityException("The sex type could not be processed");
        }
        person.setGivenNames(dto.givenNames);
        person.setSurname(dto.surname);
        person.setSex(dto.sex);
        personRepository.save(person);
        return transfer(person);
    }

    public void deletePerson(Integer id) {
        if (personRepository.existsById(id)) {
            //Detach spouses from person before deleting person
            List<Relation> relations = relationRepository.findAllByPersonIdOrSpouseId(id, id);
            for (Relation relation : relations) {
                if (Objects.equals(relation.getPerson().getId(), id)) {
                    relation.setPerson(null);
                } else if (Objects.equals(relation.getSpouse().getId(), id)) {
                    relation.setSpouse(null);
                }
                relationRepository.save(relation);
                if (relation.getPerson() == null && relation.getSpouse() == null) {
                    relationService.deleteRelation(relation.getId());
                }
            }
            Optional<Child> childOptional = childRepository.findByPersonId(id);
            if (childOptional.isPresent()) {
                childService.deleteChildFromRelation
                        (childOptional.get().getRelation().getId(),
                                childOptional.get().getId());
            }
            personRepository.deleteById(id);
        }
    }

    private PersonDto transfer(Person person) {
        PersonDto dto = new PersonDto();
        dto.id = person.getId();
        dto.givenNames = person.getGivenNames();
        dto.surname = person.getSurname();
        dto.sex = person.getSex();
        return dto;
    }

    private Person transfer(PersonInputDto dto) {
        Person person = new Person();
        person.setGivenNames(dto.givenNames);
        person.setSurname(dto.surname);
        person.setSex(dto.sex);
        return person;
    }

    private List<PersonDto> transfer(List<Person> persons) {
        List<PersonDto> dtos = new ArrayList<>();
        for (Person person : persons) {
            dtos.add(transfer(person));
        }
        return dtos;
    }

}
