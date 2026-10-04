package com.example.rabbitmq_tutorials;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void sendMessage(String message) {
        try{
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
            String fullMessage = String.format("%s - %s", timestamp, message);

            rabbitTemplate.convertAndSend("hello", fullMessage);

            System.out.println("[] Mensaje enviado: " + fullMessage);


        }catch(Exception e) {
            System.err.println("[X] Error enviando el mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
