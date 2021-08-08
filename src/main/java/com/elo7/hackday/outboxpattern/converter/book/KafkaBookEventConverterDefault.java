package com.elo7.hackday.outboxpattern.converter.book;

import com.elo7.hackday.outboxpattern.entity.Book;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class KafkaBookEventConverterDefault {

    String convertFrom(Book book, String eventId, LocalDateTime createdAt) {
        JSONObject author = new JSONObject()
                .put("id", book.getAuthor().getId())
                .put("name", book.getAuthor().getName());

        JSONObject payload = new JSONObject()
                .put("title", book.getTitle())
                .put("description", book.getDescription())
                .put("author", author);

        return new JSONObject()
                .put("eventId", eventId)
                .put("kafkaPartitionId", book.getId())
                .put("createdAt", createdAt)
                .put("payload", payload)
                .toString();
    }
}
