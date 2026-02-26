package com.example.monitoreo_kafka.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity JPA para tabla HORARIOS_VEHICULO
 * Almacena todos los horarios consumidos del tópico Kafka
 */
@Entity
@Table(name = "HORARIOS_VEHICULO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "horario_seq")
    @SequenceGenerator(name = "horario_seq", sequenceName = "HORARIO_SEQ", allocationSize = 1)
    private Long id;
    
    @Column(name = "VEHICULO_ID", nullable = false, length = 50)
    private String vehiculoId;
    
    @Column(name = "PLACA_VEHICULO", nullable = false, length = 20)
    private String placaVehiculo;
    
    @Column(name = "PARADA_ID", nullable = false, length = 50)
    private String paradaId;
    
    @Column(name = "NOMBRE_PARADA", nullable = false, length = 100)
    private String nombreParada;
    
    @Column(name = "DIRECCION_PARADA", length = 200)
    private String direccionParada;
    
    @Column(name = "HORARIO_ESTIMADO", nullable = false)
    private LocalDateTime horarioEstimado;
    
    @Column(name = "HORARIO_REAL", nullable = false)
    private LocalDateTime horarioReal;
    
    @Column(name = "RETRASO_MINUTOS")
    private Integer retrasoMinutos;
    
    @Column(name = "TIMESTAMP_REGISTRO", nullable = false)
    private LocalDateTime timestampRegistro;
    
    @Column(name = "RUTA", length = 100)
    private String ruta;
    
    @Column(name = "SECUENCIA_PARADA")
    private Integer secuenciaParada;
    
    @Column(name = "ESTADO", length = 20)
    private String estado; // ESTIMADO, LLEGANDO, LLEGADO, PASADO
    
    @Column(name = "FECHA_REGISTRO", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;
    
    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}
