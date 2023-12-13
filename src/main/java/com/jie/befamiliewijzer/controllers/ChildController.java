package com.jie.befamiliewijzer.controllers;

import com.jie.befamiliewijzer.dtos.ChildDto;
import com.jie.befamiliewijzer.dtos.ChildInputDto;
import com.jie.befamiliewijzer.dtos.ChildPersonDto;
import com.jie.befamiliewijzer.services.ChildService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@CrossOrigin
@RestController
public class ChildController {
    private final ChildService childService;

    public ChildController(ChildService childService) {
        this.childService = childService;
    }

    @GetMapping("/relations/{relationId}/children/{id}")
    public ResponseEntity<ChildDto> getChildFromRelation(@PathVariable Integer relationId, @PathVariable Integer id) {
        return ResponseEntity.ok(childService.getChildFromRelation(relationId, id));
    }

    @GetMapping("/relations/{relationId}/children")
    public ResponseEntity<List<ChildPersonDto>> getAlChildrenFromRelation(@PathVariable Integer relationId) {
        return ResponseEntity.ok(childService.getAlChildrenFromRelation(relationId));
    }

    @PostMapping("/relations/{relationId}/children")
    public ResponseEntity<Object> createChildFromRelation(@PathVariable Integer relationId, @Valid @RequestBody ChildInputDto dto) {
        ChildDto childDto = childService.createChildFromRelation(relationId, dto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + childDto.id).toUriString());
        return ResponseEntity.created(uri).body(childDto);
    }

    @DeleteMapping("/relations/{relationId}/children/{id}")
    public ResponseEntity<ChildDto> deleteChildFromRelation(@PathVariable Integer relationId, @PathVariable Integer id) {
        childService.deleteChildFromRelation(relationId, id);
        return ResponseEntity.noContent().build();
    }

}
