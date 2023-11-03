package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.RelationDto;
import com.jie.befamiliewijzer.dtos.RelationInputDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.models.Relation;
import com.jie.befamiliewijzer.repositories.RelationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RelationService {
    private final RelationRepository relationRepository;
    public RelationService(RelationRepository relationRepository) {
        this.relationRepository = relationRepository;
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
        return transfer(relationRepository.findAll());
    }

    public RelationDto createRelation(RelationInputDto dto) {
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
        dto.personId = relation.getPersonId();
        dto.spouceId = relation.getSpouceId();
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
        relation.setPersonId(dto.personId);
        relation.setSpouceId(dto.spouceId);
        relation.setChildId(dto.childId);
        return relation;
    }
}
