package com.example.monitoreo_kafka.service;

import com.example.monitoreo_kafka.dto.HorarioVehiculo;
import com.example.monitoreo_kafka.dto.UbicacionVehiculo;
import com.example.monitoreo_kafka.entity.HorarioEntity;
import com.example.monitoreo_kafka.entity.UbicacionEntity;
import com.example.monitoreo_kafka.repository.HorarioRepository;
import com.example.monitoreo_kafka.repository.UbicacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Servicio para persistir ubicaciones y horarios
 * Puede trabajar en modo SIMULACION (logs) o modo BD real
 */
@Slf4j
@Service
public class PersistenciaService {
    
    @Value("${app.modo.simulacion:true}")
    private boolean modoSimulacion;
    
    @Autowired(required = false)
    private UbicacionRepository ubicacionRepository;
    
    @Autowired(required = false)
    private HorarioRepository horarioRepository;
    
    /**
     * Guarda una ubicación (simulado con logs o en BD real)
     */
    @Transactional
    public void guardarUbicacion(UbicacionVehiculo ubicacion) {
        if (modoSimulacion) {
            simularInsertUbicacion(ubicacion);
        } else {
            insertarUbicacionReal(ubicacion);
        }
    }
    
    /**
     * Guarda un horario (simulado con logs o en BD real)
     */
    @Transactional
    public void guardarHorario(HorarioVehiculo horario) {
        if (modoSimulacion) {
            simularInsertHorario(horario);
        } else {
            insertarHorarioReal(horario);
        }
    }
    
    // ========== MODO SIMULACIÓN (LOGS) ==========
    
    private void simularInsertUbicacion(UbicacionVehiculo ubicacion) {
        log.info("💾 [SIMULACIÓN] INSERT INTO UBICACIONES_VEHICULO:");
        log.info("   └─ VEHICULO_ID: {}", ubicacion.getVehiculoId());
        log.info("   └─ PLACA_VEHICULO: {}", ubicacion.getPlacaVehiculo());
        log.info("   └─ LATITUD: {}", ubicacion.getLatitud());
        log.info("   └─ LONGITUD: {}", ubicacion.getLongitud());
        log.info("   └─ VELOCIDAD: {} km/h", ubicacion.getVelocidad());
        log.info("   └─ DIRECCION: {}", ubicacion.getDireccion());
        log.info("   └─ CIUDAD: {}", ubicacion.getCiudad());
        log.info("   └─ ESTADO: {}", ubicacion.getEstado());
        log.info("   └─ TIMESTAMP_UBICACION: {}", ubicacion.getTimestamp());
        log.info("   └─ CONDUCTOR: {}", ubicacion.getConductor());
        log.info("   └─ PASAJEROS: {}", ubicacion.getPasajeros());
        log.info("   └─ RUTA: {}", ubicacion.getRuta());
        log.info("   └─ FECHA_REGISTRO: {}", LocalDateTime.now());
        log.info("   ✓ Registro simulado exitosamente");
    }
    
    private void simularInsertHorario(HorarioVehiculo horario) {
        log.info("💾 [SIMULACIÓN] INSERT INTO HORARIOS_VEHICULO:");
        log.info("   └─ VEHICULO_ID: {}", horario.getVehiculoId());
        log.info("   └─ PLACA_VEHICULO: {}", horario.getPlacaVehiculo());
        log.info("   └─ PARADA_ID: {}", horario.getParadaId());
        log.info("   └─ NOMBRE_PARADA: {}", horario.getNombreParada());
        log.info("   └─ DIRECCION_PARADA: {}", horario.getDireccionParada());
        log.info("   └─ HORARIO_ESTIMADO: {}", convertirLocalTimeADateTime(horario.getHorarioEstimado()));
        log.info("   └─ HORARIO_REAL: {}", convertirLocalTimeADateTime(horario.getHorarioReal()));
        log.info("   └─ RETRASO_MINUTOS: {}", horario.getRetrasoMinutos());
        log.info("   └─ TIMESTAMP_REGISTRO: {}", horario.getTimestamp());
        log.info("   └─ RUTA: {}", horario.getRuta());
        log.info("   └─ SECUENCIA_PARADA: {}", horario.getSecuenciaParada());
        log.info("   └─ ESTADO: {}", horario.getEstado());
        log.info("   └─ FECHA_REGISTRO: {}", LocalDateTime.now());
        log.info("   ✓ Registro simulado exitosamente");
    }
    
    // ========== MODO BD REAL ==========
    
    private void insertarUbicacionReal(UbicacionVehiculo ubicacion) {
        UbicacionEntity entity = UbicacionEntity.builder()
            .vehiculoId(ubicacion.getVehiculoId())
            .placaVehiculo(ubicacion.getPlacaVehiculo())
            .latitud(ubicacion.getLatitud())
            .longitud(ubicacion.getLongitud())
            .velocidad(ubicacion.getVelocidad())
            .direccion(ubicacion.getDireccion())
            .ciudad(ubicacion.getCiudad())
            .estado(ubicacion.getEstado())
            .timestampUbicacion(ubicacion.getTimestamp())
            .conductor(ubicacion.getConductor())
            .pasajeros(ubicacion.getPasajeros())
            .ruta(ubicacion.getRuta())
            .build();
        
        UbicacionEntity saved = ubicacionRepository.save(entity);
        log.info("💾 Ubicación guardada en BD con ID: {}", saved.getId());
    }
    
    private void insertarHorarioReal(HorarioVehiculo horario) {
        HorarioEntity entity = HorarioEntity.builder()
            .vehiculoId(horario.getVehiculoId())
            .placaVehiculo(horario.getPlacaVehiculo())
            .paradaId(horario.getParadaId())
            .nombreParada(horario.getNombreParada())
            .direccionParada(horario.getDireccionParada())
            .horarioEstimado(convertirLocalTimeADateTime(horario.getHorarioEstimado()))
            .horarioReal(convertirLocalTimeADateTime(horario.getHorarioReal()))
            .retrasoMinutos(horario.getRetrasoMinutos())
            .timestampRegistro(horario.getTimestamp())
            .ruta(horario.getRuta())
            .secuenciaParada(horario.getSecuenciaParada())
            .estado(horario.getEstado())
            .build();
        
        HorarioEntity saved = horarioRepository.save(entity);
        log.info("💾 Horario guardado en BD con ID: {}", saved.getId());
    }
    
    // ========== UTILIDADES ==========
    
    private LocalDateTime convertirLocalTimeADateTime(java.time.LocalTime time) {
        if (time == null) return null;
        return LocalDateTime.of(LocalDate.now(), time);
    }
}
