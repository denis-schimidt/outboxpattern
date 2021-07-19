package com.elo7.hackday.outboxpattern.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EntityOutbox {

     UUID getEventId();

     Long getAggregateId() ;

     String getEventType() ;

     String getPayload();

     LocalDateTime getCreatedAt();
}
