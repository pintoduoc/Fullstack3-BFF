package com.duoc.bffservice.controller;

import com.duoc.bffservice.client.AlertClient;
import com.duoc.bffservice.client.ReportClient;
import com.duoc.bffservice.client.UserClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BffControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserClient userClient;

    @Mock
    private ReportClient reportClient;

    @Mock
    private AlertClient alertClient;

    @BeforeEach
    void setUp() {
        BffController controller = new BffController();
        ReflectionTestUtils.setField(controller, "userClient", userClient);
        ReflectionTestUtils.setField(controller, "reportClient", reportClient);
        ReflectionTestUtils.setField(controller, "alertClient", alertClient);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testIniciarSesion() throws Exception {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("rut", "11111111-1");
        usuario.put("nombreCompleto", "Juan Perez");
        when(userClient.getUsuarioByRut("11111111-1")).thenReturn(usuario);

        mockMvc.perform(get("/api/bff/login/11111111-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("11111111-1"));

        verify(userClient, times(1)).getUsuarioByRut("11111111-1");
    }

    @Test
    void testGetEstadisticasDashboard() throws Exception {
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
        List<Object> reportes = Arrays.asList(reporte1, reporte2, reporte3, reporte4, reporte5);

        when(reportClient.getAllReportes()).thenReturn(reportes);

        mockMvc.perform(get("/api/bff/dashboard/estadisticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(5))
                .andExpect(jsonPath("$.pendientes").value(2))
                .andExpect(jsonPath("$.enCombate").value(1))
                .andExpect(jsonPath("$.controlados").value(1))
                .andExpect(jsonPath("$.extinguidos").value(1));

        verify(reportClient, times(1)).getAllReportes();
    }

    @Test
    void testGetEstadisticasDashboard_Vacio() throws Exception {
        when(reportClient.getAllReportes()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/bff/dashboard/estadisticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.pendientes").value(0))
                .andExpect(jsonPath("$.enCombate").value(0))
                .andExpect(jsonPath("$.controlados").value(0))
                .andExpect(jsonPath("$.extinguidos").value(0));
    }

    @Test
    void testListarTodosLosReportes() throws Exception {
        when(reportClient.getAllReportes()).thenReturn(Arrays.asList(new HashMap<>()));

        mockMvc.perform(get("/api/bff/reportes"))
                .andExpect(status().isOk());

        verify(reportClient, times(1)).getAllReportes();
    }

    @Test
    void testCrearNuevoReporte() throws Exception {
        Map<String, Object> nuevoReporte = new HashMap<>();
        nuevoReporte.put("descripcion", "Incendio reportado");
        when(reportClient.crearReporte(any())).thenReturn(nuevoReporte);

        mockMvc.perform(post("/api/bff/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descripcion\":\"Incendio reportado\"}"))
                .andExpect(status().isOk());

        verify(reportClient, times(1)).crearReporte(any());
    }

    @Test
    void testActualizarReporte() throws Exception {
        Map<String, Object> reporteActualizado = new HashMap<>();
        reporteActualizado.put("descripcion", "Actualizado");
        when(reportClient.actualizarReporte(eq(1L), any())).thenReturn(reporteActualizado);

        mockMvc.perform(put("/api/bff/reportes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descripcion\":\"Actualizado\"}"))
                .andExpect(status().isOk());

        verify(reportClient, times(1)).actualizarReporte(eq(1L), any());
    }

    @Test
    void testObtenerMuroAlertas() throws Exception {
        when(alertClient.getTodasLasAlertas()).thenReturn(Arrays.asList(new HashMap<>()));

        mockMvc.perform(get("/api/bff/alertas"))
                .andExpect(status().isOk());

        verify(alertClient, times(1)).getTodasLasAlertas();
    }

    @Test
    void testEmitirNuevaAlerta() throws Exception {
        Map<String, Object> nuevaAlerta = new HashMap<>();
        nuevaAlerta.put("mensaje", "Alerta emitida");
        when(alertClient.emitirAlerta(any())).thenReturn(nuevaAlerta);

        mockMvc.perform(post("/api/bff/alertas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mensaje\":\"Alerta emitida\"}"))
                .andExpect(status().isOk());

        verify(alertClient, times(1)).emitirAlerta(any());
    }
}
