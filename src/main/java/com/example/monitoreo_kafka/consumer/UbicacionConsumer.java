package com.example.monitoreo_kafka.consumer;

import com.example.monitoreo_kafka.dto.UbicacionVehiculo;
import com.example.monitoreo_kafka.service.PersistenciaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Consumer de Kafka para tópico ubicaciones_vehiculos
 * Consume ubicaciones y las persiste en la base de datos
 */
@Slf4j
@Component
public class UbicacionConsumer {
    
    @Autowired
    private PersistenciaService persistenciaService;
    
    @KafkaListener(
        topics = "ubicaciones_vehiculos",
        groupId = "monitorizacion-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumirUbicacion(UbicacionVehiculo ubicacion, Acknowledgment acknowledgment) {
        try {
            log.info("📍 Ubicación recibida: Vehículo={}, Placa={}, Lat={}, Lon={}, Velocidad={} km/h, Estado={}, Ciudad={}", 
                ubicacion.getVehiculoId(),
                ubicacion.getPlacaVehiculo(),
                ubicacion.getLatitud(),
                ubicacion.getLongitud(),
                ubicacion.getVelocidad(),
                ubicacion.getEstado(),
                ubicacion.getCiudad()
            );
            
            // Persistir en BD (o simular con logs)
            persistenciaService.guardarUbicacion(ubicacion);
            
            log.info("✅ Ubicación procesada exitosamente para vehículo: {}", ubicacion.getVehiculoId());
            
            // Confirmar mensaje procesado
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("❌ Error al procesar ubicación para vehículo {}: {}", 
                ubicacion.getVehiculoId(), e.getMessage(), e);
            // No hacer acknowledge para reprocesar el mensaje
        }
    }
}
