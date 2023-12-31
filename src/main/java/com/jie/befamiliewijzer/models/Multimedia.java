package com.jie.befamiliewijzer.models;

import jakarta.persistence.*;

@Entity
@Table(name = "multimedias")
public class Multimedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 128)
    private String description;
    @Column(length = 128)
    private String filename;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    Event event;
    @OneToOne
    Media media;

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getFilename() {
        return filename;
    }

    public Event getEvent() {
        return event;
    }

    public Media getMedia() {
        return media;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
