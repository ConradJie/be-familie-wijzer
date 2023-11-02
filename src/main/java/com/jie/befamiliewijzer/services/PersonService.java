package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.PersonDto;
import com.jie.befamiliewijzer.dtos.PersonInputDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.models.Person;
import com.jie.befamiliewijzer.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonDto findById(Integer id) {
        Optional<Person> personFound = personRepository.findById(id);
        if (personFound.isPresent()) {
            return transfer(personFound.get());
        } else {
            throw new ResourceNotFoundException("The requested person could not be found");
        }
    }

    public List<PersonDto> findAll() {
        return transfer(personRepository.findAll());
    }


    public PersonDto create(PersonInputDto dto) {
        Person person = transfer(dto);
        personRepository.save(person);
        return transfer(person);
    }


    public PersonDto update(Integer id, PersonInputDto dto) {
        Person person = personRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested person could not be found"));
        person.setGivenNames(dto.givenNames);
        person.setSurname(dto.surname);
        person.setSex(dto.sex);
        personRepository.save(person);
        return transfer(person);
    }

    public void delete(Integer id) {
        if (personRepository.existsById(id)) {
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

    private Person transfer(PersonDto dto) {
        Person person = new Person();
        person.setId(dto.id);
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
