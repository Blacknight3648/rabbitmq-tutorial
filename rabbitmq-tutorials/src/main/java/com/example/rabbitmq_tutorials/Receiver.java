package com.example.rabbitmq_tutorials;

import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Receiver {

    private final AtomicInteger attemptCount = new AtomicInteger(0);

    @RabbitListener(queues = "hello")
    public void receiveMessage(String message) {
        int attempt = attemptCount.incrementAndGet();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

        System.out.println(String.format("['%s'] [Intento %d de 3] Mensaje recibido: %s",
                timestamp, attempt, message));

        try {
            // Si el mensaje contiene "error" o "fallo", simulamos un error para probar los reintentos
            if (message.toLowerCase().contains("error") || message.toLowerCase().contains("fallo")) {
                throw new RuntimeException("Fallo simulado para probar reintentos");
            }

            // Procesamiento exitoso
            System.out.println(String.format("['%s'] [Éxito] Mensaje procesado correctamente en el intento %d",
                    timestamp, attempt));
            attemptCount.set(0); // Reiniciar contador para el siguiente mensaje

        } catch (Exception e) {
            System.err.println(String.format("['%s'] [Intento %d falló] Motivo: %s",
                    timestamp, attempt, e.getMessage()));

            // Si se alcanzaron los 3 intentos totales (1 inicial + 2 reintentos)
            if (attempt >= 3) {
                System.err.println(String.format("['%s'] [Reintentos agotados] Se alcanzó el límite de 3 intentos.",
                        timestamp));
                attemptCount.set(0); // Reiniciar para no afectar mensajes futuros
            }
            throw new RuntimeException(e);
        }
    }

}
