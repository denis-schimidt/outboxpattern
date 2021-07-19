package com.elo7.hackday.outboxpattern.repository;

import com.elo7.hackday.outboxpattern.entity.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {}
