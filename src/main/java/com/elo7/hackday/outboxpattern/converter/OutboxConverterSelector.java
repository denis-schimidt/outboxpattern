package com.elo7.hackday.outboxpattern.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OutboxConverterSelector<T> {

    private Map<String, OutboxConverter<T>> outboxConvertersByEvent;

    @Autowired
    public OutboxConverterSelector(List<OutboxConverter<T>> allOutboxConverters) {
        this.outboxConvertersByEvent = allOutboxConverters
                .stream()
                .collect(Collectors.toMap(OutboxConverter::getEventName, Function.identity()));
    }

    public Optional<OutboxConverter<T>> selectConverter(String eventName) {
        return Optional.ofNullable(outboxConvertersByEvent.get(eventName));
    }
}
