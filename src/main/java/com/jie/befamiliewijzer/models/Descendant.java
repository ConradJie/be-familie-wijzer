package com.jie.befamiliewijzer.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Entity(name = "descendants")
@Immutable
public class Descendant {
    @Id
    @Column(length = 12)
    private String id;
    private int level;
    @Column(name = "relation_parent")
    private Integer relationParent;
    @Column(name = "relation_child")
    private Integer relationChild;
    @Column(name = "marriage_begin_date")
    private LocalDate marriageBeginDate;
    @Column(name = "marriage_end_date")
    private LocalDate marriageEndDate;
    @Column(name = "divorce_begin_date")
    private LocalDate divorceBeginDate;
    @Column(name = "divorce_end_date")
    private LocalDate divorceEndDate;
    @Column(name = "person_id")
    private Integer personId;
    @Column(name = "given_names", length = 120)
    private String givenNames;
    @Column(length = 120)
    private String surname;
    @Column(name = "birth_begin_date")
    private LocalDate birthBeginDate;
    @Column(name = "birth_end_date")
    private LocalDate birthEndDate;
    @Column(name = "death_begin_date")
    private LocalDate deathBeginDate;
    @Column(name = "death_end_date")
    private LocalDate deathEndDate;
    @Column(name = "spouse_id")
    private Integer spouseId;
    @Column(name = "spouse_given_names", length = 120)
    private String spouseGivenNames;
    @Column(name = "spouse_surname", length = 120)
    private String spouseSurname;

    @Column(name = "spouse_birth_begin_date")
    private LocalDate spouseBirthBeginDate;
    @Column(name = "spouse_birth_end_date")
    private LocalDate spouseBirthEndDate;
    @Column(name = "spouse_death_begin_date")
    private LocalDate spouseDeathBeginDate;
    @Column(name = "spouse_death_End_date")
    private LocalDate spouseDeathEndDate;

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public Integer getRelationParent() {
        return relationParent;
    }

    public Integer getRelationChild() {
        return relationChild;
    }

    public LocalDate getMarriageBeginDate() {
        return marriageBeginDate;
    }

    public LocalDate getMarriageEndDate() {
        return marriageEndDate;
    }

    public LocalDate getDivorceBeginDate() {
        return divorceBeginDate;
    }

    public LocalDate getDivorceEndDate() {
        return divorceEndDate;
    }

    public Integer getPersonId() {
        return personId;
    }

    public String getGivenNames() {
        return givenNames;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthBeginDate() {
        return birthBeginDate;
    }

    public LocalDate getBirthEndDate() {
        return birthEndDate;
    }

    public LocalDate getDeathBeginDate() {
        return deathBeginDate;
    }

    public LocalDate getDeathEndDate() {
        return deathEndDate;
    }

    public Integer getSpouseId() {
        return spouseId;
    }

    public String getSpouseGivenNames() {
        return spouseGivenNames;
    }

    public String getSpouseSurname() {
        return spouseSurname;
    }

    public LocalDate getSpouseBirthBeginDate() {
        return spouseBirthBeginDate;
    }

    public LocalDate getSpouseBirthEndDate() {
        return spouseBirthEndDate;
    }

    public LocalDate getSpouseDeathBeginDate() {
        return spouseDeathBeginDate;
    }

    public LocalDate getSpouseDeathEndDate() {
        return spouseDeathEndDate;
    }

    public String getDateText(LocalDate beginDate, LocalDate endDate) {
        if (beginDate == null) {
            return "";
        }
        return String.format("%s %s %s",
                beginDate.getDayOfMonth() == endDate.getDayOfMonth() ?
                        beginDate.getDayOfMonth() : "",
                beginDate.getMonth() == endDate.getMonth() ?
                        beginDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.US) : "",
                beginDate.getYear() == endDate.getYear() ?
                        beginDate.getYear() : "").trim();
    }
}
