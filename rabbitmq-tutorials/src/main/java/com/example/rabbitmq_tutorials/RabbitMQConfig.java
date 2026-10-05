package com.example.rabbitmq_tutorials;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

@Configuration
public class RabbitMQConfig {

    public static final String ORDERS_EXCHAGE = "orders.exchange";
    public static final String ORDER_QUEUE = "order.queue";
    public static final String ORDERS_ROUTING_KEY = "order.create";

    public static final String DLX_EXCHANGE = "dlx.exchange";
    public static final String DLQ_QUEUE = "dlq.queue";
    public static final String DLX_ROUTING_KEY = "order.dead";

    @Bean
    public DirectExchange ordersExchange() {
        return new DirectExchange(ORDERS_EXCHAGE, true, false);
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE)
                .withArgument("x-message-ttl", 30000)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLX_ROUTING_KEY)
                .withArgument("x-max-length", 1000)
                .build();
    }

    @Bean
    public Binding ordersBinding(@Qualifier("orderQueue") Queue orderQueue, DirectExchange ordersExchange) {
        return BindingBuilder.bind(orderQueue)
                .to(ordersExchange)
                .with(ORDERS_ROUTING_KEY);
    }

    @Bean
    public FanoutExchange deadLetterExchange (){

        return new FanoutExchange(DLX_EXCHANGE, true, false);

    }

    @Bean
    public Queue deadLetterQueue(){
        
        return QueueBuilder.durable(DLQ_QUEUE)
            .withArgument("x-message-ttl", 86400000)
            .build();
    
    }

    @Bean
    public Binding deadLetterBinding(@Qualifier("deadLetterQueue") Queue deadLetterQueue, FanoutExchange deadLetterExchange){
        return BindingBuilder.bind(deadLetterQueue)
            .to(deadLetterExchange);
    }



    @Bean
    public Queue myQueue() {
        return new Queue("hello", false);
    }

}
