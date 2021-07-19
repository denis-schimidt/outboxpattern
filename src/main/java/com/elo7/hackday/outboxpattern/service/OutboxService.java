package com.elo7.hackday.outboxpattern.service;

import com.elo7.hackday.outboxpattern.repository.OutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class OutboxService {

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 60000)
    public void publishEventsOnkafka() {

        outboxRepository.findAll()
            .forEach(outbox-> {

                ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("bookCreated", outbox.getAggregateId(),  outbox.toString());
                future.addCallback(new ListenableFutureCallback<>() {

                    @Override
                    public void onSuccess(SendResult<String, String> result) {
                        outboxRepository.delete(outbox);

                        System.out.println("Sent message=[" + outbox + "] with offset=[" + result.getRecordMetadata().offset() + "]");
                    }

                    @Override
                    public void onFailure(Throwable ex) {
                        System.out.println("Unable to send message=[" + outbox + "] due to : " + ex.getMessage());
                    }
                });
            });
    }
}
