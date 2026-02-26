package com.example.monitoreo_kafka.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO para horario de vehículo consumido desde Kafka
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioVehiculo {
    
    private String vehiculoId;
    private String placaVehiculo;
    private String paradaId;
    private String nombreParada;
    private String direccionParada;
    
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horarioEstimado;
    
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horarioReal;
    
    private Integer retrasoMinutos; // Negativo = adelanto, Positivo = retraso
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    private String ruta;
    private Integer secuenciaParada;
    private String estado; // ESTIMADO, LLEGANDO, LLEGADO, PASADO
}
