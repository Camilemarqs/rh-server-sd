package com.rh.rh.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EventoService {

    private static final Logger log = LoggerFactory.getLogger(EventoService.class);

    private final RabbitTemplate rabbitTemplate;

    public EventoService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicar(String tipo, Map<String, Object> dados) {
        try {
            RhEvent evento = new RhEvent(tipo, "servidor", dados);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_EVENTOS, "", evento);
        } catch (Exception e) {
            log.warn("Falha ao publicar evento {}: {}", tipo, e.getMessage());
        }
    }
}
