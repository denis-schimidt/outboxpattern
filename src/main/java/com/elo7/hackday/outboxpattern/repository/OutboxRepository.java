package com.elo7.hackday.outboxpattern.repository;

import com.elo7.hackday.outboxpattern.entity.Outbox;
import com.elo7.hackday.outboxpattern.repository.fragments.StreamRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, Long>, StreamRepository<Outbox> {

    default Stream<Outbox> streamAll() {
        return streamAll(Outbox.class);
    }
}
