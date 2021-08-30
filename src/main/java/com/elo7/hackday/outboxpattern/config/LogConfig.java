package com.elo7.hackday.outboxpattern.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
class LogConfig {

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    Logger logger(InjectionPoint injectionPoint){
        return LoggerFactory.getLogger(injectionPoint.getMethodParameter().getContainingClass());
    }
}
