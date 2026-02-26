package com.example.monitoreo_kafka.repository;

import com.example.monitoreo_kafka.entity.HorarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository para operaciones CRUD de horarios
 */
@Repository
public interface HorarioRepository extends JpaRepository<HorarioEntity, Long> {
    
    /**
     * Obtiene todos los horarios de un vehículo en una fecha específica
     */
    @Query("SELECT h FROM HorarioEntity h WHERE h.vehiculoId = :vehiculoId " +
           "AND DATE(h.timestampRegistro) = :fecha ORDER BY h.timestampRegistro")
    List<HorarioEntity> findByVehiculoIdAndFecha(
        @Param("vehiculoId") String vehiculoId,
        @Param("fecha") LocalDate fecha
    );
    
    /**
     * Cuenta horarios por vehículo y fecha
     */
    @Query("SELECT COUNT(h) FROM HorarioEntity h WHERE h.vehiculoId = :vehiculoId " +
           "AND DATE(h.timestampRegistro) = :fecha")
    Long countByVehiculoIdAndFecha(
        @Param("vehiculoId") String vehiculoId,
        @Param("fecha") LocalDate fecha
    );
}
