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

    PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/persons/{id}")
    public ResponseEntity<PersonDto> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @GetMapping("/persons")
    public ResponseEntity<List<PersonDto>> findAll() {
        return ResponseEntity.ok(personService.findAll());
    }
    @PostMapping("/persons")
    public ResponseEntity<Object> create(@Valid @RequestBody PersonInputDto personInputDto) {
        PersonDto personDto = personService.create(personInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + personDto.id).toUriString());
        return ResponseEntity.created(uri).body(personDto);
    }

    @PatchMapping("/persons/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id,
                                         @Valid  @RequestBody PersonInputDto personInputDto) {
        PersonDto dto = personService.update(id, personInputDto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/persons/{id}")
    public ResponseEntity<PersonDto> delete(@PathVariable Integer id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
