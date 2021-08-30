package com.elo7.hackday.outboxpattern.service;

import com.elo7.hackday.outboxpattern.entity.Outbox;
import com.elo7.hackday.outboxpattern.repository.OutboxRepository;
import org.slf4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

class DlqCallbackHandler implements ListenableFutureCallback<SendResult<String, String>> {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxRepository outboxRepository;
    private final Logger logger;
    private final Outbox outbox;
    private final AlarmNotification alarmNotification;

    private DlqCallbackHandler(KafkaTemplate<String, String> kafkaTemplate, OutboxRepository outboxRepository, Logger logger, Outbox outbox, AlarmNotification alarmNotification) {
        this.kafkaTemplate = requireNonNull(kafkaTemplate);
        this.outboxRepository = requireNonNull(outboxRepository);
        this.logger = requireNonNull(logger);
        this.outbox = requireNonNull(outbox);
        this.alarmNotification = alarmNotification;
    }

    static DlqCallbackHandler of(KafkaTemplate<String, String> kafkaTemplate, OutboxRepository outboxRepository, Logger logger, Outbox outbox, AlarmNotification alarmNotification) {
        return new DlqCallbackHandler(kafkaTemplate, outboxRepository, logger, outbox, alarmNotification);
    }

    @Override
    public void onSuccess(SendResult<String, String> result) {
        outboxRepository.delete(outbox);
        alarmNotification.sendNotification(format("Sent message=[{}}] to DLQ Topic", outbox));

        logger.info("Sent message=[{}}] to DLQ Topic with offset=[{}}]", outbox, result.getRecordMetadata().offset());
    }

    @Override
    public void onFailure(Throwable exception) {
        outbox.addRetryToDlqTopic();
        String errorMessage = format("Error trying to send message to DLQ Topic=[%s] - Exception: %s", outbox, exception.getMessage());

        alarmNotification.sendNotification(errorMessage);
        logger.error(errorMessage);
    }
}
