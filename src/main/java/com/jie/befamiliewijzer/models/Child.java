package com.jie.befamiliewijzer.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "children")
public class Child {
    @Id
    private Integer id;
    Integer personId;

    Integer relationId;

    public Integer getId() {
        return id;
    }

    public Integer getPersonId() {
        return personId;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }
}
