package com.example.monitoreo_kafka.repository;

import com.example.monitoreo_kafka.entity.UbicacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones CRUD de ubicaciones
 */
@Repository
public interface UbicacionRepository extends JpaRepository<UbicacionEntity, Long> {
    
    /**
     * Obtiene todas las ubicaciones de un vehículo en una fecha específica
     */
    @Query("SELECT u FROM UbicacionEntity u WHERE u.vehiculoId = :vehiculoId " +
           "AND DATE(u.timestampUbicacion) = :fecha ORDER BY u.timestampUbicacion")
    List<UbicacionEntity> findByVehiculoIdAndFecha(
        @Param("vehiculoId") String vehiculoId,
        @Param("fecha") LocalDate fecha
    );
    
    /**
     * Obtiene todos los vehículos activos en una fecha
     */
    @Query("SELECT DISTINCT u.vehiculoId FROM UbicacionEntity u " +
           "WHERE DATE(u.timestampUbicacion) = :fecha")
    List<String> findDistinctVehiculoIdsByFecha(@Param("fecha") LocalDate fecha);
    
    /**
     * Cuenta ubicaciones por vehículo y fecha
     */
    @Query("SELECT COUNT(u) FROM UbicacionEntity u WHERE u.vehiculoId = :vehiculoId " +
           "AND DATE(u.timestampUbicacion) = :fecha")
    Long countByVehiculoIdAndFecha(
        @Param("vehiculoId") String vehiculoId,
        @Param("fecha") LocalDate fecha
    );
    
    // Nuevos métodos para estadísticas y dashboard
    @Query("SELECT DISTINCT u.vehiculoId FROM UbicacionEntity u ORDER BY u.vehiculoId")
    List<String> findDistinctVehiculoIds();
    
    @Query("SELECT DISTINCT u.ciudad FROM UbicacionEntity u WHERE u.ciudad IS NOT NULL ORDER BY u.ciudad")
    List<String> findDistinctCiudades();
    
    Optional<UbicacionEntity> findTopByOrderByFechaRegistroDesc();
    
    // Consultas por fechas
    long countByFechaRegistroBetween(LocalDateTime inicio, LocalDateTime fin);
    
    @Query("SELECT DISTINCT u.vehiculoId FROM UbicacionEntity u WHERE u.fechaRegistro BETWEEN :inicio AND :fin ORDER BY u.vehiculoId")
    List<String> findDistinctVehiculoIdsByFechaRegistroBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    // Ubicaciones por vehículo y tiempo real
    List<UbicacionEntity> findByVehiculoIdAndFechaRegistroGreaterThanEqualOrderByFechaRegistroDesc(String vehiculoId, LocalDateTime fecha);
    
    List<UbicacionEntity> findByFechaRegistroGreaterThanEqualOrderByFechaRegistroDesc(LocalDateTime fecha);
}
