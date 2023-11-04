package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.RelationDto;
import com.jie.befamiliewijzer.dtos.RelationInputDto;
import com.jie.befamiliewijzer.exceptions.ResourceAlreadyExistsException;
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
import java.util.Optional;

@Service
public class RelationService {
    private final RelationRepository relationRepository;
    private final PersonRepository personRepository;
    private final EventRepository eventRepository;

    public RelationService(RelationRepository relationRepository,
                           PersonRepository personRepository,
                           EventRepository eventRepository) {
        this.relationRepository = relationRepository;
        this.personRepository = personRepository;
        this.eventRepository = eventRepository;
    }

    public RelationDto getRelation(Integer id) {
        Optional<Relation> relationFound = relationRepository.findById(id);
        if (relationFound.isPresent()) {
            return transfer(relationFound.get());
        } else {
            throw new ResourceNotFoundException("The requested relation could not be found");
        }
    }

    public List<RelationDto> getAllRelations() {
//        return transfer(relationRepository.findAll());
        List<Relation> list = relationRepository.findAll();
        return transfer(list);
    }

    public RelationDto createRelation(RelationInputDto dto) {
        if ((dto.personId == null || !personRepository.existsById(dto.personId))
                && (dto.spouceId == null || !personRepository.existsById(dto.spouceId))) {
            throw new ResourceNotFoundException("The person(s) do not exists");
        }
        List<Relation> relations = relationRepository.findAllByPersonIdAndSpouceId(dto.personId, dto.spouceId);
        if (!relations.isEmpty()) {
            boolean divorced = false;
            for (Relation relation : relations) {
                Optional<Event> eventOptional = eventRepository.findEventByRelationIdAndEventType(relation.getId(), "DIVORCE");
                if (eventOptional.isPresent()) {
                    divorced = true;
                    break;
                }
            }
            if (!divorced) {
                throw new ResourceAlreadyExistsException("The relation already Exists");
            }
        }
        Relation relation = transfer(dto);
        relationRepository.save(relation);
        return transfer(relation);
    }

    public RelationDto updateRelation(Integer id, RelationInputDto dto) {
        Relation relation = relationRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested relation could not be found"));
        relation = transfer(dto);
        relationRepository.save(relation);
        return transfer(relation);
    }

    public void deleteRelation(Integer id) {
        if (relationRepository.existsById(id)) {
            relationRepository.deleteById(id);
        }
    }

    private RelationDto transfer(Relation relation) {
        RelationDto dto = new RelationDto();
        dto.id = relation.getId();
        dto.personId = relation.getPerson().getId();
        dto.spouceId = relation.getSpouce().getId();
        return dto;
    }

    private List<RelationDto> transfer(List<Relation> relations) {
        List<RelationDto> dtos = new ArrayList<>();
        for (Relation relation : relations) {
            dtos.add(transfer(relation));
        }
        return dtos;
    }

    private Relation transfer(RelationInputDto dto) {
        Relation relation = new Relation();
        if (dto.personId != null) {
            Optional<Person> personOptional = personRepository.findById(dto.personId);
            if (personOptional.isPresent()) {
                relation.setPerson(personOptional.get());
            }
        }
        if (dto.spouceId != null) {
            Optional<Person> personOptional = personRepository.findById(dto.spouceId);
            if (personOptional.isPresent()) {
                relation.setSpouce(personOptional.get());
            }
        }
        return relation;
    }
}
