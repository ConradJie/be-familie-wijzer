package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.EventDto;
import com.jie.befamiliewijzer.dtos.EventInputDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.models.Event;
import com.jie.befamiliewijzer.models.Person;
import com.jie.befamiliewijzer.models.Relation;
import com.jie.befamiliewijzer.repositories.EventRepository;
import com.jie.befamiliewijzer.repositories.PersonRepository;
import com.jie.befamiliewijzer.repositories.RelationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final PersonRepository personRepository;
    private final RelationRepository relationRepository;

    public EventService(EventRepository eventRepository,
                        PersonRepository personRepository,
                        RelationRepository relationRepository) {
        this.eventRepository = eventRepository;
        this.personRepository = personRepository;
        this.relationRepository = relationRepository;
    }

    public EventDto getEventFromPerson(Integer personId, Integer id) {
        Event event = eventRepository
                .findByPersonIdAndId(personId, id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested event could not be found"));
        return transfer(event);
    }

    public EventDto getEventFromRelation(Integer relationId, Integer id) {
        Event event = eventRepository
                .findByRelationIdAndId(relationId, id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested event could not be found"));
        return transfer(event);
    }

    public List<EventDto> getAllEventsFromPerson(Integer personId) {
        if (personRepository.existsById(personId)) {
            return transfer(eventRepository.findEventsByPersonId(personId));
        } else {
            throw new ResourceNotFoundException("The requested person could not be found");
        }
    }

    public List<EventDto> getAllEventsFromRelation(Integer relationId) {
        if (relationRepository.existsById(relationId)) {
            return transfer(eventRepository.findEventsByRelationId(relationId));
        } else {
            throw new ResourceNotFoundException("The requested relation could not be found");
        }
    }

    public EventDto createEventFromPerson(Integer personId, EventInputDto dto) {
        Person person = personRepository
                .findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("The requested person could not be found"));
        Event event = transfer(dto);
        event.setPerson(person);
        eventRepository.save(event);
        return transfer(event);
    }

    public EventDto createEventFromRelation(Integer relationId, EventInputDto dto) {
        Relation relation = relationRepository
                .findById(relationId)
                .orElseThrow(() -> new ResourceNotFoundException("The requested relation could not be found"));
        Event event = transfer(dto);
        event.setRelation(relation);
        eventRepository.save(event);
        return transfer(event);
    }

    public EventDto updateEventFromPerson(Integer personId, Integer id, EventInputDto dto) {
        Person person = personRepository
                .findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("The requested person could not be found"));
        Event event = eventRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested event could not be found"));
        event.setEventType(dto.eventType);
        event.setDescription(dto.description);
        event.setText(dto.text);
        event.setBeginDate(dto.beginDate);
        event.setEndDate(dto.endDate);
        event.setPerson(person);
        eventRepository.save(event);
        return transfer(event);
    }

    public EventDto updateEventFromRelation(Integer relationId, Integer id, EventInputDto dto) {
        Relation relation = relationRepository
                .findById(relationId)
                .orElseThrow(() -> new ResourceNotFoundException("The requested relation could not be found"));
        Event event = eventRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested event could not be found"));
        event.setEventType(dto.eventType);
        event.setDescription(dto.description);
        event.setText(dto.text);
        event.setBeginDate(dto.beginDate);
        event.setEndDate(dto.endDate);
        event.setRelation(relation);
        eventRepository.save(event);
        return transfer(event);
    }

    public void deleteEventFromPerson(Integer personId, Integer id) {
        if (personRepository.existsById(personId) && eventRepository.existsById(id)) {
            //Detach person from event before deleting event
            Event event = eventRepository
                    .findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("The requested event could not be found"));
            event.setPerson(null);
            eventRepository.save(event);
            eventRepository.deleteById(id);
        }
    }

    public void deleteEventFromRelation(Integer relationId, Integer id) {
        if (relationRepository.existsById(relationId) && eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
        }
    }

    private EventDto transfer(Event event) {
        EventDto dto = new EventDto();
        dto.id = event.getId();
        dto.eventType = event.getEventType();
        dto.description = event.getDescription();
        dto.text = event.getText();
        dto.beginDate = event.getBeginDate();
        dto.endDate = event.getEndDate();
        if (event.getPerson() != null) {
            dto.personId = event.getPerson().getId();
        }
        if (event.getRelation() != null) {
            dto.relationId = event.getRelation().getId();
        }
        return dto;
    }

    private Event transfer(EventInputDto dto) {
        Event event = new Event();
        event.setEventType(dto.eventType);
        event.setDescription(dto.description);
        event.setText(dto.text);
        event.setBeginDate(dto.beginDate);
        event.setEndDate(dto.endDate);
        return event;
    }

    private List<EventDto> transfer(List<Event> events) {
        List<EventDto> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(transfer(event));
        }
        return dtos;
    }
}
