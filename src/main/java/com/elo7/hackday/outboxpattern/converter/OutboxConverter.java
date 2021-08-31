package com.elo7.hackday.outboxpattern.converter;

import com.elo7.hackday.outboxpattern.entity.Outbox;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class OutboxConverter<T> {
    private UUID eventId;
    private LocalDateTime createdAt;
    private String kafkaPartitionId;

    public final Outbox toOutbox(Serializable kafkaPartitionId, T entity){
        this.kafkaPartitionId = kafkaPartitionId.toString();
        this.eventId = UUID.randomUUID();
        this.createdAt =  LocalDateTime.now();

        String payload = toKafkaEvent(entity);

        return new Outbox(getEventId(), getKafkaPartitionId(), getEventName(), getCreatedAt(), payload);
    }

    public final UUID getEventId() {
        return eventId;
    };

    public final LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public final String getKafkaPartitionId() {
        return kafkaPartitionId;
    }

    public abstract String getEventName();

    protected abstract String toKafkaEvent(T entity);
}
