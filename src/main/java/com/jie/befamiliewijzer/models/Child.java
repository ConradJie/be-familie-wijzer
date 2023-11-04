package com.jie.befamiliewijzer.models;

import jakarta.persistence.*;

@Entity
@Table(name = "children")
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "child_id")
    Person person;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "relation_id")
    Relation relation;

    public Integer getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }
}
