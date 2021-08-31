package com.elo7.hackday.outboxpattern.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JpaFirstLevelCacheCleaner {
    private int objectsInFirstLevelCache;
    private int maxObjectsInFirstLevelJpaCache;
    private OutboxRepository outboxRepository;

    @Autowired
    JpaFirstLevelCacheCleaner(@Value("${max-objects-in-first-level-jpa-cache}") Integer maxObjectsInFirstLevelJpaCache, OutboxRepository outboxRepository) {
        this.maxObjectsInFirstLevelJpaCache = maxObjectsInFirstLevelJpaCache.intValue();
        this.outboxRepository = outboxRepository;
    }

    public void clearFirstLevelCacheWhenNecessary() {
        objectsInFirstLevelCache++;

        if (objectsInFirstLevelCache > maxObjectsInFirstLevelJpaCache) {
            outboxRepository.clearFirstLevelCache();
            objectsInFirstLevelCache = 0;
        }
    }
}
