package com.example.monitoreo_kafka.service;

import com.example.monitoreo_kafka.dto.EstadisticasGenerales;
import com.example.monitoreo_kafka.dto.ResumenDiarioDto;
import com.example.monitoreo_kafka.dto.UbicacionesVehiculoDto;
import com.example.monitoreo_kafka.entity.ResumenDiarioEntity;
import com.example.monitoreo_kafka.entity.UbicacionEntity;
import com.example.monitoreo_kafka.entity.HorarioEntity;
import com.example.monitoreo_kafka.repository.ResumenDiarioRepository;
import com.example.monitoreo_kafka.repository.UbicacionRepository;
import com.example.monitoreo_kafka.repository.HorarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReporteService {

    private final UbicacionRepository ubicacionRepository;
    private final HorarioRepository horarioRepository;
    private final ResumenDiarioRepository resumenDiarioRepository;
    
    @Value("${app.simulacion.enabled:false}")
    private boolean simulacionEnabled;

    /**
     * Obtiene estadísticas generales del sistema
     */
    public EstadisticasGenerales obtenerEstadisticasGenerales() {
        log.info("📊 Generando estadísticas generales del sistema");
        
        if (simulacionEnabled) {
            // Datos simulados para modo desarrollo
            return EstadisticasGenerales.builder()
                .totalUbicacionesRegistradas(1250L)
                .totalHorariosRegistrados(380L)
                .totalResumenesDiarios(15L)
                .vehiculosActivos(5)
                .ciudadesVisitadas(Arrays.asList("Lima", "Callao", "San Miguel", "Miraflores"))
                .ultimaActividad(LocalDateTime.now().minusMinutes(5))
                .vehiculosIds(Arrays.asList("VEH-001", "VEH-002", "VEH-003", "VEH-004", "VEH-005"))
                .fechaGeneracion(LocalDateTime.now())
                .build();
        }
        
        // Datos reales de BD
        long totalUbicaciones = ubicacionRepository.count();
        long totalHorarios = horarioRepository.count();
        long totalResumenes = resumenDiarioRepository.count();
        
        // Obtener vehículos únicos
        List<String> vehiculosActivos = ubicacionRepository.findDistinctVehiculoIds();
        
        // Obtener ciudades visitadas
        List<String> ciudadesVisitadas = ubicacionRepository.findDistinctCiudades();
        
        // Última actividad
        Optional<UbicacionEntity> ultimaUbicacion = ubicacionRepository.findTopByOrderByFechaRegistroDesc();
        LocalDateTime ultimaActividad = ultimaUbicacion
            .map(UbicacionEntity::getFechaRegistro)
            .orElse(null);

        return EstadisticasGenerales.builder()
            .totalUbicacionesRegistradas(totalUbicaciones)
            .totalHorariosRegistrados(totalHorarios)
            .totalResumenesDiarios(totalResumenes)
            .vehiculosActivos(vehiculosActivos.size())
            .ciudadesVisitadas(ciudadesVisitadas)
            .ultimaActividad(ultimaActividad)
            .vehiculosIds(vehiculosActivos)
            .fechaGeneracion(LocalDateTime.now())
            .build();
    }

    /**
     * Obtiene el resumen diario de un vehículo específico
     */
    public List<ResumenDiarioDto> obtenerResumenDiario(String vehiculoId, LocalDate fecha) {
        log.info("📈 Generando resumen diario para vehículo: {} en fecha: {}", vehiculoId, fecha);
        
        if (simulacionEnabled) {
            // Generar datos simulados
            List<ResumenDiarioDto> resumenes = new ArrayList<>();
            String[] vehiculos = vehiculoId != null ? new String[]{vehiculoId} 
                : new String[]{"VEH-001", "VEH-002", "VEH-003", "VEH-004", "VEH-005"};
            
            for (String vehId : vehiculos) {
                ResumenDiarioDto resumen = ResumenDiarioDto.builder()
                    .id((long) (Math.random() * 100))
                    .vehiculoId(vehId)
                    .placaVehiculo("ABC-" + vehId.substring(4))
                    .fechaResumen(fecha != null ? fecha : LocalDate.now())
                    .totalUbicaciones((long) (150 + Math.random() * 100))
                    .ciudadesVisitadas(Arrays.asList("Lima", "Callao", "San Miguel"))
                    .totalParadas((long) (15 + Math.random() * 10))
                    .horaInicio(LocalDateTime.now().minusHours(16))
                    .horaFin(LocalDateTime.now().minusHours(2))
                    .velocidadPromedio(35.5 + Math.random() * 20)
                    .pasajerosPromedio(20.0 + Math.random() * 15)
                    .ruta("Ruta " + vehId.charAt(4) + " - Norte")
                    .fechaCreacion(LocalDateTime.now())
                    .build();
                resumenes.add(resumen);
            }
            return resumenes;
        }
        
        // Datos reales de BD
        List<ResumenDiarioEntity> resumenes;
        
        if (vehiculoId != null && fecha != null) {
            resumenes = resumenDiarioRepository.findByVehiculoIdAndFechaResumen(vehiculoId, fecha);
        } else if (vehiculoId != null) {
            resumenes = resumenDiarioRepository.findByVehiculoIdOrderByFechaResumenDesc(vehiculoId);
        } else if (fecha != null) {
            resumenes = resumenDiarioRepository.findByFechaResumenOrderByVehiculoId(fecha);
        } else {
            LocalDate hace7Dias = LocalDate.now().minusDays(7);
            resumenes = resumenDiarioRepository.findByFechaResumenGreaterThanEqualOrderByFechaResumenDesc(hace7Dias);
        }

        return resumenes.stream()
            .map(this::convertirADto)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene las ubicaciones de un vehículo en tiempo real (últimas 24 horas)
     */
    public UbicacionesVehiculoDto obtenerUbicacionesTiempoReal(String vehiculoId) {
        log.info("🗺️  Obteniendo ubicaciones en tiempo real para vehículo: {}", vehiculoId);
        
        if (simulacionEnabled) {
            // Generar datos simulados
            Set<String> vehiculosConActividad = vehiculoId != null 
                ? Set.of(vehiculoId) 
                : Set.of("VEH-001", "VEH-002", "VEH-003", "VEH-004", "VEH-005");
                
            return UbicacionesVehiculoDto.builder()
                .fechaConsulta(LocalDateTime.now())
                .totalUbicaciones(vehiculosConActividad.size() * 50)
                .vehiculosConActividad(vehiculosConActividad)
                .ubicacionesPorVehiculo(new HashMap<>()) // Simulado vacío por simplicidad
                .build();
        }
        
        LocalDateTime hace24Horas = LocalDateTime.now().minusHours(24);
        List<UbicacionEntity> ubicaciones;
        
        if (vehiculoId != null) {
            ubicaciones = ubicacionRepository.findByVehiculoIdAndFechaRegistroGreaterThanEqualOrderByFechaRegistroDesc(vehiculoId, hace24Horas);
        } else {
            ubicaciones = ubicacionRepository.findByFechaRegistroGreaterThanEqualOrderByFechaRegistroDesc(hace24Horas);
        }

        // Agrupar por vehículo
        Map<String, List<UbicacionEntity>> ubicacionesPorVehiculo = ubicaciones.stream()
            .collect(Collectors.groupingBy(UbicacionEntity::getVehiculoId));

        return UbicacionesVehiculoDto.builder()
            .fechaConsulta(LocalDateTime.now())
            .totalUbicaciones(ubicaciones.size())
            .vehiculosConActividad(ubicacionesPorVehiculo.keySet())
            .ubicacionesPorVehiculo(ubicacionesPorVehiculo)
            .build();
    }

    /**
     * Genera un reporte completo del día actual en formato JSON
     */
    public Map<String, Object> generarReporteCompletoHoy() {
        log.info("📋 Generando reporte completo del día actual");
        
        LocalDate hoy = LocalDate.now();
        
        if (simulacionEnabled) {
            // Generar reporte simulado
            Map<String, Object> reporte = new HashMap<>();
            reporte.put("fecha", hoy);
            reporte.put("fechaGeneracion", LocalDateTime.now());
            reporte.put("modoSimulacion", true);
            reporte.put("estadisticas", Map.of(
                "ubicacionesRegistradas", 250L,
                "horariosRegistrados", 75L,
                "vehiculosActivos", 5,
                "vehiculosIds", Arrays.asList("VEH-001", "VEH-002", "VEH-003", "VEH-004", "VEH-005")
            ));
            reporte.put("resumenesPorVehiculo", obtenerResumenDiario(null, hoy));
            reporte.put("estadisticasGenerales", obtenerEstadisticasGenerales());
            return reporte;
        }
        
        LocalDateTime inicioDelDia = hoy.atStartOfDay();
        LocalDateTime finDelDia = hoy.atTime(23, 59, 59);

        // Estadísticas del día
        long ubicacionesHoy = ubicacionRepository.countByFechaRegistroBetween(inicioDelDia, finDelDia);
        long horariosHoy = horarioRepository.countByFechaRegistroBetween(inicioDelDia, finDelDia);

        // Vehículos activos hoy
        List<String> vehiculosActivosHoy = ubicacionRepository.findDistinctVehiculoIdsByFechaRegistroBetween(inicioDelDia, finDelDia);

        // Resumen por vehículo
        List<ResumenDiarioDto> resumenes = obtenerResumenDiario(null, hoy);

        Map<String, Object> reporte = new HashMap<>();
        reporte.put("fecha", hoy);
        reporte.put("fechaGeneracion", LocalDateTime.now());
        reporte.put("modoSimulacion", false);
        reporte.put("estadisticas", Map.of(
            "ubicacionesRegistradas", ubicacionesHoy,
            "horariosRegistrados", horariosHoy,
            "vehiculosActivos", vehiculosActivosHoy.size(),
            "vehiculosIds", vehiculosActivosHoy
        ));
        reporte.put("resumenesPorVehiculo", resumenes);
        reporte.put("estadisticasGenerales", obtenerEstadisticasGenerales());

        return reporte;
    }

    private ResumenDiarioDto convertirADto(ResumenDiarioEntity entity) {
        return ResumenDiarioDto.builder()
            .id(entity.getId())
            .vehiculoId(entity.getVehiculoId())
            .placaVehiculo(entity.getPlacaVehiculo())
            .fechaResumen(entity.getFechaResumen())
            .totalUbicaciones(entity.getTotalUbicaciones())
            .ciudadesVisitadas(Arrays.asList(entity.getCiudadesVisitadas().split(",")))
            .totalParadas(entity.getTotalParadas())
            .horaInicio(entity.getHoraInicio())
            .horaFin(entity.getHoraFin())
            .velocidadPromedio(entity.getVelocidadPromedio())
            .pasajerosPromedio(entity.getPasajerosPromedio())
            .ruta(entity.getRuta())
            .fechaCreacion(entity.getFechaCreacion())
            .build();
    }
}
