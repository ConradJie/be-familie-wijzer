package com.jie.befamiliewijzer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "given_names", length = 120)
    private String givenNames;
    @Column(length = 120)
    private String surname;
    private String sex;
    @OneToOne(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Child child;
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    Set<Relation> relations;
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    Set<Event> events;

    public Integer getId() {
        return id;
    }

    public String getGivenNames() {
        return givenNames;
    }

    public String getSurname() {
        return surname;
    }

    public String getSex() {
        return sex;
    }

    public Child getChild() {
        return child;
    }

    public Set<Relation> getRelations() {
        return relations;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGivenNames(String givenNames) {
        this.givenNames = givenNames;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public void setRelations(Set<Relation> relations) {
        this.relations = relations;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public static Set<String> getSexTypes() {
        return new HashSet<>(Arrays.asList("M", "F", "X"));
    }
}
