package com.example.monitoreo_kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonitoreoKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoreoKafkaApplication.class, args);
		System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
		System.out.println("║   🚀 MICROSERVICIO DE MONITORIZACIÓN INICIADO                     ║");
		System.out.println("║   📡 Puerto: 8083                                                 ║");
		System.out.println("║   📊 Consumiendo de Kafka: ubicaciones_vehiculos, horarios       ║");
		System.out.println("║   💾 Modo: SIMULACIÓN (logs en lugar de BD)                       ║");
		System.out.println("║   ⏰ Resumen diario programado: 23:59                             ║");
		System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
	}

}

