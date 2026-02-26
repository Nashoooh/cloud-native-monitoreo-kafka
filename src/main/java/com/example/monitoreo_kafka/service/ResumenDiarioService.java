package com.example.monitoreo_kafka.service;

import com.example.monitoreo_kafka.entity.HorarioEntity;
import com.example.monitoreo_kafka.entity.ResumenDiarioEntity;
import com.example.monitoreo_kafka.entity.UbicacionEntity;
import com.example.monitoreo_kafka.repository.HorarioRepository;
import com.example.monitoreo_kafka.repository.ResumenDiarioRepository;
import com.example.monitoreo_kafka.repository.UbicacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio para generar el resumen diario de ubicaciones
 * Se ejecuta automáticamente a las 23:59 cada día
 */
@Slf4j
@Service
public class ResumenDiarioService {
    
    @Value("${app.modo.simulacion:true}")
    private boolean modoSimulacion;
    
    @Autowired(required = false)
    private UbicacionRepository ubicacionRepository;
    
    @Autowired(required = false)
    private HorarioRepository horarioRepository;
    
    @Autowired(required = false)
    private ResumenDiarioRepository resumenRepository;
    
    /**
     * Ejecuta la generación del resumen diario a las 23:59
     * Para testing, también se puede invocar manualmente
     */
    @Scheduled(cron = "0 59 23 * * *") // 23:59 todos los días
    public void generarResumenDiarioAutomatico() {
        log.info("⏰ Inicio programado de generación de resumen diario");
        generarResumenDiario(LocalDate.now());
    }
    
    /**
     * Genera el resumen diario para una fecha específica
     * Puede ser invocado manualmente para testing
     */
    @Transactional
    public void generarResumenDiario(LocalDate fecha) {
        log.info("═══════════════════════════════════════════════════════════");
        log.info("📊 INICIANDO GENERACIÓN DE RESUMEN DIARIO");
        log.info("📅 Fecha: {}", fecha);
        log.info("═══════════════════════════════════════════════════════════");
        
        if (modoSimulacion) {
            generarResumenSimulado(fecha);
        } else {
            generarResumenReal(fecha);
        }
        
        log.info("═══════════════════════════════════════════════════════════");
        log.info("🎉 RESUMEN DIARIO COMPLETADO");
        log.info("═══════════════════════════════════════════════════════════");
    }
    
    // ========== MODO SIMULACIÓN ==========
    
    private void generarResumenSimulado(LocalDate fecha) {
        log.info("🔄 [SIMULACIÓN] Generando resumen para vehículos activos...");
        
        // Simular datos de ejemplo para 5 vehículos
        String[] vehiculos = {"VEH-001", "VEH-002", "VEH-003", "VEH-004", "VEH-005"};
        
        for (String vehiculoId : vehiculos) {
            simularResumenVehiculo(vehiculoId, fecha);
        }
    }
    
    private void simularResumenVehiculo(String vehiculoId, LocalDate fecha) {
        log.info("");
        log.info("🚌 Procesando vehículo: {}", vehiculoId);
        log.info("─────────────────────────────────────────────────────────");
        
        // Simular cálculos agregados
        int totalUbicaciones = (int) (Math.random() * 200) + 100; // 100-300
        int totalParadas = (int) (Math.random() * 20) + 10; // 10-30
        double velocidadPromedio = Math.round((Math.random() * 30 + 30) * 100.0) / 100.0; // 30-60 km/h
        double pasajerosPromedio = Math.round((Math.random() * 20 + 15) * 100.0) / 100.0; // 15-35
        
        String[] ciudades = {"Lima", "Callao", "San Miguel"};
        String ciudadesVisitadas = String.join(", ", ciudades);
        
        LocalDateTime horaInicio = fecha.atTime(6, 0);
        LocalDateTime horaFin = fecha.atTime(22, 0);
        
        log.info("💾 [SIMULACIÓN] INSERT INTO RESUMEN_DIARIO_UBICACIONES:");
        log.info("   └─ VEHICULO_ID: {}", vehiculoId);
        log.info("   └─ PLACA_VEHICULO: ABC-{}", vehiculoId.substring(4));
        log.info("   └─ FECHA_RESUMEN: {}", fecha);
        log.info("   └─ TOTAL_UBICACIONES: {}", totalUbicaciones);
        log.info("   └─ CIUDADES_VISITADAS: {}", ciudadesVisitadas);
        log.info("   └─ TOTAL_PARADAS: {}", totalParadas);
        log.info("   └─ HORA_INICIO: {}", horaInicio);
        log.info("   └─ HORA_FIN: {}", horaFin);
        log.info("   └─ VELOCIDAD_PROMEDIO: {} km/h", velocidadPromedio);
        log.info("   └─ PASAJEROS_PROMEDIO: {}", pasajerosPromedio);
        log.info("   └─ RUTA: Ruta {} - Norte", vehiculoId.charAt(4));
        log.info("   └─ FECHA_CREACION: {}", LocalDateTime.now());
        log.info("   ✓ Resumen simulado exitosamente");
        
        log.info("");
        log.info("📈 ESTADÍSTICAS DEL DÍA:");
        log.info("   • Ubicaciones registradas: {}", totalUbicaciones);
        log.info("   • Paradas visitadas: {}", totalParadas);
        log.info("   • Tiempo de operación: {} horas", 16);
        log.info("   • Ciudades: {}", ciudadesVisitadas);
        log.info("   • Velocidad promedio: {} km/h", velocidadPromedio);
        log.info("   • Ocupación promedio: {} pasajeros", pasajerosPromedio);
    }
    
    // ========== MODO BD REAL ==========
    
    private void generarResumenReal(LocalDate fecha) {
        List<String> vehiculosActivos = ubicacionRepository.findDistinctVehiculoIdsByFecha(fecha);
        
        log.info("📋 Vehículos activos encontrados: {}", vehiculosActivos.size());
        
        if (vehiculosActivos.isEmpty()) {
            log.warn("⚠️ No hay vehículos activos para la fecha: {}", fecha);
            return;
        }
        
        for (String vehiculoId : vehiculosActivos) {
            try {
                ResumenDiarioEntity resumen = calcularResumenReal(vehiculoId, fecha);
                ResumenDiarioEntity saved = resumenRepository.save(resumen);
                
                log.info("✅ Resumen guardado para vehículo: {} (ID: {})", vehiculoId, saved.getId());
                imprimirEstadisticas(resumen);
                
            } catch (Exception e) {
                log.error("❌ Error al generar resumen para {}: {}", vehiculoId, e.getMessage(), e);
            }
        }
    }
    
    private ResumenDiarioEntity calcularResumenReal(String vehiculoId, LocalDate fecha) {
        // Obtener todas las ubicaciones del día
        List<UbicacionEntity> ubicaciones = ubicacionRepository
            .findByVehiculoIdAndFecha(vehiculoId, fecha);
        
        // Obtener todos los horarios del día
        List<HorarioEntity> horarios = horarioRepository
            .findByVehiculoIdAndFecha(vehiculoId, fecha);
        
        if (ubicaciones.isEmpty()) {
            throw new IllegalStateException("No hay ubicaciones para el vehículo: " + vehiculoId);
        }
        
        // Calcular agregados
        Set<String> ciudades = ubicaciones.stream()
            .map(UbicacionEntity::getCiudad)
            .filter(c -> c != null && !c.isEmpty())
            .collect(Collectors.toSet());
        
        double velocidadPromedio = ubicaciones.stream()
            .filter(u -> u.getVelocidad() != null)
            .mapToDouble(UbicacionEntity::getVelocidad)
            .average()
            .orElse(0.0);
        
        double pasajerosPromedio = ubicaciones.stream()
            .filter(u -> u.getPasajeros() != null)
            .mapToInt(UbicacionEntity::getPasajeros)
            .average()
            .orElse(0.0);
        
        return ResumenDiarioEntity.builder()
            .vehiculoId(vehiculoId)
            .placaVehiculo(ubicaciones.get(0).getPlacaVehiculo())
            .fechaResumen(fecha)
            .totalUbicaciones(ubicaciones.size())
            .ciudadesVisitadas(String.join(", ", ciudades))
            .totalParadas(horarios.size())
            .horaInicio(ubicaciones.get(0).getTimestampUbicacion())
            .horaFin(ubicaciones.get(ubicaciones.size() - 1).getTimestampUbicacion())
            .velocidadPromedio(Math.round(velocidadPromedio * 100.0) / 100.0)
            .pasajerosPromedio(Math.round(pasajerosPromedio * 100.0) / 100.0)
            .ruta(ubicaciones.get(0).getRuta())
            .build();
    }
    
    private void imprimirEstadisticas(ResumenDiarioEntity resumen) {
        log.info("📈 ESTADÍSTICAS DEL DÍA:");
        log.info("   • Vehículo: {}", resumen.getVehiculoId());
        log.info("   • Ubicaciones registradas: {}", resumen.getTotalUbicaciones());
        log.info("   • Paradas visitadas: {}", resumen.getTotalParadas());
        log.info("   • Ciudades: {}", resumen.getCiudadesVisitadas());
        log.info("   • Velocidad promedio: {} km/h", resumen.getVelocidadPromedio());
        log.info("   • Ocupación promedio: {} pasajeros", resumen.getPasajerosPromedio());
    }
}
