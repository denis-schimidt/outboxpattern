package com.elo7.hackday.outboxpattern.service;

import com.elo7.hackday.outboxpattern.converter.OutboxConverterSelector;
import com.elo7.hackday.outboxpattern.entity.Outbox;
import com.elo7.hackday.outboxpattern.repository.JpaFirstLevelCacheCleaner;
import com.elo7.hackday.outboxpattern.repository.OutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.stream.Stream;

@Service
public class OutboxService<T> {

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private OutboxSender outboxSender;

    @Autowired
    private OutboxConverterSelector<T> outboxConverterSelector;

    @Autowired
    private JpaFirstLevelCacheCleaner jpaCleaner;

    public void createOutbox(Serializable kafkaPartitiondId, T originalEntity, String eventName) {

        Outbox outbox = outboxConverterSelector.selectConverter(eventName)
                .map(converter ->  converter.toOutbox(kafkaPartitiondId, originalEntity))
                .orElseThrow(()-> new RuntimeException("No converter selected by key -> " + eventName));

        outboxRepository.save(outbox);
    }

    @Scheduled(fixedRateString = "${fixed-rate.in.milliseconds}")
    @Transactional
    public void publishEventsOnkafka() {

        try (Stream<Outbox> outboxStream = outboxRepository.streamAll()) {

            outboxStream.forEach(outbox -> {
                outboxSender.sendToKafka(outbox);
                jpaCleaner.clearFirstLevelCacheWhenNecessary();
            });
        }
    }
}
