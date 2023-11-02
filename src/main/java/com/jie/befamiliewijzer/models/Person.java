package com.jie.befamiliewijzer.models;

import jakarta.persistence.*;

@Entity
@Table(name = "persons")
public class Person {
    @Id
    private Integer id;
    private String givenNames;
    private String surname;
    private String sex;

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
}
