package com.duoc.bffservice.controller;

import com.duoc.bffservice.client.AlertClient;
import com.duoc.bffservice.client.ReportClient;
import com.duoc.bffservice.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bff")
public class BffController {

    @Autowired
    private UserClient userClient;

    @Autowired
    private ReportClient reportClient;

    @Autowired
    private AlertClient alertClient;

    // --- MÓDULO DE AUTENTICACIÓN ---
    @GetMapping("/login/{rut}")
    public ResponseEntity<?> iniciarSesion(@PathVariable String rut) {
        // El BFF valida la existencia del usuario a través del user-service
        return ResponseEntity.ok(userClient.getUsuarioByRut(rut));
    }

    // --- MÓDULO DE DASHBOARD (ORQUESTACIÓN Y CÁLCULO) ---
    @GetMapping("/dashboard/estadisticas")
    public ResponseEntity<?> getEstadisticasDashboard() {
        // El BFF pide la lista completa al report-service
        List<Object> reportes = reportClient.getAllReportes();

        // El BFF realiza el procesamiento de datos para que el frontend sea más rápido
        long pendientes = countByEstado(reportes, "PENDIENTE");
        long combate = countByEstado(reportes, "EN_COMBATE");
        long controlado = countByEstado(reportes, "CONTROLADO");
        long extinguido = countByEstado(reportes, "EXTINGUIDO");

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", reportes.size());
        stats.put("pendientes", pendientes);
        stats.put("enCombate", combate);
        stats.put("controlados", controlado);
        stats.put("extinguidos", extinguido);

        return ResponseEntity.ok(stats);
    }

    // --- MÓDULO DE REPORTES (PARA CIUDADANOS Y ADMINS) ---
    @GetMapping("/reportes")
    public ResponseEntity<?> listarTodosLosReportes() {
        return ResponseEntity.ok(reportClient.getAllReportes());
    }

    @PostMapping("/reportes")
    public ResponseEntity<?> crearNuevoReporte(@RequestBody Object nuevoReporte) {
        return ResponseEntity.ok(reportClient.crearReporte(nuevoReporte));
    }

    @PutMapping("/reportes/{id}")
    public ResponseEntity<?> actualizarReporte(@PathVariable Long id, @RequestBody Object reporteEditado) {
        return ResponseEntity.ok(reportClient.actualizarReporte(id, reporteEditado));
    }

    // --- MÓDULO DE ALERTAS (PÚBLICO Y ADMIN) ---
    @GetMapping("/alertas")
    public ResponseEntity<?> obtenerMuroAlertas() {
        return ResponseEntity.ok(alertClient.getTodasLasAlertas());
    }

    @PostMapping("/alertas")
    public ResponseEntity<?> emitirNuevaAlerta(@RequestBody Object alerta) {
        return ResponseEntity.ok(alertClient.emitirAlerta(alerta));
    }

    // Método auxiliar para contar estados (Lógica de negocio en el backend)
    private long countByEstado(List<Object> reportes, String estado) {
        return reportes.stream()
                .filter(r -> {
                    // Mapeo simple para extraer el campo 'estado' de los objetos genéricos
                    Map<?, ?> map = (Map<?, ?>) r;
                    return estado.equals(map.get("estado"));
                })
                .count();
    }
}