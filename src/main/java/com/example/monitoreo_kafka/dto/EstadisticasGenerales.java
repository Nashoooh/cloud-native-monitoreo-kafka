package com.example.monitoreo_kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadisticasGenerales {
    private Long totalUbicacionesRegistradas;
    private Long totalHorariosRegistrados;
    private Long totalResumenesDiarios;
    private Integer vehiculosActivos;
    private List<String> ciudadesVisitadas;
    private LocalDateTime ultimaActividad;
    private List<String> vehiculosIds;
    private LocalDateTime fechaGeneracion;
}
