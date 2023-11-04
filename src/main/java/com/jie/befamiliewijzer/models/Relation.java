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
    @OneToMany(mappedBy = "relation")
    @JsonIgnore
    Set<Child> children;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    Person person;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "spouce_id")
    Person spouce;

    @OneToMany(mappedBy = "relation")
    @JsonIgnore
    List<Event> events;

    public Integer getId() {
        return id;
    }

    public Set<Child> getChildren() {
        return children;
    }

    public Person getPerson() {
        return person;
    }

    public Person getSpouce() {
        return spouce;
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

    public void setSpouce(Person spouce) {
        this.spouce = spouce;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}

