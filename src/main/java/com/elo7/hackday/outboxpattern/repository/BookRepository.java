package com.elo7.hackday.outboxpattern.repository;

import com.elo7.hackday.outboxpattern.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book,Long> {}
