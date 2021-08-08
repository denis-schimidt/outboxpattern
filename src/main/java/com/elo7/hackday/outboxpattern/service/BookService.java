package com.elo7.hackday.outboxpattern.service;

import com.elo7.hackday.outboxpattern.entity.Book;
import com.elo7.hackday.outboxpattern.repository.AuthorRepository;
import com.elo7.hackday.outboxpattern.repository.BookRepository;
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
    private OutboxService<Book> outboxService;

    @Transactional
    public Book createBookWithEvent(Book book) {

        authorRepository.findById(book.getAuthor().getId())
                .ifPresent(book::setAuthor);

        Book newBook = bookRepository.save(book);
        outboxService.createOutbox(book.getId(), book, "bookCreated");

        return book;
    }

    public Optional<Book> findBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        optionalBook.ifPresent(book -> outboxService.createOutbox(book.getId(), book, "bookRetrieved"));

        return optionalBook;
    }
}
