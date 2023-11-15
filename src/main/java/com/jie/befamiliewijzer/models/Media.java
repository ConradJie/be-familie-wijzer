package com.jie.befamiliewijzer.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Media {
    @Id
    private String filename;
    private String contentType;
    private String url;

    public Media() {
    }

    public Media(String filename, String contentType, String url) {
        this.filename = filename;
        this.contentType = contentType;
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public String getContentType() {
        return contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
