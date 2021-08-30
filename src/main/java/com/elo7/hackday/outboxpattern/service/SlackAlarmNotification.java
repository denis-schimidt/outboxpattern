package com.elo7.hackday.outboxpattern.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SlackAlarmNotification implements AlarmNotification {

    @Autowired
    private Logger logger;

    @Override
    public void sendNotification(String message) {
        logger.info("Sent to SLACK -> " + message);
    }
}
