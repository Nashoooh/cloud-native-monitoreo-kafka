package com.example.monitoreo_kafka.controller;

import com.example.monitoreo_kafka.service.ResumenDiarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para operaciones de monitorización
 */
@Slf4j
@RestController
@RequestMapping("/api/monitorizacion")
public class MonitorizacionController {
    
    @Autowired
    private ResumenDiarioService resumenDiarioService;
    
    /**
     * Endpoint de health check
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Microservicio de Monitorización");
        response.put("port", 8083);
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Genera manualmente el resumen diario para la fecha actual
     * GET /api/monitorizacion/resumen/generar
     */
    @PostMapping("/resumen/generar")
    public ResponseEntity<Map<String, Object>> generarResumenHoy() {
        log.info("🔄 Solicitud manual de generación de resumen para hoy");
        
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
     * Genera manualmente el resumen diario para una fecha específica
     * POST /api/monitorizacion/resumen/generar/2026-02-24
     */
    @PostMapping("/resumen/generar/{fecha}")
    public ResponseEntity<Map<String, Object>> generarResumenFecha(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        log.info("🔄 Solicitud manual de generación de resumen para fecha: {}", fecha);
        
        try {
            resumenDiarioService.generarResumenDiario(fecha);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Resumen diario generado exitosamente");
            response.put("fecha", fecha.toString());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("❌ Error al generar resumen para {}: {}", fecha, e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al generar resumen: " + e.getMessage());
            
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Información del servicio
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Microservicio de Monitorización");
        response.put("description", "Consume ubicaciones y horarios de Kafka y los persiste en Oracle Cloud");
        response.put("topics", new String[]{"ubicaciones_vehiculos", "horarios"});
        response.put("features", new String[]{
            "Consumo de tópicos Kafka",
            "Persistencia en Oracle Cloud (simulada con logs)",
            "Generación automática de resumen diario a las 23:59",
            "API REST para control manual"
        });
        return ResponseEntity.ok(response);
    }
}
