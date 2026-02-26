package com.example.monitoreo_kafka.repository;

import com.example.monitoreo_kafka.entity.ResumenDiarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones CRUD de resúmenes diarios
 */
@Repository
public interface ResumenDiarioRepository extends JpaRepository<ResumenDiarioEntity, Long> {
    
    /**
     * Busca un resumen por vehículo y fecha
     */
    List<ResumenDiarioEntity> findByVehiculoIdAndFechaResumen(
        String vehiculoId,
        LocalDate fechaResumen
    );
    
    /**
     * Obtiene todos los resúmenes de una fecha
     */
    List<ResumenDiarioEntity> findByFechaResumenOrderByVehiculoId(LocalDate fechaResumen);
    
    /**
     * Obtiene todos los resúmenes de un vehículo ordenados por fecha
     */
    List<ResumenDiarioEntity> findByVehiculoIdOrderByFechaResumenDesc(String vehiculoId);
    
    /**
     * Obtiene resúmenes desde una fecha específica
     */
    List<ResumenDiarioEntity> findByFechaResumenGreaterThanEqualOrderByFechaResumenDesc(LocalDate fecha);
    
    /**
     * Obtiene los últimos N resúmenes
     */
    @Query("SELECT r FROM ResumenDiarioEntity r ORDER BY r.fechaCreacion DESC")
    List<ResumenDiarioEntity> findTopNOrderByFechaCreacionDesc(@Param("limit") int limit);
}
