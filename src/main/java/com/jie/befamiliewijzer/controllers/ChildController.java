package com.jie.befamiliewijzer.controllers;

import com.jie.befamiliewijzer.dtos.ChildDto;
import com.jie.befamiliewijzer.services.ChildService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class ChildController {
    private final ChildService childService;

    public ChildController(ChildService childService) {
        this.childService = childService;
    }

    @GetMapping("/children/{id}")
    public ResponseEntity<ChildDto> getChild(@PathVariable Integer id) {
        return ResponseEntity.ok(childService.getChild(id));
    }

    @GetMapping("/children")
    public ResponseEntity<List<ChildDto>> getAlChildren() {
        return ResponseEntity.ok(childService.getAllChildren());
    }

    @PostMapping("/children")
    public ResponseEntity<Object> createChild(@Valid @RequestBody ChildDto dto) {
        ChildDto childDto = childService.createChild(dto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + childDto.id).toUriString());
        return ResponseEntity.created(uri).body(childDto);
    }

    @PostMapping("/children/{id}")
    public ResponseEntity<Object> updateChild(@PathVariable Integer id,
                                         @Valid @RequestBody ChildDto childDto) {
        ChildDto dto = childService.updateChild(id, childDto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/children/{id}")
    public ResponseEntity<ChildDto> deleteChild(@PathVariable Integer id) {
        childService.deleteChild(id);
        return ResponseEntity.noContent().build();
    }

}
