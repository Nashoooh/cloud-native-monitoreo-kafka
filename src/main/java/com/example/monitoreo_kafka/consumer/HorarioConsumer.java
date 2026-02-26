package com.example.monitoreo_kafka.consumer;

import com.example.monitoreo_kafka.dto.HorarioVehiculo;
import com.example.monitoreo_kafka.service.PersistenciaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Consumer de Kafka para tópico horarios
 * Consume horarios y los persiste en la base de datos
 */
@Slf4j
@Component
public class HorarioConsumer {
    
    @Autowired
    private PersistenciaService persistenciaService;
    
    @KafkaListener(
        topics = "horarios",
        groupId = "monitorizacion-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumirHorario(HorarioVehiculo horario, Acknowledgment acknowledgment) {
        try {
            log.info("🕐 Horario recibido: Vehículo={}, Parada={}, HorarioEst={}, HorarioReal={}, Retraso={} min, Estado={}", 
                horario.getVehiculoId(),
                horario.getNombreParada(),
                horario.getHorarioEstimado(),
                horario.getHorarioReal(),
                horario.getRetrasoMinutos(),
                horario.getEstado()
            );
            
            // Persistir en BD (o simular con logs)
            persistenciaService.guardarHorario(horario);
            
            log.info("✅ Horario procesado exitosamente para vehículo: {} en parada: {}", 
                horario.getVehiculoId(), horario.getNombreParada());
            
            // Confirmar mensaje procesado
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("❌ Error al procesar horario para vehículo {} en parada {}: {}", 
                horario.getVehiculoId(), horario.getNombreParada(), e.getMessage(), e);
            // No hacer acknowledge para reprocesar el mensaje
        }
    }
}
