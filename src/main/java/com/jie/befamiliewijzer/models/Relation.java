package com.jie.befamiliewijzer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "relations")
public class Relation {
    @Id
    private Integer id;
    Integer childId;
    Integer personId;
    Integer spouceId;

    public Integer getId() {
        return id;
    }

    public Integer getChildId() {
        return childId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public Integer getSpouceId() {
        return spouceId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setChildId(Integer childId) {
        childId = childId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public void setSpouceId(Integer spouceId) {
        this.spouceId = spouceId;
    }
}

