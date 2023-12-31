package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.EventDto;
import com.jie.befamiliewijzer.dtos.EventInputDto;
import com.jie.befamiliewijzer.dtos.EventMonthDayDto;
import com.jie.befamiliewijzer.dtos.EventTypeDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.exceptions.UnprocessableEntityException;
import com.jie.befamiliewijzer.models.Event;
import com.jie.befamiliewijzer.models.Person;
import com.jie.befamiliewijzer.models.Relation;
import com.jie.befamiliewijzer.repositories.EventRepository;
import com.jie.befamiliewijzer.repositories.PersonRepository;
import com.jie.befamiliewijzer.repositories.RelationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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

    public List<EventTypeDto> getEventTypes() {
        Set<String> eventTypes = Event.getEventTypes();
        return transfer(eventTypes);
    }

    public List<EventTypeDto> getPersonEventTypes() {
        Set<String> eventTypes = Event.getPersonEventTypes();
        return transfer(eventTypes);
    }

    public List<EventTypeDto> getRelationEventTypes() {
        Set<String> eventTypes = Event.getRelationEventTypes();
        return transfer(eventTypes);
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
            return transfer(eventRepository.findEventsByPersonIdOrderByBeginDate(personId));
        } else {
            throw new ResourceNotFoundException("The requested person could not be found");
        }
    }

    public List<EventDto> getAllRelationEventsFromPerson(Integer personId) {
        if (!personRepository.existsById(personId)) {
            throw new ResourceNotFoundException("The requested person could not be found");
        }
        List<EventDto> list = new ArrayList<>();
        List<Relation> relations = relationRepository.findAllByPersonIdOrSpouseId(personId, personId);
        for (Relation relation : relations) {
            List<Event> events = eventRepository.findEventsByRelationId(relation.getId());
            for (Event event : events) {
                list.add(transfer(event));
            }
        }
        return list;
    }

    public List<EventDto> getAllEventsFromRelation(Integer relationId) {
        if (relationRepository.existsById(relationId)) {
            return transfer(eventRepository.findEventsByRelationId(relationId));
        } else {
            throw new ResourceNotFoundException("The requested relation could not be found");
        }
    }

    public List<EventMonthDayDto> getAllCelebrateEventsOnMonthDay(Integer month, Integer day) {
        List<EventMonthDayDto> dtos = new ArrayList<>();
        List<Event> events = eventRepository.findAllEventsOnMonthDay(month, day);
        for (Event event : events) {
            if (event.getEventType().equals("MARRIAGE")) {
                Optional<Event> divorce = eventRepository.
                        findEventByRelationIdAndEventType(event.getRelation().getId(), "DIVORCE");
                if (divorce.isPresent()) {
                    continue;
                }
            } else if (event.getEventType().equals("BIRTH")) {
                Optional<Event> death = eventRepository.
                        findByPersonIdAndEventType(event.getPerson().getId(), "DEATH");
                if (death.isPresent()) {
                    continue;
                }
            } else {
                continue;
            }
            EventMonthDayDto dto = new EventMonthDayDto();
            dto.id = event.getId();
            dto.eventType = event.getEventType();
            dto.description = event.getDescription();
            dto.text = event.getText();
            dto.date = event.getBeginDate();
            if (event.getPerson() != null) {
                Optional<Person> personOptional = personRepository.findById(event.getPerson().getId());
                if (personOptional.isPresent()) {
                    Person person = personOptional.get();
                    dto.personId = person.getId();
                    dto.givenNames = person.getGivenNames();
                    dto.surname = person.getSurname();
                }
            } else {
                Optional<Relation> relationOptional = relationRepository.findById(event.getRelation().getId());
                if (relationOptional.isPresent()) {
                    Relation relation = relationOptional.get();
                    dto.relationId = relation.getId();
                    Optional<Person> personOptional = personRepository.findById(relation.getPerson().getId());
                    if (personOptional.isPresent()) {
                        Person person = personOptional.get();
                        dto.givenNames = person.getGivenNames();
                        dto.surname = person.getSurname();
                    }
                    if (relation.getSpouse() != null) {
                        Optional<Person> spouseOptional = personRepository.findById(relation.getSpouse().getId());
                        if (spouseOptional.isPresent()) {
                            Person spouse = spouseOptional.get();
                            dto.spouseGivenNames = spouse.getGivenNames();
                            dto.spouseSurname = spouse.getSurname();
                        }
                    }
                }
            }
            dtos.add(dto);
        }
        return dtos;
    }

    public EventDto createEventFromPerson(Integer personId, EventInputDto dto) {
        if (dto.beginDate.isAfter(LocalDate.now()) || dto.endDate.isAfter(LocalDate.now())) {
            throw new UnprocessableEntityException("This event is in the future");
        }
        Person person = personRepository
                .findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("The requested person could not be found"));
        validatePersonEventDate(personId, dto);

        dto.relationId = null;
        Event event = transfer(dto);
        event.setPerson(person);
        event = eventRepository.save(event);
        return transfer(event);
    }

    private void validatePersonEventDate(Integer personId, EventInputDto dto) {
        validatePersonEventDate(personId, -1, dto);
    }


    public EventDto createEventFromRelation(Integer relationId, EventInputDto dto) {
        if (dto.beginDate.isAfter(LocalDate.now()) || dto.endDate.isAfter(LocalDate.now())) {
            throw new UnprocessableEntityException("This event is in the future");
        }
        Relation relation = relationRepository
                .findById(relationId)
                .orElseThrow(() -> new ResourceNotFoundException("The requested relation could not be found"));
        if (!Event.getRelationEventTypes().contains(dto.eventType.toUpperCase())) {
            throw new UnprocessableEntityException("The event type could not be processed");
        }
        dto.personId = null;
        Event event = transfer(dto);
        event.setRelation(relation);
        event = eventRepository.save(event);
        return transfer(event);
    }

    private void validatePersonEventDate(Integer personId, Integer eventId, EventInputDto dto) {
        if (!Event.getPersonEventTypes().contains(dto.eventType.toUpperCase())) {
            throw new UnprocessableEntityException("The event type could not be processed");
        }
        if (dto.beginDate.isAfter(dto.endDate)) {
            throw new UnprocessableEntityException("The begin date occurred after the end date");
        }
        if (Event.getPersonOneTimeEventTypes().contains(dto.eventType)) {
            Optional<Event> eventOptional = eventRepository.
                    findByPersonIdAndEventType(personId, dto.eventType);
            if (eventOptional.isPresent()) {
                Event event = eventOptional.get();
                if (!event.getId().equals(eventId)) {
                    throw new UnprocessableEntityException(String.format("Such an event (%s) already exists", dto.eventType));
                }
            }
        }
        List<Event> personEvents = eventRepository.findEventsByPersonIdOrderByBeginDate(personId);
        if (personEvents.size() > 0) {
            boolean birth = personEvents.get(0).getEventType().equals("BIRTH");
            boolean selfBeginOfPeriod = personEvents.get(0).getId().equals(eventId);
            LocalDate beginOfPeriod = personEvents.get(0).getBeginDate();
            LocalDate beginOfPeriodNext = personEvents.get(Math.min(1, personEvents.size() - 1)).getBeginDate();
            Integer beginOfPeriodNextId = personEvents.get(Math.min(1, personEvents.size() - 1)).getId();

            boolean death = personEvents.get(personEvents.size() - 1).getEventType().equals("DEATH");
            boolean selfEndOfPeriod = personEvents.get(personEvents.size() - 1).getId().equals(eventId);
            LocalDate endOfPeriod = personEvents.get(personEvents.size() - 1).getEndDate();
            LocalDate endOfPeriodPrev = personEvents.get(Math.max(0, personEvents.size() - 2)).getEndDate();
            Integer endOfPeriodPrevId = personEvents.get(Math.max(0, personEvents.size() - 2)).getId();

            if (birth && !selfBeginOfPeriod && beginOfPeriod.isAfter(dto.endDate)) {
                throw new UnprocessableEntityException("The event occurred before the date of birth");
            } else if (dto.eventType.equals("DEATH")
                    && (dto.endDate.isBefore(beginOfPeriod)
                    || (dto.endDate.isBefore(beginOfPeriodNext) && !Objects.equals(eventId, beginOfPeriodNextId)))) {
                throw new UnprocessableEntityException("The date of death occurs before previous events");
            }
            if (death && !selfEndOfPeriod && endOfPeriod.isBefore(dto.beginDate)) {
                throw new UnprocessableEntityException("The event occurs after the date of death");
            } else if (dto.eventType.equals("BIRTH")
                    && dto.beginDate.isAfter(endOfPeriod)
                    && (dto.beginDate.isAfter(endOfPeriodPrev) && !Objects.equals(eventId, endOfPeriodPrevId))) {
                throw new UnprocessableEntityException("The date of birth occurs after previous events");
            }
        }
    }

    public EventDto updateEventFromPerson(Integer personId, Integer id, EventInputDto dto) {
        if (dto.beginDate.isAfter(LocalDate.now()) || dto.endDate.isAfter(LocalDate.now())) {
            throw new UnprocessableEntityException("This event is in the future");
        }
        Person person = personRepository
                .findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("The requested person could not be found"));
        Event event = eventRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested event could not be found"));
        validatePersonEventDate(personId, id, dto);
        event.setEventType(dto.eventType.toUpperCase());
        event.setDescription(dto.description);
        event.setText(dto.text);
        event.setBeginDate(dto.beginDate);
        event.setEndDate(dto.endDate);
        event.setPerson(person);
        event = eventRepository.save(event);
        return transfer(event);
    }

    public EventDto updateEventFromRelation(Integer relationId, Integer id, EventInputDto dto) {
        if (dto.beginDate.isAfter(LocalDate.now()) || dto.endDate.isAfter(LocalDate.now())) {
            throw new UnprocessableEntityException("This event is in the future");
        }
        Relation relation = relationRepository
                .findById(relationId)
                .orElseThrow(() -> new ResourceNotFoundException("The requested relation could not be found"));
        Event event = eventRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested event could not be found"));
        if (!Event.getRelationEventTypes().contains(dto.eventType.toUpperCase())) {
            throw new UnprocessableEntityException("The event type could not be processed");
        }
        event.setEventType(dto.eventType);
        event.setDescription(dto.description);
        event.setText(dto.text);
        event.setBeginDate(dto.beginDate);
        event.setEndDate(dto.endDate);
        event.setRelation(relation);
        event = eventRepository.save(event);
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
            //Detach relation from event before deleting event
            Event event = eventRepository
                    .findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("The requested event could not be found"));
            event.setRelation(null);
            eventRepository.save(event);
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
        dto.dateText = event.getDateText();
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
        event.setEventType(dto.eventType.toUpperCase());
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

    private List<EventTypeDto> transfer(Set<String> eventTypes) {
        List<EventTypeDto> list = new ArrayList<>();
        for (String type : eventTypes) {
            EventTypeDto dto = new EventTypeDto();
            dto.eventType = type;
            list.add(dto);
        }
        return list;
    }

}
