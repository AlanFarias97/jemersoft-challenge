package com.challenge.config.event;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    @RabbitListener(queues = "main-queue")
    public void receiveEvent(String message) {
        // Procesa el evento recibido
        System.out.println("Received event: " + message);
    }
}