package com.elo7.hackday.outboxpattern.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Author {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Author(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // JPA Eyes
    public Author() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
