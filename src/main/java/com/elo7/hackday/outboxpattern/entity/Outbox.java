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

    @Column(name = "topic")
    private String topic;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "payload")
    private String payload;

    @Column(name = "retriesToTopic")
    private int retriesToTopic;

    @Column(name = "retriesToDlqTopic")
    private int retriesToDlqTopic;

    // JPA Eyes
    Outbox() {}

    public Outbox(UUID eventId, String kafkaPartitionId, String eventType, LocalDateTime createdAt, String payload) {
        this.eventId = eventId;
        this.kafkaPartitionId = requireNonNull(kafkaPartitionId);
        this.topic = requireNonNull(eventType);
        this.createdAt = createdAt;
        this.payload = requireNonNull(payload);
    }

    public UUID getEventId() {
        return eventId;
    }

    public String getKafkaPartitionId() {
        return kafkaPartitionId;
    }

    public String getTopic() {
        return topic;
    }

    public String getDLQTopic() {
        return topic.concat("DLQ");
    }

    public String getPayload() {
        return payload;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void addRetryToOriginalTopic() {
        retriesToTopic++;
    }

    public void addRetryToDlqTopic() {
        retriesToDlqTopic++;
    }

    public int getRetriesToDlqTopic() {
        return retriesToDlqTopic;
    }

    public int getRetriesToTopic() {
        return retriesToTopic;
    }

    public boolean hasExceed(int maxRetriesToSendEventToKafka) {
        return retriesToTopic > maxRetriesToSendEventToKafka;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, JSON_STYLE);
    }
}
