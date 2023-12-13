package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.RelationDto;
import com.jie.befamiliewijzer.dtos.RelationInputDto;
import com.jie.befamiliewijzer.dtos.RelationSpouseDto;
import com.jie.befamiliewijzer.exceptions.ResourceAlreadyExistsException;
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
import java.util.Objects;
import java.util.Optional;

@Service
public class RelationService {
    private final RelationRepository relationRepository;
    private final PersonRepository personRepository;
    private final ChildRepository childRepository;

    public RelationService(RelationRepository relationRepository,
                           PersonRepository personRepository,
                           ChildRepository childRepository) {
        this.relationRepository = relationRepository;
        this.personRepository = personRepository;
        this.childRepository = childRepository;
    }

    public RelationDto getRelation(Integer id) {
        Relation relation = relationRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested relation could not be found"));
        return transfer(relation);
    }

    public RelationDto getRelationByPersonIdAndSpouseId(Integer personId, Integer spouseId) {
        if (!personRepository.existsById(personId)) {
            throw new ResourceNotFoundException("The person do not exists");
        }
        if (!personRepository.existsById(spouseId)) {
            throw new ResourceNotFoundException("The spouse do not exists");
        }
        Relation relation = relationRepository
                .findByPersonIdAndSpouseId(personId, spouseId)
                .orElseThrow(() -> new ResourceNotFoundException("The requested relation could not be found"));
        return transfer(relation);
    }

    public List<RelationSpouseDto> getAllRelationsFromPersonId(Integer personId) {
        List<Relation> relations = relationRepository.findAllByPersonIdOrSpouseId(personId, personId);
        List<RelationSpouseDto> list = new ArrayList<>();
        for (Relation relation : relations) {
            boolean single = false;
            if (relation.getPerson() != null) {
                if (!relation.getPerson().getId().equals(personId)) {
                    RelationSpouseDto relationSpouseDto = new RelationSpouseDto();
                    relationSpouseDto.id = relation.getId();
                    relationSpouseDto.personId = personId;
                    relationSpouseDto.spouseId = relation.getPerson().getId();
                    relationSpouseDto.spouseGivenNames = relation.getPerson().getGivenNames();
                    relationSpouseDto.spouseSurname = relation.getPerson().getSurname();
                    relationSpouseDto.spouseSex = relation.getPerson().getSex();
                    list.add(relationSpouseDto);
                }
            }
            if (relation.getSpouse() != null) {
                if (!relation.getSpouse().getId().equals(personId)) {
                    RelationSpouseDto relationSpouseDto = new RelationSpouseDto();
                    relationSpouseDto.id = relation.getId();
                    relationSpouseDto.personId = personId;
                    relationSpouseDto.spouseId = relation.getSpouse().getId();
                    relationSpouseDto.spouseGivenNames = relation.getSpouse().getGivenNames();
                    relationSpouseDto.spouseSurname = relation.getSpouse().getSurname();
                    relationSpouseDto.spouseSex = relation.getSpouse().getSex();
                    list.add(relationSpouseDto);
                }
            } else {
                single = true;
            }
            if (single) {
                //If single then create an anonymous spouse
                RelationSpouseDto relationSpouseDto = new RelationSpouseDto();
                relationSpouseDto.id = relation.getId();
                relationSpouseDto.personId = personId;
                relationSpouseDto.spouseId = null;
                relationSpouseDto.spouseGivenNames = "-";
                relationSpouseDto.spouseSurname = "-";
                relationSpouseDto.spouseSex = "X";
                list.add(relationSpouseDto);
            }
        }
        return list;
    }

    public List<RelationDto> getAllRelations() {
        return transfer(relationRepository.findAll());
    }

    public RelationDto createRelation(RelationInputDto dto) {
        if ((dto.personId == null || !personRepository.existsById(dto.personId))
                && (dto.spouseId == null || !personRepository.existsById(dto.spouseId))) {
            throw new ResourceNotFoundException("The person(s) do not exists");
        }
        if (Objects.equals(dto.personId, dto.spouseId)) {
            throw new ResourceNotFoundException("The person and spouse are the same person");
        }
        if (relationRepository.existsByPersonIdAndSpouseId(dto.personId, dto.spouseId)
                || relationRepository.existsByPersonIdAndSpouseId(dto.spouseId, dto.personId)) {
            throw new ResourceAlreadyExistsException("The relation already Exists");
        }
        Relation relation = transfer(dto);
        relation = relationRepository.save(relation);
        return transfer(relation);
    }

    public RelationDto updateRelation(Integer id, RelationInputDto dto) {
        Relation relation = relationRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested relation could not be found"));
        if (dto.personId == null) {
            relation.setPerson(null);
        } else {
            Person person = personRepository
                    .findById(dto.personId)
                    .orElseThrow(() -> new ResourceNotFoundException("The requested person could not be found"));
            relation.setPerson(person);
        }
        if (dto.spouseId == null) {
            relation.setSpouse(null);
        } else {
            Person spouse = personRepository
                    .findById(dto.spouseId)
                    .orElseThrow(() -> new ResourceNotFoundException("The requested spouse could not be found"));
            relation.setSpouse(spouse);
        }
        if (relation.getPerson() == null && relation.getSpouse() != null) {
            relation.setPerson(relation.getSpouse());
            relation.setSpouse(null);
        }
        relation = relationRepository.save(relation);
        return transfer(relation);
    }

    public void deleteRelation(Integer id) {
        if (relationRepository.existsById(id)) {
            //Detach person, spouse and children from relation before deleting relation
            Optional<Relation> relationOptional = relationRepository.findById(id);
            if (relationOptional.isPresent()) {
                Relation relation = relationOptional.get();
                relation.setPerson(null);
                relation.setSpouse(null);
                if (relation.getChildren() != null) {
                    for (Child child : relation.getChildren()) {
                        child.setPerson(null);
                        childRepository.save(child);
                    }
                }
                relationRepository.save(relation);
                relationRepository.deleteById(id);
            }
        }
    }

    public void removePersonFromRelation(Integer relationId, Integer personId) {
        Relation relation = relationRepository
                .findById(relationId)
                .orElseThrow(() -> new ResourceNotFoundException("The requested relation could not be found"));
        if (Objects.equals(relation.getPerson().getId(), personId)) {
            if (relation.getSpouse()!=null) {
                relation.setPerson(relation.getSpouse());
                relation.setSpouse(null);
            } else {
                relation.setPerson(null);
            }
        } else if (Objects.equals(relation.getSpouse().getId(), personId)) {
            relation.setSpouse(null);
        }
        if (relation.getChildren().size() == 0) {
            relation.setPerson(null);
        }
        relation = relationRepository.save(relation);
        if (relation.getPerson() == null && relation.getSpouse() == null) {
            deleteRelation(relationId);
        }
    }

    private RelationDto transfer(Relation relation) {
        RelationDto dto = new RelationDto();
        dto.id = relation.getId();
        if (relation.getPerson() != null) {
            dto.personId = relation.getPerson().getId();
        }
        if (relation.getSpouse() != null) {
            dto.spouseId = relation.getSpouse().getId();
        }
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
