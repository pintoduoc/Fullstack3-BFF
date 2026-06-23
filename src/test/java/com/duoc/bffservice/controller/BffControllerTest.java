package com.duoc.bffservice.controller;

import com.duoc.bffservice.client.AlertClient;
import com.duoc.bffservice.client.ReportClient;
import com.duoc.bffservice.client.UserClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BffController.class)
class BffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserClient userClient;

    @MockitoBean
    private ReportClient reportClient;

    @MockitoBean
    private AlertClient alertClient;

    @Test
    void testIniciarSesion() throws Exception {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("rut", "11111111-1");
        usuario.put("nombreCompleto", "Juan Perez");
        when(userClient.getUsuarioByRut("11111111-1")).thenReturn(usuario);

        mockMvc.perform(get("/api/bff/login/11111111-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("11111111-1"))
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Perez"));
    }

    @Test
    void testDashboardEstadisticas() throws Exception {
        Map<String, Object> reporte1 = new HashMap<>();
        reporte1.put("estado", "PENDIENTE");
        Map<String, Object> reporte2 = new HashMap<>();
        reporte2.put("estado", "EN_COMBATE");
        Map<String, Object> reporte3 = new HashMap<>();
        reporte3.put("estado", "CONTROLADO");
        Map<String, Object> reporte4 = new HashMap<>();
        reporte4.put("estado", "EXTINGUIDO");
        Map<String, Object> reporte5 = new HashMap<>();
        reporte5.put("estado", "PENDIENTE");
        when(reportClient.getAllReportes()).thenReturn(Arrays.asList(reporte1, reporte2, reporte3, reporte4, reporte5));

        mockMvc.perform(get("/api/bff/dashboard/estadisticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(5))
                .andExpect(jsonPath("$.pendientes").value(2))
                .andExpect(jsonPath("$.enCombate").value(1))
                .andExpect(jsonPath("$.controlados").value(1))
                .andExpect(jsonPath("$.extinguidos").value(1));
    }

    @Test
    void testListarReportes() throws Exception {
        when(reportClient.getAllReportes()).thenReturn(Arrays.asList(new HashMap<>()));

        mockMvc.perform(get("/api/bff/reportes"))
                .andExpect(status().isOk());
    }

    @Test
    void testCrearReporte() throws Exception {
        Map<String, Object> nuevoReporte = new HashMap<>();
        nuevoReporte.put("descripcion", "Incendio reportado");
        when(reportClient.crearReporte(any())).thenReturn(nuevoReporte);

        mockMvc.perform(post("/api/bff/reportes")
                        .contentType("application/json")
                        .content("{\"descripcion\":\"Incendio reportado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("Incendio reportado"));
    }

    @Test
    void testActualizarReporte() throws Exception {
        Map<String, Object> actualizado = new HashMap<>();
        actualizado.put("estado", "EN_COMBATE");
        when(reportClient.actualizarReporte(eq(1L), any())).thenReturn(actualizado);

        mockMvc.perform(put("/api/bff/reportes/1")
                        .contentType("application/json")
                        .content("{\"estado\":\"EN_COMBATE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_COMBATE"));
    }

    @Test
    void testObtenerAlertas() throws Exception {
        when(alertClient.getTodasLasAlertas()).thenReturn(Arrays.asList(new HashMap<>()));

        mockMvc.perform(get("/api/bff/alertas"))
                .andExpect(status().isOk());
    }

    @Test
    void testEmitirAlerta() throws Exception {
        Map<String, Object> alerta = new HashMap<>();
        alerta.put("mensajeAlerta", "Alerta de prueba");
        when(alertClient.emitirAlerta(any())).thenReturn(alerta);

        mockMvc.perform(post("/api/bff/alertas")
                        .contentType("application/json")
                        .content("{\"mensajeAlerta\":\"Alerta de prueba\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensajeAlerta").value("Alerta de prueba"));
    }
}
