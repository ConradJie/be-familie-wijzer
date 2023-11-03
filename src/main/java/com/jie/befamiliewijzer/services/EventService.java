package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.EventDto;
import com.jie.befamiliewijzer.dtos.EventInputDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.models.Event;
import com.jie.befamiliewijzer.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public EventDto getEvent(Integer id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            return transfer(eventOptional.get());
        } else {
            throw new ResourceNotFoundException("The requested event could not be found");
        }
    }

    public List<EventDto> getAllEvents() {
        return transfer(eventRepository.findAll());
    }


    public EventDto createEvent(EventInputDto dto) {
        Event event = transfer(dto);
        eventRepository.save(event);
        return transfer(event);
    }


    public EventDto updateEvent(Integer id, EventInputDto dto) {
        Event event = eventRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested person could not be found"));
        event.setEventType(dto.eventType);
        event.setDescription(dto.description);
        event.setBeginDate(dto.beginDate);
        event.setEndDate(dto.endDate);
        eventRepository.save(event);
        return transfer(event);
    }

    public void deleteEvent(Integer id) {
        if (eventRepository.existsById(id)) {
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
        return dto;
    }

    private Event transfer(EventInputDto dto) {
        Event event = new Event();
        event.setEventType(dto.eventType);
        event.setDescription(dto.description);
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
