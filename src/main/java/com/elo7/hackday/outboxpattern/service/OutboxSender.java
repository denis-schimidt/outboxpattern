package com.elo7.hackday.outboxpattern.service;

import com.elo7.hackday.outboxpattern.entity.Outbox;
import com.elo7.hackday.outboxpattern.repository.OutboxRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
class OutboxSender {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxRepository outboxRepository;
    private final Logger logger;
    private final int maxRetriesToSendEventToKafka;
    private final AlarmNotification alarmNotification;

    @Autowired
    OutboxSender(KafkaTemplate<String, String> kafkaTemplate, OutboxRepository outboxRepository, Logger logger, @Value("maxRetriesToSendEventToKafka") int maxRetriesToSendEventToKafka,
                 AlarmNotification alarmNotification) {
        this.kafkaTemplate = kafkaTemplate;
        this.outboxRepository = outboxRepository;
        this.logger = logger;
        this.maxRetriesToSendEventToKafka = maxRetriesToSendEventToKafka;
        this.alarmNotification = alarmNotification;
    }

    void sendToKafka(Outbox outbox) {

        var future = kafkaTemplate.send(outbox.getEventType(), outbox.getKafkaPartitionId(), outbox.getPayload());
        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                outboxRepository.delete(outbox);

                logger.info("Sent message=[{}}] with offset=[{}}]", outbox, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable exception) {

                if (outbox.hasExceed(maxRetriesToSendEventToKafka)) {
                    var dlqResult =  kafkaTemplate.send(outbox.getDLQType(), outbox.getKafkaPartitionId(), outbox.getPayload());

                    dlqResult.addCallback(DlqCallbackHandler.of(kafkaTemplate, outboxRepository, logger, outbox, alarmNotification));

                } else {
                    outbox.addRetryToOriginalTopic();
                    logger.warn("Error trying to send message=[{}}] - Exception: {}", outbox, exception.getMessage());
                }
            }
        });
    }
}
