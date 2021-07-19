package com.elo7.hackday.outboxpattern.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

@Entity
public class Outbox {

    @Id
    @Column(name = "eventId")
    private UUID eventId;

    @Column(name = "aggregateId")
    private Long aggregateId;

    @Column(name = "eventType")
    private String eventType;

    @Column(name = "payload")
    private String payload;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    // JPA Eyes
    Outbox() {}

    private Outbox(EntityOutbox entityOutbox) {
        this.eventId = entityOutbox.getEventId();
        this.aggregateId = entityOutbox.getAggregateId();
        this.eventType = entityOutbox.getEventType();
        this.payload = entityOutbox.getPayload();
        this.createdAt = entityOutbox.getCreatedAt();
    }

    public static Outbox from(EntityOutbox entityOutbox) {
        return new Outbox(entityOutbox);
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getAggregateId() {
        return aggregateId.toString();
    }

    public void setAggregateId(Long aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, JSON_STYLE);
    }
}
