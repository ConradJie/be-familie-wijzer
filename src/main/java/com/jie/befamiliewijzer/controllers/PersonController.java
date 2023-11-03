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
        return ResponseEntity.ok(personService.findAll());
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
