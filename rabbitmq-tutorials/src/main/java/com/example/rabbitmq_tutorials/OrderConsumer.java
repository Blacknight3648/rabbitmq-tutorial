package com.example.rabbitmq_tutorials;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.Channel;

@Service
public class OrderConsumer {

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void processOrder(
            String message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            System.out.println("[PROCESADOR] Recibida orden:" + message);
            if (Math.random() < 0.5) {
                throw new RuntimeException("Error simulado al procesar: " + message);
            }

            System.out.println("[Exito] Orden procesada correctamente: " + message);

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            System.out.println("[Error] " + e.getMessage());

            try {
                channel.basicNack(deliveryTag, false, false);
                System.out.println("[-> DLX] Mensaje enviado a Dead Letter Exchange");
            } catch (Exception nackException) {
                nackException.printStackTrace();
            }

        }
    }

    @RabbitListener(queues = RabbitMQConfig.DLQ_QUEUE)
    public void processDLQ(String message) {
        System.out.println("[DLQ] Mensaje en cuarentena: " + message);
        System.out.println(" -> Revisar logs del procesador para diagrama del fallo.");

    }

}
