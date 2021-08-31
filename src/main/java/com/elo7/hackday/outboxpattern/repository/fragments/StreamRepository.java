package com.elo7.hackday.outboxpattern.repository.fragments;

import java.util.stream.Stream;

/**
 * @Transactional this is important, because Streams can only be opened in a transaction
 * @param <T>
 */

public interface StreamRepository<T> {

    Stream<T> streamAll(Class<T> clazz);

    void clearFirstLevelCache();
}
