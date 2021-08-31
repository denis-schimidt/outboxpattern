package com.elo7.hackday.outboxpattern.repository.fragments;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.stream.Stream;

public class StreamRepositoryImpl<T> implements StreamRepository<T> {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Stream<T> streamAll(Class<T> clazz) {
        return entityManager
                .createQuery("SELECT t FROM " + clazz.getSimpleName() + " t WHERE t.retriesToDlqTopic = 0")
                .getResultStream();
    }

    @Override
    public void clearFirstLevelCache() {
        entityManager.clear();
    }
}
