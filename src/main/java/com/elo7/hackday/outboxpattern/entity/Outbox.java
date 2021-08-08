package com.elo7.hackday.outboxpattern.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

@Entity
public class Outbox {

    @Id
    @Column(name = "eventId")
    private UUID eventId;

    @Column(name = "kafkaPartitionId")
    private String kafkaPartitionId;

    @Column(name = "eventType")
    private String eventType;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "payload")
    private String payload;

    // JPA Eyes
    Outbox(){}

    public Outbox(UUID eventId, String kafkaPartitionId, String eventType, LocalDateTime createdAt, String payload) {
        this.eventId = eventId;
        this.kafkaPartitionId = requireNonNull(kafkaPartitionId);
        this.eventType = requireNonNull(eventType);
        this.createdAt = createdAt;
        this.payload = requireNonNull(payload);
    }

    public UUID getEventId() {
        return eventId;
    }

    public String getKafkaPartitionId() {
        return kafkaPartitionId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPayload() {
        return payload;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, JSON_STYLE);
    }
}
