package com.rh.rh.messaging;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_EVENTOS = "rh.eventos";

    @Bean
    public FanoutExchange eventosExchange() {
        return new FanoutExchange(EXCHANGE_EVENTOS, true, false);
    }
}
