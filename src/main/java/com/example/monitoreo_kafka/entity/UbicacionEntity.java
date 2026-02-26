package com.example.monitoreo_kafka.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity JPA para tabla UBICACIONES_VEHICULO
 * Almacena todas las ubicaciones consumidas del tópico Kafka
 */
@Entity
@Table(name = "UBICACIONES_VEHICULO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UbicacionEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ubicacion_seq")
    @SequenceGenerator(name = "ubicacion_seq", sequenceName = "UBICACION_SEQ", allocationSize = 1)
    private Long id;
    
    @Column(name = "VEHICULO_ID", nullable = false, length = 50)
    private String vehiculoId;
    
    @Column(name = "PLACA_VEHICULO", nullable = false, length = 20)
    private String placaVehiculo;
    
    @Column(name = "LATITUD", nullable = false, precision = 10, scale = 7)
    private Double latitud;
    
    @Column(name = "LONGITUD", nullable = false, precision = 11, scale = 7)
    private Double longitud;
    
    @Column(name = "VELOCIDAD", precision = 6, scale = 2)
    private Double velocidad;
    
    @Column(name = "DIRECCION", length = 200)
    private String direccion;
    
    @Column(name = "CIUDAD", length = 100)
    private String ciudad;
    
    @Column(name = "ESTADO", length = 20)
    private String estado; // EN_RUTA, DETENIDO, EN_PARADA
    
    @Column(name = "TIMESTAMP_UBICACION", nullable = false)
    private LocalDateTime timestampUbicacion;
    
    @Column(name = "CONDUCTOR", length = 100)
    private String conductor;
    
    @Column(name = "PASAJEROS")
    private Integer pasajeros;
    
    @Column(name = "RUTA", length = 100)
    private String ruta;
    
    @Column(name = "FECHA_REGISTRO", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;
    
    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}
