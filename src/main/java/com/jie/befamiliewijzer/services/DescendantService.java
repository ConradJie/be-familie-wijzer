package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.DescendantDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.models.Descendant;
import com.jie.befamiliewijzer.repositories.DescendantRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DescendantService {
    private final DescendantRepository descendantRepository;

    DescendantService(DescendantRepository descendantRepository) {
        this.descendantRepository = descendantRepository;
    }

    public List<DescendantDto> findDescendantsById(Integer id) {
        List<Descendant> descendants = descendantRepository.findDescendantsById(id);
        if (descendants.isEmpty()) {
            throw new ResourceNotFoundException("The requested descendant could not be found");
        }
        return transfer(descendants);
    }

    private DescendantDto transfer(Descendant descendant) {
        DescendantDto dto = new DescendantDto();
        dto.id = descendant.getId();
        dto.level = descendant.getLevel();
        dto.relationParent = descendant.getRelationParent();
        dto.relationChild = descendant.getRelationChild();
        String divorce = descendant.getDateText(descendant.getDivorceBeginDate(), descendant.getDivorceEndDate());
        dto.relationPeriod = String.format("%s%s%s",
                descendant.getDateText(descendant.getMarriageBeginDate(), descendant.getMarriageEndDate()),
                divorce.equals("") ? "" : " / ",
                divorce);
        dto.personId = descendant.getPersonId();
        dto.givenNames = descendant.getGivenNames();
        dto.surname = descendant.getSurname();
        String deathDate = descendant.getDateText(descendant.getDeathBeginDate(), descendant.getDeathEndDate());
        dto.lifePeriod = String.format("%s%s%s",
                descendant.getDateText(descendant.getBirthBeginDate(), descendant.getBirthEndDate()),
                deathDate.equals("") ? "" : " / ",
                deathDate);
        dto.spouseId = descendant.getSpouseId();
        dto.spouseGivenNames = descendant.getSpouseGivenNames();
        dto.spouseSurname = descendant.getSpouseSurname();
        deathDate = descendant.getDateText(descendant.getSpouseDeathBeginDate(), descendant.getSpouseDeathEndDate());
        dto.spouseLifePeriod = String.format("%s%s%s",
                descendant.getDateText(descendant.getSpouseBirthBeginDate(), descendant.getSpouseBirthEndDate()),
                deathDate.equals("") ? "" : " / ",
                deathDate);
        return dto;
    }

    private List<DescendantDto> transfer(List<Descendant> descendants) {
        List<DescendantDto> dtos = new ArrayList<>();
        for (Descendant descendant : descendants) {
            if (descendant!= null) {
                dtos.add(transfer(descendant));
            }
        }
        return dtos;
    }

}
