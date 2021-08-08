package com.elo7.hackday.outboxpattern.controller;

import com.elo7.hackday.outboxpattern.dto.BookDTO;
import com.elo7.hackday.outboxpattern.entity.Book;
import com.elo7.hackday.outboxpattern.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Optional;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/books")
    public ResponseEntity<Void> createBook(@RequestBody BookDTO book) {
        Book newBook = bookService.createBookWithEvent(book.toEntity());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(newBook.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookDTO> createBook(@PathVariable Long id) {

        return bookService.findBook(id)
                .map(book -> BookDTO.fromEntity(book))
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }
}
