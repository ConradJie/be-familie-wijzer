package com.jie.befamiliewijzer.models;

import jakarta.persistence.*;

@Entity
@Table(name = "multimedias")
public class Multimedia {
    @Id
    private Integer id;
    private String description;
    @Column(length = 128)
    private String filename;

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getFilename() {
        return filename;
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
}
