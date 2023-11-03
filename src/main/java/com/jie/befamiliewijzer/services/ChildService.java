package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.ChildDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.models.Child;
import com.jie.befamiliewijzer.repositories.ChildRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChildService {
    private final ChildRepository childRepository;
    public ChildService(ChildRepository childRepository) {
        this.childRepository = childRepository;
    }

    public ChildDto getChild(Integer id) {
        Optional<Child> childFound = childRepository.findById(id);
        if (childFound.isPresent()) {
            return transfer(childFound.get());
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
        child.setPersonId(dto.personId);
        child.setPersonId(dto.relationId);
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
        dto.personId = child.getPersonId();
        dto.relationId = child.getRelationId();
        return dto;
    }

    private Child transfer(ChildDto dto) {
        Child child = new Child();
        child.setId(dto.id);
        child.setPersonId(dto.personId);
        child.setRelationId(dto.relationId);
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
