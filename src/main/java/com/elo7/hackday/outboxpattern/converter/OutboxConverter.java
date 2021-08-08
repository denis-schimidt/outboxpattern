package com.elo7.hackday.outboxpattern.converter;

import com.elo7.hackday.outboxpattern.entity.Outbox;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class OutboxConverter<Entity> {
    private final UUID eventId = UUID.randomUUID();
    private final LocalDateTime createdAt = LocalDateTime.now();
    private String kafkaPartitionId;

    public final Outbox toOutbox(Serializable kafkaPartitionId, Entity entity){
        this.kafkaPartitionId = kafkaPartitionId.toString();
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

    protected abstract String toKafkaEvent(Entity entity);
}
