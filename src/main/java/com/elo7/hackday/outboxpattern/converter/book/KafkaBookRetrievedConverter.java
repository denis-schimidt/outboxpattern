package com.elo7.hackday.outboxpattern.converter.book;

import com.elo7.hackday.outboxpattern.converter.OutboxConverter;
import com.elo7.hackday.outboxpattern.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class KafkaBookRetrievedConverter extends OutboxConverter<Book> {

    @Autowired
    private KafkaBookEventConverterDefault converter;

    @Override
    protected String toKafkaEvent(Book book) {
        return converter.convertFrom(book, getEventId().toString(), getCreatedAt());
    }

    @Override
    public String getEventName() {
        return "bookRetrieved";
    }
}
