package com.elo7.hackday.outboxpattern.dto;

import com.elo7.hackday.outboxpattern.entity.Author;
import com.elo7.hackday.outboxpattern.entity.Book;

public class BookDTO {
    private Long id;
    private String title;
    private String description;
    private AuthorDTO author;

    // Jackson Eyes
    public BookDTO() {}

    public BookDTO(Long id, String title, String description, AuthorDTO author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }

    public Book toEntity() {
        return new Book(id, title, description, new Author(author.getId(), author.getName()));
    }

    public static BookDTO fromEntity(Book book) {
        return new BookDTO(book.getId(), book.getTitle(), book.getDescription(), new AuthorDTO(book.getAuthor().getId(), book.getAuthor().getName()));
    }
}
