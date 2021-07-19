package com.elo7.hackday.outboxpattern.entity;

import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Book implements EntityOutbox {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    // JPA Eyes
    public Book() {}

    public Book(Long id, String title, String description, Author author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public UUID getEventId() {
        return UUID.randomUUID();
    }

    @Override
    public Long getAggregateId() {
        return id;
    }

    @Override
    public String getEventType() {
        return "BookCreated";
    }

    @Override
    public String getPayload() {
        return new JSONObject()
            .put("title", title)
            .put("description", description)
            .put("author", new JSONObject()
                                .put("id", author.getId())
                                .put("name",author.getName()))
            .toString();
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return LocalDateTime.now();
    }
}
