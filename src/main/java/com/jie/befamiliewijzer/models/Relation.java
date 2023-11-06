package com.jie.befamiliewijzer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "relations")
public class Relation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "relation", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<Child> children;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    Person person;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "spouse_id")
    Person spouse;

    @OneToMany(mappedBy = "relation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Event> events;

    @OneToMany(mappedBy = "relation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Child> getChildren() {
        return children;
    }

    public Integer getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public Person getSpouse() {
        return spouse;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setChildren(Set<Child> children) {
        this.children = children;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}

