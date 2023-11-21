package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.DescendantDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.models.Descendant;
import com.jie.befamiliewijzer.repositories.DescendantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
        String divorce = datesToString(descendant.getDivorceBeginDate(), descendant.getDivorceEndDate());
        dto.relationPeriod = String.format("%s%s%s",
                datesToString(descendant.getMarriageBeginDate(), descendant.getMarriageEndDate()),
                divorce.equals("") ? "" : " / ",
                divorce);
        dto.personId = descendant.getPersonId();
        dto.givenNames = descendant.getGivenNames();
        dto.surname = descendant.getSurname();
        String deathDate = datesToString(descendant.getDeathBeginDate(), descendant.getDeathEndDate());
        dto.lifePeriod = String.format("%s%s%s",
                datesToString(descendant.getBirthBeginDate(), descendant.getBirthEndDate()),
                deathDate.equals("") ? "" : " / ",
                deathDate);
        dto.spouseId = descendant.getSpouseId();
        dto.spouseGivenNames = descendant.getSpouseGivenNames();
        dto.spouseSurname = descendant.getSpouseSurname();
        deathDate = datesToString(descendant.getSpouseDeathBeginDate(), descendant.getSpouseDeathEndDate());
        dto.spouseLifePeriod = String.format("%s%s%s",
                datesToString(descendant.getSpouseBirthBeginDate(), descendant.getSpouseBirthEndDate()),
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

    private String datesToString(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return "";
        }
        LocalDate begin = new java.sql.Date(beginDate.getTime()).toLocalDate();
        LocalDate end = new java.sql.Date(endDate.getTime()).toLocalDate();

        StringBuilder date = new StringBuilder();
        if (begin.getDayOfMonth() == end.getDayOfMonth()) {
            date.append(begin.getDayOfMonth());
        }
        if (begin.getMonthValue() == end.getMonthValue()) {
            if (!date.isEmpty()) {
                date.append("-");
            }
            date.append(begin.getMonthValue());
        }
        if (begin.getYear() == end.getYear()) {
            if (!date.isEmpty()) {
                date.append("-");
            }
            date.append(begin.getYear());
        }
        return date.toString();
    }

}
