package com.example.monitoreo_kafka.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para ubicación de vehículo consumida desde Kafka
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UbicacionVehiculo {
    
    private String vehiculoId;
    private String placaVehiculo;
    private Double latitud;
    private Double longitud;
    private Double velocidad;
    private String direccion;
    private String ciudad;
    private String estado; // EN_RUTA, DETENIDO, EN_PARADA
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    private String conductor;
    private Integer pasajeros;
    private String ruta;
}
