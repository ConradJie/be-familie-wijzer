package com.jie.befamiliewijzer.controllers;

import com.jie.befamiliewijzer.dtos.MultimediaDto;
import com.jie.befamiliewijzer.dtos.MultimediaInputDto;
import com.jie.befamiliewijzer.services.MultimediaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class MultimediaController {
    private final MultimediaService multimediaService;

    public MultimediaController(MultimediaService multimediaService) {
        this.multimediaService = multimediaService;
    }
    @GetMapping("/multimedias/{id}")
    public ResponseEntity<MultimediaDto> getMultimedia(@PathVariable Integer id) {
        return ResponseEntity.ok(multimediaService.getMultimedia(id));
    }

    @GetMapping("/multimedias")
    public ResponseEntity<List<MultimediaDto>> getAllMultimedias() {
        return ResponseEntity.ok(multimediaService.getAllMultimedias());
    }

    @PostMapping("/multimedias")
    public ResponseEntity<Object> createPerson(@Valid @RequestBody MultimediaInputDto multimediaInputDto) {
        MultimediaDto multimediaDto = multimediaService.createMultimedia(multimediaInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + multimediaDto.id).toUriString());
        return ResponseEntity.created(uri).body(multimediaDto);
    }

    @PutMapping("/multimedias/{id}")
    public ResponseEntity<Object> updatePerson(@PathVariable Integer id,
                                               @Valid @RequestBody MultimediaInputDto multimediaInputDto) {
        MultimediaDto dto = multimediaService.updateMultimedia(id, multimediaInputDto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/multimedias/{id}")
    public ResponseEntity<MultimediaDto> deletePerson(@PathVariable Integer id) {
        multimediaService.deleteMultimedia(id);
        return ResponseEntity.noContent().build();
    }
}
