package com.example.monitoreo_kafka.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity JPA para tabla RESUMEN_DIARIO_UBICACIONES
 * Almacena el resumen agregado generado al final del día
 */
@Entity
@Table(name = "RESUMEN_DIARIO_UBICACIONES",
       uniqueConstraints = @UniqueConstraint(columnNames = {"VEHICULO_ID", "FECHA_RESUMEN"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenDiarioEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resumen_seq")
    @SequenceGenerator(name = "resumen_seq", sequenceName = "RESUMEN_DIARIO_SEQ", allocationSize = 1)
    private Long id;
    
    @Column(name = "VEHICULO_ID", nullable = false, length = 50)
    private String vehiculoId;
    
    @Column(name = "PLACA_VEHICULO", nullable = false, length = 20)
    private String placaVehiculo;
    
    @Column(name = "FECHA_RESUMEN", nullable = false)
    private LocalDate fechaResumen;
    
    @Column(name = "TOTAL_UBICACIONES", nullable = false)
    private Long totalUbicaciones;
    
    @Column(name = "CIUDADES_VISITADAS", length = 500)
    private String ciudadesVisitadas;
    
    @Column(name = "TOTAL_PARADAS", nullable = false)
    private Long totalParadas;
    
    @Column(name = "HORA_INICIO")
    private LocalDateTime horaInicio;
    
    @Column(name = "HORA_FIN")
    private LocalDateTime horaFin;
    
    @Column(name = "VELOCIDAD_PROMEDIO", precision = 6, scale = 2)
    private Double velocidadPromedio;
    
    @Column(name = "PASAJEROS_PROMEDIO", precision = 6, scale = 2)
    private Double pasajerosPromedio;
    
    @Column(name = "RUTA", length = 100)
    private String ruta;
    
    @Column(name = "FECHA_CREACION", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
