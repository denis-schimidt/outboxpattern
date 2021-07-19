package com.elo7.hackday.outboxpattern.dto;

public class AuthorDTO {
    private Long id;
    private String name;

    // Jackson Eyes
    public AuthorDTO() {}

    public AuthorDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

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
