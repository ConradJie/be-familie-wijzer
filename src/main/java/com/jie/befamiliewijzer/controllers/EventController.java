package com.jie.befamiliewijzer.controllers;

import com.jie.befamiliewijzer.dtos.EventDto;
import com.jie.befamiliewijzer.dtos.EventInputDto;
import com.jie.befamiliewijzer.dtos.EventTypeDto;
import com.jie.befamiliewijzer.services.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@CrossOrigin
@RestController
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/eventtypes")
    public ResponseEntity<List<EventTypeDto>> getEventTypes() {
        return ResponseEntity.ok(eventService.getEventTypes());
    }
    @GetMapping("/eventtypes/person")
    public ResponseEntity<List<EventTypeDto>> getPerosnEventTypes() {
        return ResponseEntity.ok(eventService.getPersonEventTypes());
    }
    @GetMapping("/eventtypes/relation")
    public ResponseEntity<List<EventTypeDto>> getRelationEventTypes() {
        return ResponseEntity.ok(eventService.getRelationEventTypes());
    }
    @GetMapping("/persons/{personId}/events/{id}")
    public ResponseEntity<EventDto> getEventFromPerson(@PathVariable Integer personId, @PathVariable Integer id) {
        return ResponseEntity.ok(eventService.getEventFromPerson(personId, id));
    }

    @GetMapping("/relations/{relationId}/events/{id}")
    public ResponseEntity<EventDto> getEventFromRelation(@PathVariable Integer relationId, @PathVariable Integer id) {
        return ResponseEntity.ok(eventService.getEventFromRelation(relationId, id));
    }
    @GetMapping("/persons/{id}/events")
    public ResponseEntity<List<EventDto>> getAllEventsFromPerson(@PathVariable Integer id) {
        return ResponseEntity.ok(eventService.getAllEventsFromPerson(id));
    }
    @GetMapping("/persons/{id}/relationEvents")
    public ResponseEntity<List<EventDto>> getAllRelationEventsFromPerson(@PathVariable Integer id) {
        return ResponseEntity.ok(eventService.getAllRelationEventsFromPerson(id));
    }
    @GetMapping("/relations/{id}/events")
    public ResponseEntity<List<EventDto>> getAllEventsFromRelation(@PathVariable Integer id) {
        return ResponseEntity.ok(eventService.getAllEventsFromRelation(id));
    }

    @PostMapping("/persons/{personId}/events")
    public ResponseEntity<Object> createEventFromPerson(@PathVariable Integer personId, @Valid @RequestBody EventInputDto eventInputDto) {
        EventDto eventDtoo = eventService.createEventFromPerson(personId, eventInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + eventDtoo.id).toUriString());
        return ResponseEntity.created(uri).body(eventDtoo);
    }
    @PostMapping("/relations/{relationId}/events")
    public ResponseEntity<Object> createEventFromRelation(@PathVariable Integer relationId, @Valid @RequestBody EventInputDto eventInputDto) {
        EventDto eventDtoo = eventService.createEventFromRelation(relationId, eventInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + eventDtoo.id).toUriString());
        return ResponseEntity.created(uri).body(eventDtoo);
    }

    @PutMapping("/persons/{personId}/events/{id}")
    public ResponseEntity<Object> updateEventFromPerson(@PathVariable Integer personId, @PathVariable Integer id,
                                                        @Valid @RequestBody EventInputDto eventInputDto) {
        EventDto dto = eventService.updateEventFromPerson(personId, id, eventInputDto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/relations/{relationId}/events/{id}")
    public ResponseEntity<Object> updateEventFromRelation(@PathVariable Integer relationId, @PathVariable Integer id,
                                                        @Valid @RequestBody EventInputDto eventInputDto) {
        EventDto dto = eventService.updateEventFromRelation(relationId, id, eventInputDto);
        return ResponseEntity.ok(dto);
    }
    @DeleteMapping("/persons/{personId}/events/{id}")
    public ResponseEntity<EventDto> deleteEventFromPerson(@PathVariable Integer personId, @PathVariable Integer id) {
        eventService.deleteEventFromPerson(personId, id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/relations/{relationId}/events/{id}")
    public ResponseEntity<EventDto> deleteEventFromRelation(@PathVariable Integer relationId, @PathVariable Integer id) {
        eventService.deleteEventFromRelation(relationId, id);
        return ResponseEntity.noContent().build();
    }

}
