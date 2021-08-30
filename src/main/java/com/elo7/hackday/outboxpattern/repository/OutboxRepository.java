package com.elo7.hackday.outboxpattern.repository;

import com.elo7.hackday.outboxpattern.entity.Outbox;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface OutboxRepository extends CrudRepository<Outbox, Long> {

    @Query("SELECT o FROM Outbox o")
    Stream<Outbox> streamAll();
}
