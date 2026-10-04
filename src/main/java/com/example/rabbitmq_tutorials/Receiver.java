package com.example.rabbitmq_tutorials;

import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@Component
public class Receiver {

    @RabbitListener(queues = "hello")
    public void receiveMessage(String message) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
            System.out.println("['" + timestamp + "' [] Mensaje recibido: " + message);
        } catch (Exception e) {
            System.err.println("['" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")) + "' [] Error procesando el mensaje     : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
