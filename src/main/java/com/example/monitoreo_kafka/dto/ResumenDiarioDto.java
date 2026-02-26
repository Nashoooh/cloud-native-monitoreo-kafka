package com.example.monitoreo_kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenDiarioDto {
    private Long id;
    private String vehiculoId;
    private String placaVehiculo;
    private LocalDate fechaResumen;
    private Long totalUbicaciones;
    private List<String> ciudadesVisitadas;
    private Long totalParadas;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private Double velocidadPromedio;
    private Double pasajerosPromedio;
    private String ruta;
    private LocalDateTime fechaCreacion;
}
