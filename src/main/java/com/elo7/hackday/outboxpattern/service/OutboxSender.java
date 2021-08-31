package com.elo7.hackday.outboxpattern.service;

import com.elo7.hackday.outboxpattern.entity.Outbox;
import com.elo7.hackday.outboxpattern.repository.OutboxRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
class OutboxSender {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxRepository outboxRepository;
    private final Logger logger;
    private final int maxRetriesToSendEventToKafka;
    private final AlarmNotification alarmNotification;
    private final DlqEventHandler dlqEventHandler;

    @Autowired
    OutboxSender(KafkaTemplate<String, String> kafkaTemplate, OutboxRepository outboxRepository, Logger logger, @Value("${max-retries-to-send-event-to-kafka}") Integer maxRetriesToSendEventToKafka,
                 AlarmNotification alarmNotification, DlqEventHandler dlqEventHandler) {
        this.kafkaTemplate = kafkaTemplate;
        this.outboxRepository = outboxRepository;
        this.logger = logger;
        this.maxRetriesToSendEventToKafka = maxRetriesToSendEventToKafka;
        this.alarmNotification = alarmNotification;
        this.dlqEventHandler = dlqEventHandler;
    }

    void sendToKafka(Outbox outbox) {

        try {
            var future = kafkaTemplate.send(outbox.getTopic(), outbox.getKafkaPartitionId(), outbox.getPayload());

            future.addCallback((successfulResult) -> {
                        outboxRepository.delete(outbox);
                        logger.info("Sent message=[{}}] with offset=[{}}]", outbox, successfulResult.getRecordMetadata().offset());
                    },
                    (exception) -> new RuntimeException(exception.getMessage()));

        } catch (Exception exception) {

            if (outbox.hasExceed(maxRetriesToSendEventToKafka)) {
                dlqEventHandler.handleEventToDlq(outbox, exception);
                return;
            }

            outbox.addRetryToOriginalTopic();
            logger.warn("Error trying to send message=[{}}] - Exception: {}", outbox, exception.getMessage());
        }
    }
}
