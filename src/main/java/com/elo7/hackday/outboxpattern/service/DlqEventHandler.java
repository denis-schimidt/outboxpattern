package com.elo7.hackday.outboxpattern.service;

import com.elo7.hackday.outboxpattern.entity.Outbox;
import com.elo7.hackday.outboxpattern.repository.OutboxRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
class DlqEventHandler {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private OutboxRepository outboxRepository;
    @Autowired
    private Logger logger;
    @Autowired
    private AlarmNotification alarmNotification;

    void handleEventToDlq(Outbox outbox, Exception exception) {

        try {
            kafkaTemplate.send(outbox.getDLQTopic(), outbox.getKafkaPartitionId(), outbox.getPayload())
                    .addCallback((successfulResult) -> {
                                outboxRepository.delete(outbox);
                                alarmNotification.sendNotification(format("Sent message=[{}}] to DLQ Topic", outbox));
                                logger.warn("Sent message=[{}}] to DLQ Topic with offset=[{}}]", outbox, successfulResult.getRecordMetadata().offset());
                            },
                            (otherException) -> new RuntimeException(otherException.getMessage()));

        } catch (Exception e) {
            outbox.addRetryToDlqTopic();
            String errorMessage = format("Error trying to send message to DLQ Topic=[%s] - Exception: %s", outbox, exception.getMessage());
            alarmNotification.sendNotification(errorMessage);
            logger.error(errorMessage);
        }
    }
}
