package com.elo7.hackday.outboxpattern.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import static java.util.Optional.ofNullable;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
class LogConfig {

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    Logger logger(InjectionPoint ip) {
        Class<?> clazz = ofNullable(ip.getField())
                .map(Field::getDeclaringClass)
                .orElseGet(() -> ofNullable(ip.getMethodParameter())
                        .<Class>map(MethodParameter::getContainingClass)
                        .orElseThrow(IllegalArgumentException::new));

        return LoggerFactory.getLogger(clazz);
    }
}
