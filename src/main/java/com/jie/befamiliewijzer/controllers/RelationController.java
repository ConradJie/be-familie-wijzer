package com.jie.befamiliewijzer.controllers;

import com.jie.befamiliewijzer.dtos.RelationDto;
import com.jie.befamiliewijzer.dtos.RelationInputDto;
import com.jie.befamiliewijzer.services.RelationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
public class RelationController {
    final private RelationService relationService;

    public RelationController(RelationService relationService) {
        this.relationService = relationService;
    }

    @GetMapping("/relations/{id}")
    public ResponseEntity<RelationDto> getRelation(@PathVariable Integer id) {
        return ResponseEntity.ok(relationService.getRelation(id));
    }

    @GetMapping("/relations/persons/{personId}")
    public ResponseEntity<List<RelationDto>> getRelationFromPerson(@PathVariable Integer personId) {
        return ResponseEntity.ok(relationService.getAllRelationFromPersonId(personId));
    }

    @GetMapping("/relations/persons/{personId}/{spouseId}")
    public ResponseEntity<RelationDto> getRelation(@PathVariable Integer personId, @PathVariable Integer spouseId) {
        return ResponseEntity.ok(relationService.getRelationByPersonIdAndSpouseId(personId, spouseId));
    }

    @GetMapping("/relations")
    public ResponseEntity<List<RelationDto>> getAllRelations() {
        return ResponseEntity.ok(relationService.getAllRelations());
    }

    @PostMapping("/relations")
    public ResponseEntity<Object> createRelation(@Valid @RequestBody RelationInputDto relationInputDto) {
        RelationDto relationDto = relationService.createRelation(relationInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + relationDto.id).toUriString());
        return ResponseEntity.created(uri).body(relationDto);
    }

    @PutMapping("/relations/{id}")
    public ResponseEntity<Object> updateRelation(@PathVariable Integer id,
                                                 @Valid @RequestBody RelationInputDto relationInputDto) {
        RelationDto dto = relationService.updateRelation(id, relationInputDto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/relations/{id}")
    public ResponseEntity<RelationDto> deleteRelation(@PathVariable Integer id) {
        relationService.deleteRelation(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/relations/persons/{personId}/{spouseId}")
    public ResponseEntity<RelationDto> deleteRelationByPersonIdAndSpouseId(@PathVariable Integer personId, @PathVariable Integer spouseId) {
        relationService.deleteRelationByPersonIdAndSpouseId(personId, spouseId);
        return ResponseEntity.noContent().build();
    }
}
