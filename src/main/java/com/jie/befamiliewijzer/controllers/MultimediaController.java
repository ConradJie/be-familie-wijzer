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

@CrossOrigin
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

    @GetMapping("/events/{eventId}/multimedias/{id}")
    public ResponseEntity<MultimediaDto> getMultimedia(@PathVariable Integer eventId, @PathVariable Integer id) {
        return ResponseEntity.ok(multimediaService.getMultimediaFromEvent(eventId, id));
    }

    @GetMapping("/events/{eventId}/multimedias")
    public ResponseEntity<List<MultimediaDto>> getAllMultimediasFromEvent(@PathVariable Integer eventId) {
//        return ResponseEntity.ok(multimediaService.getAllMultimediasFromEvent(eventId));
        List<MultimediaDto> list = multimediaService.getAllMultimediasFromEvent(eventId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/multimedias")
    public ResponseEntity<Object> createMultimedia(@Valid @RequestBody MultimediaInputDto multimediaInputDto) {
        MultimediaDto multimediaDto = multimediaService.createMultimedia(multimediaInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + multimediaDto.id).toUriString());
        return ResponseEntity.created(uri).body(multimediaDto);
    }

    @PostMapping("/events/{eventId}/multimedias")
    public ResponseEntity<Object> createMultimediaFromEvent(@PathVariable Integer eventId,
                                                            @Valid @RequestBody MultimediaInputDto multimediaInputDto) {
        MultimediaDto multimediaDto = multimediaService.createMultimediaFromEvent(eventId, multimediaInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + multimediaDto.id).toUriString());
        return ResponseEntity.created(uri).body(multimediaDto);
    }

    @PutMapping("/events/{eventId}/multimedias/{id}")
    public ResponseEntity<Object> updateMultimediaEvent(@PathVariable Integer eventId,
                                                        @PathVariable Integer id,
                                                        @Valid @RequestBody MultimediaInputDto multimediaInputDto) {
        MultimediaDto dto = multimediaService.updateMultimediaFromEvent(eventId, id, multimediaInputDto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/events/{eventId}/multimedias/{id}")
    public ResponseEntity<MultimediaDto> deletePerson(@PathVariable Integer eventId, @PathVariable Integer id) {
        multimediaService.deleteMultimediaFromEvent(eventId, id);
        return ResponseEntity.noContent().build();
    }
}
