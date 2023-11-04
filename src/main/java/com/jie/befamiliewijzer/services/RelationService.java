package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.RelationDto;
import com.jie.befamiliewijzer.dtos.RelationInputDto;
import com.jie.befamiliewijzer.exceptions.ResourceAlreadyExistsException;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.models.Child;
import com.jie.befamiliewijzer.models.Event;
import com.jie.befamiliewijzer.models.Person;
import com.jie.befamiliewijzer.models.Relation;
import com.jie.befamiliewijzer.repositories.ChildRepository;
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
    private final ChildRepository childRepository;

    public RelationService(RelationRepository relationRepository,
                           PersonRepository personRepository,
                           EventRepository eventRepository,
                           ChildRepository childRepository) {
        this.relationRepository = relationRepository;
        this.personRepository = personRepository;
        this.eventRepository = eventRepository;
        this.childRepository = childRepository;
    }

    public RelationDto getRelation(Integer id) {
        Optional<Relation> relationOptional = relationRepository.findById(id);
        if (relationOptional.isPresent()) {
            return transfer(relationOptional.get());
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
                && (dto.spouseId == null || !personRepository.existsById(dto.spouseId))) {
            throw new ResourceNotFoundException("The person(s) do not exists");
        }
        List<Relation> relations = relationRepository.findAllByPersonIdAndSpouseId(dto.personId, dto.spouseId);
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
        if (!relationRepository.existsById(id)) {
            throw new ResourceNotFoundException("The requested relation could not be found");
        }
        Relation relation = transfer(dto);
        relationRepository.save(relation);
        return transfer(relation);
    }

    public void deleteRelation(Integer id) {
        if (relationRepository.existsById(id)) {
            //Detach person, spouse and children from relation before deleting relation
            Relation relation = relationRepository.findById(id).get();
            relation.setPerson(null);
            relation.setSpouse(null);
            for (Child child : relation.getChildren()) {
                child.setPerson(null);
                childRepository.save(child);
            }
            relationRepository.save(relation);
            relationRepository.deleteById(id);
        }
    }

    private RelationDto transfer(Relation relation) {
        RelationDto dto = new RelationDto();
        dto.id = relation.getId();
        dto.personId = relation.getPerson().getId();
        dto.spouseId = relation.getSpouse().getId();
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
        if (dto.spouseId != null) {
            Optional<Person> personOptional = personRepository.findById(dto.spouseId);
            if (personOptional.isPresent()) {
                relation.setSpouse(personOptional.get());
            }
        }
        return relation;
    }
}
