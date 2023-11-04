package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.ChildDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.models.Child;
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

    public ChildDto getChild(Integer id) {
        Optional<Child> childOptional = childRepository.findById(id);
        if (childOptional.isPresent()) {
            return transfer(childOptional.get());
        } else {
            throw new ResourceNotFoundException("The requested child could not be found");
        }
    }

    public List<ChildDto> getAllChildren() {
        return transfer(childRepository.findAll());
    }


    public ChildDto createChild(ChildDto dto) {
        Child child = transfer(dto);
        childRepository.save(child);
        return transfer(child);
    }


    public ChildDto updateChild(Integer id, ChildDto dto) {
        Child child = childRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested child could not be found"));
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
        return transfer(child);
    }

    public void deleteChild(Integer id) {
        if (childRepository.existsById(id)) {
            childRepository.deleteById(id);
        }
    }

    private ChildDto transfer(Child child) {
        ChildDto dto = new ChildDto();
        dto.id = child.getId();
        dto.personId = child.getPerson().getId();
        dto.relationId = child.getRelation().getId();
        return dto;
    }

    private Child transfer(ChildDto dto) {
        Child child = new Child();
        child.setId(dto.id);
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
