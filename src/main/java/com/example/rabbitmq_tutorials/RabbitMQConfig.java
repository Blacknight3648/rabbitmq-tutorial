package com.example.rabbitmq_tutorials;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;


@Configuration
public class RabbitMQConfig {

    public static final String ORDERS_EXCHAGE = "orders.exchange";
    public static final String ORDER_QUEUE = "order";
    public static final String SHIPPING_QUEUE = "shipping";
    public static final String SLAES_QUEUE = "sales";

    
    
    @Bean 
    public Queue myQueue() {
        return new Queue("hello", false);
    }



}
