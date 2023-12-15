package com.jie.befamiliewijzer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "event_type", length = 12)
    private String eventType;
    @Column(length = 1024)
    private String description;
    @Column(length = 10240)
    private String text;
    @Column(name = "begin_date")
    private LocalDate beginDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    Person person;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "relation_id")
    Relation relation;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Multimedia> multimedias;

    public Integer getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Person getPerson() {
        return person;
    }

    public Relation getRelation() {
        return relation;
    }

    public List<Multimedia> getMultimedias() {
        return multimedias;
    }

    public String getDateText() {
        return String.format("%s %s %s",
                this.beginDate.getDayOfMonth() == this.endDate.getDayOfMonth()?
                        this.beginDate.getDayOfMonth() : "",
                this.beginDate.getMonth() == this.endDate.getMonth()?
                        this.beginDate.getMonth() : "",
                this.beginDate.getYear() == this.endDate.getYear()?
                        this.beginDate.getYear() : "").trim();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public void setMultimedias(List<Multimedia> multimedias) {
        this.multimedias = multimedias;
    }

    public static Set<String> getPersonOneTimeEventTypes() {
        return new HashSet<>(Arrays.asList("BIRTH", "DEATH"));
    }

    public static Set<String> getPersonMultipleTimesEventTypes() {
        return new HashSet<>(Arrays.asList("MIGRATION", "CELEBRATION", "OTHERS"));
    }

    public static Set<String> getPersonEventTypes() {
        Set<String> set = new HashSet<>();
        set.addAll(getPersonOneTimeEventTypes());
        set.addAll(getPersonMultipleTimesEventTypes());
        return set;
    }

    public static Set<String> getRelationEventTypes() {
        return new HashSet<>(Arrays.asList("MARRIAGE", "DIVORCE", "OTHERS"));
    }

    public static Set<String> getEventTypes() {
        Set<String> set = new HashSet<>();
        set.addAll(getPersonEventTypes());
        set.addAll(getRelationEventTypes());
        return set;
    }

}
