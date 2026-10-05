package com.example.rabbitmq_tutorials;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController 
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired 
    private RabbitTemplate rabbitTemplate;
    
    @PostMapping("/send")
    public Map<String, String> sendOrder(@RequestBody Map<String, String> payload){
        String orderId= payload.getOrDefault("orderId", "ORD-" + System.currentTimeMillis());
        String customerName = payload.getOrDefault("customerName", "Unknown");

        String message = String.format("Order ID: %s | Cliente: %s | Hora: %s", orderId, customerName, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        rabbitTemplate.convertAndSend(
        
            RabbitMQConfig.ORDERS_EXCHAGE,
            RabbitMQConfig.ORDERS_ROUTING_KEY,
            message

        );

        Map<String, String> response = new HashMap<>();

        response.put("status", "Orden enviada");
        response.put("orderId", orderId);
        response.put("message", message);

        return response;
    }
    
    @GetMapping("/status")
    public Map<String, String> getStatus(){

        Map<String, String> status = new HashMap<>();
        status.put("backend", "Online");
        status.put("RabbitMQ", "Conectado");
        status.put("timestamp", LocalDateTime.now().toString());

        return status;

    }

}
