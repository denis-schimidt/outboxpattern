package com.elo7.hackday.outboxpattern.service;

import com.elo7.hackday.outboxpattern.entity.Book;
import com.elo7.hackday.outboxpattern.entity.Outbox;
import com.elo7.hackday.outboxpattern.repository.AuthorRepository;
import com.elo7.hackday.outboxpattern.repository.BookRepository;
import com.elo7.hackday.outboxpattern.repository.OutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private OutboxRepository outboxRepository;

    @Transactional
    public Book createBookWithEvent(Book book) {

        authorRepository.findById(book.getAuthor().getId())
            .ifPresent(book::setAuthor);

        Book newBook = bookRepository.save(book);
        outboxRepository.save(Outbox.from(newBook));

        return book;
    }

    public Optional<Book> findBook(Long id) {
        return bookRepository.findById(id);
    }
}
