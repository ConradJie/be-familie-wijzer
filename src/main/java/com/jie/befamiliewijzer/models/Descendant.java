package com.jie.befamiliewijzer.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;

import java.util.Date;

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
    private Date marriageBeginDate;
    @Column(name = "marriage_end_date")
    private Date marriageEndDate;
    @Column(name = "divorce_begin_date")
    private Date divorceBeginDate;
    @Column(name = "divorce_end_date")
    private Date divorceEndDate;
    @Column(name = "person_id")
    private Integer personId;
    @Column(name = "given_names", length = 120)
    private String givenNames;
    @Column(length = 120)
    private String surname;
    @Column(name = "birth_begin_date")
    private Date birthBeginDate;
    @Column(name = "birth_end_date")
    private Date birthEndDate;
    @Column(name = "death_begin_date")
    private Date deathBeginDate;
    @Column(name = "death_end_date")
    private Date deathEndDate;
    @Column(name = "spouse_id")
    private Integer spouseId;
    @Column(name = "spouse_given_names", length = 120)
    private String spouseGivenNames;
    @Column(name = "spouse_surname", length = 120)
    private String spouseSurname;

    @Column(name = "spouse_birth_begin_date")
    private Date spouseBirthBeginDate;
    @Column(name = "spouse_birth_end_date")
    private Date spouseBirthEndDate;
    @Column(name = "spouse_death_begin_date")
    private Date spouseDeathBeginDate;
    @Column(name = "spouse_death_End_date")
    private Date spouseDeathEndDate;

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

    public Date getMarriageBeginDate() {
        return marriageBeginDate;
    }

    public Date getMarriageEndDate() {
        return marriageEndDate;
    }

    public Date getDivorceBeginDate() {
        return divorceBeginDate;
    }

    public Date getDivorceEndDate() {
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

    public Date getBirthBeginDate() {
        return birthBeginDate;
    }

    public Date getBirthEndDate() {
        return birthEndDate;
    }

    public Date getDeathBeginDate() {
        return deathBeginDate;
    }

    public Date getDeathEndDate() {
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

    public Date getSpouseBirthBeginDate() {
        return spouseBirthBeginDate;
    }

    public Date getSpouseBirthEndDate() {
        return spouseBirthEndDate;
    }

    public Date getSpouseDeathBeginDate() {
        return spouseDeathBeginDate;
    }

    public Date getSpouseDeathEndDate() {
        return spouseDeathEndDate;
    }
}
