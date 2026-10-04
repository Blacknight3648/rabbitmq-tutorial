package com.example.rabbitmq_tutorials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import java.util.Scanner;

@SpringBootApplication
public class RabbitmqTutorialsApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(RabbitmqTutorialsApplication.class, args);

		Sender sender = ctx.getBean(Sender.class);

		System.out.println("\n" + "=".repeat(60));
		System.out.println("RabbitMQ = Hello world con Spring boot");
		System.out.println("=".repeat(60));
		System.out.println("[]Aplicacion iniciada correctamente");
		System.out.println("[]RabbitMQ conectado en el localhost:5762");
		System.out.println("[]Cola 'hello' lista para usar \n");

		Scanner scanner = new Scanner(System.in);
		boolean running = true;

		while (running){
			System.out.println("\n [----Menu----]");
			System.out.println("1. Enviar Mensaje");
			System.out.println("2. Enviar multiples mensajes");
			System.out.println("3. salir");
			System.out.println("Seleccione una opcion (1-3)");

			String opcion = scanner.nextLine();

			switch (opcion){
				case "1":
					System.out.println("[->] Ingrese el mensaje a enviar: ");
					String mensaje = scanner.nextLine();
					sender.sendMessage(mensaje);
					break;
				case "2":
					System.out.println("¿Cuantos mensajes?");
					try{
						int count = Integer.parseInt(scanner.nextLine());
						for (int i = 1; i <= count; i++) {
							sender.sendMessage("Mensaje numero " + i + " - Hello RabbitMQ");
							Thread.sleep(500);
						}
						System.out.println("[+] Se enviaron " + count + " mensajes.");
					}catch(NumberFormatException e){
						System.out.println("[x] Número invalido");
					}catch(InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					break;
				case "3":
					running = false;
					System.out.println("[->] Saliendo...");
					break;
				default:
					System.out.println("[X] Opcion no valida");
					break;
			}
		}

		scanner.close();
		System.exit(0);

	}

}
