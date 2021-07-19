package com.elo7.hackday.outboxpattern.repository;

import com.elo7.hackday.outboxpattern.entity.Outbox;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxRepository extends CrudRepository<Outbox, Long> {}
