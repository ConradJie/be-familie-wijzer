package com.jie.befamiliewijzer.controllers;

import com.jie.befamiliewijzer.dtos.EventDto;
import com.jie.befamiliewijzer.dtos.EventInputDto;
import com.jie.befamiliewijzer.services.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/evens/{id}")
    public ResponseEntity<EventDto> getEvent(@PathVariable Integer id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    @GetMapping("/evens")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @PostMapping("/evens")
    public ResponseEntity<Object> createEvent(@Valid @RequestBody EventInputDto eventInputDto) {
        EventDto eventDtoo = eventService.createEvent(eventInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + eventDtoo.id).toUriString());
        return ResponseEntity.created(uri).body(eventDtoo);
    }

    @PutMapping("/evens/{id}")
    public ResponseEntity<Object> updateEvent(@PathVariable Integer id,
                                               @Valid @RequestBody EventInputDto eventInputDto) {
        EventDto dto = eventService.updateEvent(id, eventInputDto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/evens/{id}")
    public ResponseEntity<EventDto> deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

}
