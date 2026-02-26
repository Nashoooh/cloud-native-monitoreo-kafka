package com.example.monitoreo_kafka.dto;

import com.example.monitoreo_kafka.entity.UbicacionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UbicacionesVehiculoDto {
    private LocalDateTime fechaConsulta;
    private Integer totalUbicaciones;
    private Set<String> vehiculosConActividad;
    private Map<String, List<UbicacionEntity>> ubicacionesPorVehiculo;
}
