package com.jie.befamiliewijzer.controllers;

import com.jie.befamiliewijzer.dtos.PersonDto;
import com.jie.befamiliewijzer.dtos.PersonInputDto;
import com.jie.befamiliewijzer.services.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class PersonController {
    final private PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/persons/{id}")
    public ResponseEntity<PersonDto> getPerson(@PathVariable Integer id) {
        return ResponseEntity.ok(personService.getPerson(id));
    }

    @GetMapping("/persons")
    public ResponseEntity<List<PersonDto>> getAllpersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @GetMapping("/persons/namecontains/{name}")
    public ResponseEntity<List<PersonDto>> getAllPersonsByName(@PathVariable String name) {
        return ResponseEntity.ok(personService.getAllPersonsByName(name));
    }

    @GetMapping("/persons/contains")
    public ResponseEntity<List<PersonDto>> getAllPersonsContainsGivenNamesSurname(
            @RequestParam(value = "givenNames", required = false) Optional<String> givenNames,
            @RequestParam(value = "surname", required = false) Optional<String> surname) {
        if (givenNames.isPresent() && surname.isPresent()) {
            return ResponseEntity.ok(personService.getAllPersonsContainsGivenNamesAndSurname(givenNames.get(), surname.get()));
        } else if (givenNames.isPresent()) {
            return ResponseEntity.ok(personService.getAllPersonsContainsGivenNames(givenNames.get()));
        } else if (surname.isPresent()) {
            return ResponseEntity.ok(personService.getAllPersonsContainsSurname(surname.get()));
        }
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @GetMapping("/persons/children/{id}")
    public ResponseEntity<List<PersonDto>> getAllChildrenFromPerson(@PathVariable Integer id) {
        return ResponseEntity.ok(personService.getAllChildrenFromPerson(id));
    }

    @GetMapping("/persons/parents/{id}")
    public ResponseEntity<List<PersonDto>> getParentrsFromPerson(@PathVariable Integer id) {
        return ResponseEntity.ok(personService.getParentsPerson(id));
    }

    @GetMapping("/persons/nospouses")
    public ResponseEntity<List<PersonDto>> getPersonsInRelationsWithoutSpouses() {
        return ResponseEntity.ok(personService.getPersonsInRelationsWithoutSpouses());
    }

    @PostMapping("/persons")
    public ResponseEntity<Object> createPerson(@Valid @RequestBody PersonInputDto personInputDto) {
        PersonDto personDto = personService.createPerson(personInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + personDto.id).toUriString());
        return ResponseEntity.created(uri).body(personDto);
    }

    @PutMapping("/persons/{id}")
    public ResponseEntity<Object> updatePerson(@PathVariable Integer id,
                                               @Valid @RequestBody PersonInputDto personInputDto) {
        PersonDto dto = personService.updatePerson(id, personInputDto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/persons/{id}")
    public ResponseEntity<PersonDto> deletePerson(@PathVariable Integer id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

}
