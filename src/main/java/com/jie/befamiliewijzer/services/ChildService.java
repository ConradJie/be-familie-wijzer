package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.ChildDto;
import com.jie.befamiliewijzer.dtos.ChildInputDto;
import com.jie.befamiliewijzer.dtos.ChildPersonDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.models.Child;
import com.jie.befamiliewijzer.models.Event;
import com.jie.befamiliewijzer.models.Person;
import com.jie.befamiliewijzer.models.Relation;
import com.jie.befamiliewijzer.repositories.ChildRepository;
import com.jie.befamiliewijzer.repositories.PersonRepository;
import com.jie.befamiliewijzer.repositories.RelationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChildService {
    private final ChildRepository childRepository;
    private final PersonRepository personRepository;
    private final RelationRepository relationRepository;

    public ChildService(ChildRepository childRepository, PersonRepository personRepository, RelationRepository relationRepository) {
        this.childRepository = childRepository;
        this.personRepository = personRepository;
        this.relationRepository = relationRepository;
    }

    public ChildDto getChildFromRelation(Integer relationId, Integer id) {
        Child child = childRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested child could not be found"));
        if (child.getRelation().getId() != relationId) {
            throw new ResourceNotFoundException("The requested relation could not be found");
        }
        return transfer(child);
    }

    public List<ChildPersonDto> getAlChildrenFromRelation(Integer relationId) {
        List<Child> list = childRepository.findAllByRelationId(relationId);
        List<ChildPersonDto> childPersonDtos = new ArrayList<>();
        for (Child child : list) {
            Optional<Person> personOptional = personRepository.findById(child.getPerson().getId());
            if (personOptional.isPresent()) {
                Person person = personOptional.get();
                ChildPersonDto dto = new ChildPersonDto();
                dto.id = child.getId();
                dto.personId = child.getPerson().getId();
                dto.relationId = child.getRelation().getId();
                dto.givenNames = person.getGivenNames();
                dto.surname = person.getSurname();
                dto.sex = person.getSex();
                childPersonDtos.add(dto);
            }
        }
        return childPersonDtos;
    }


    public ChildDto createChildFromRelation(Integer relationId, ChildInputDto dto) {
        if (relationId != dto.relationId || !relationRepository.existsById(relationId)) {
            throw new ResourceNotFoundException("The requested relation could not be found");
        }
        if (childRepository.existsByRelation_IdAndPersonId(relationId, dto.personId)) {
            throw new ResourceNotFoundException("This child already exists");
        }
        Child child = transfer(dto);
        childRepository.save(child);
        return transfer(child);
    }


    public ChildDto updateChildFromRelastion(Integer relationId, Integer id, ChildInputDto dto) {
        if (relationId != dto.relationId || !relationRepository.existsById(relationId)) {
            throw new ResourceNotFoundException("The requested relation could not be found");
        }
        Child child = childRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested child could not be found"));
        if (dto.personId != null) {
            Person person = personRepository
                    .findById(dto.personId)
                    .orElseThrow(() -> new ResourceNotFoundException("The requested person could not be found"));
            child.setPerson(person);
        } else {
            child.setPerson(null);
        }
        childRepository.save(child);
        return transfer(child);
    }

    public void deleteChildFromRelation(Integer relationId, Integer id) {
        if (childRepository.existsById(id)) {
            Child child = childRepository.findById(id).get();
            if (relationId == child.getRelation().getId()) {
                //Detach person and relation from child before deleting relation
                child.setPerson(null);
                child.setRelation(null);
                childRepository.save(child);
                childRepository.deleteById(id);
            }
        }
    }

    private ChildDto transfer(Child child) {
        ChildDto dto = new ChildDto();
        dto.id = child.getId();
        dto.personId = child.getPerson().getId();
        dto.relationId = child.getRelation().getId();
        return dto;
    }

    private Child transfer(ChildInputDto dto) {
        Child child = new Child();
        if (dto.personId != null) {
            Optional<Person> personOptional = personRepository.findById(dto.personId);
            if (personOptional.isPresent()) {
                child.setPerson(personOptional.get());
            }
        }
        if (dto.relationId != null) {
            Optional<Relation> relationOptional = relationRepository.findById(dto.relationId);
            if (relationOptional.isPresent()) {
                child.setRelation(relationOptional.get());
            }
        }
        return child;
    }

    private List<ChildDto> transfer(List<Child> children) {
        List<ChildDto> dtos = new ArrayList<>();
        for (Child child : children) {
            dtos.add(transfer(child));
        }
        return dtos;
    }

}
