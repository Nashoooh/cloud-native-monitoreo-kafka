package com.example.monitoreo_kafka.controller;

import com.example.monitoreo_kafka.dto.EstadisticasGenerales;
import com.example.monitoreo_kafka.dto.ResumenDiarioDto;
import com.example.monitoreo_kafka.dto.UbicacionesVehiculoDto;
import com.example.monitoreo_kafka.service.ReporteService;
import com.example.monitoreo_kafka.service.ResumenDiarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Para permitir acceso desde cualquier origen (útil para EC2)
public class DashboardController {

    private final ReporteService reporteService;
    private final ResumenDiarioService resumenDiarioService;

    /**
     * Endpoint principal del dashboard - estadísticas generales
     * GET /api/dashboard/estadisticas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasGenerales> obtenerEstadisticasGenerales() {
        log.info("🌐 API: Solicitud de estadísticas generales");
        EstadisticasGenerales estadisticas = reporteService.obtenerEstadisticasGenerales();
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * Endpoint para obtener resumen diario
     * GET /api/dashboard/resumen-diario?vehiculoId=VEH-001&fecha=2026-02-26
     */
    @GetMapping("/resumen-diario")
    public ResponseEntity<List<ResumenDiarioDto>> obtenerResumenDiario(
            @RequestParam(required = false) String vehiculoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        
        log.info("🌐 API: Solicitud de resumen diario - vehiculoId: {}, fecha: {}", vehiculoId, fecha);
        List<ResumenDiarioDto> resumen = reporteService.obtenerResumenDiario(vehiculoId, fecha);
        return ResponseEntity.ok(resumen);
    }

    /**
     * Endpoint para ubicaciones en tiempo real (últimas 24 horas)
     * GET /api/dashboard/ubicaciones-tiempo-real?vehiculoId=VEH-001
     */
    @GetMapping("/ubicaciones-tiempo-real")
    public ResponseEntity<UbicacionesVehiculoDto> obtenerUbicacionesTiempoReal(
            @RequestParam(required = false) String vehiculoId) {
        
        log.info("🌐 API: Solicitud de ubicaciones tiempo real - vehiculoId: {}", vehiculoId);
        UbicacionesVehiculoDto ubicaciones = reporteService.obtenerUbicacionesTiempoReal(vehiculoId);
        return ResponseEntity.ok(ubicaciones);
    }

    /**
     * Endpoint para reporte completo del día actual
     * GET /api/dashboard/reporte-hoy
     */
    @GetMapping("/reporte-hoy")
    public ResponseEntity<Map<String, Object>> obtenerReporteCompletoHoy() {
        log.info("🌐 API: Solicitud de reporte completo del día actual");
        Map<String, Object> reporte = reporteService.generarReporteCompletoHoy();
        return ResponseEntity.ok(reporte);
    }

    /**
     * Endpoint para generar resumen diario manualmente
     * POST /api/dashboard/generar-resumen-hoy
     */
    @PostMapping("/generar-resumen-hoy")
    public ResponseEntity<Map<String, Object>> generarResumenHoy() {
        log.info("🌐 API: Solicitud manual de generación de resumen para hoy");
        
        try {
            LocalDate hoy = LocalDate.now();
            resumenDiarioService.generarResumenDiario(hoy);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Resumen diario generado exitosamente");
            response.put("fecha", hoy.toString());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("❌ Error al generar resumen: {}", e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al generar resumen: " + e.getMessage());
            
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Endpoint de salud del servicio
     * GET /api/dashboard/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        log.debug("🌐 API: Health check");
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", System.currentTimeMillis(),
            "service", "monitorizacion-vehiculos",
            "version", "1.0.0"
        ));
    }

    /**
     * Endpoint para obtener información del sistema
     * GET /api/dashboard/info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        log.info("🌐 API: Solicitud de información del sistema");
        return ResponseEntity.ok(Map.of(
            "serviceName", "Microservicio de Monitorización de Vehículos",
            "description", "Consume mensajes de Kafka y persiste en Oracle Cloud",
            "version", "1.0.0",
            "puerto", 8083,
            "endpoints", List.of(
                "/api/dashboard/estadisticas",
                "/api/dashboard/resumen-diario",
                "/api/dashboard/ubicaciones-tiempo-real",
                "/api/dashboard/reporte-hoy",
                "/api/dashboard/generar-resumen-hoy",
                "/api/dashboard/health",
                "/api/dashboard/info"
            ),
            "topicsKafka", List.of("ubicaciones_vehiculos", "horarios"),
            "baseDatos", "Oracle Cloud"
        ));
    }
}
