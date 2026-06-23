package com.duoc.bffservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "report-service", path = "/api/reporte-incendio")
public interface ReportClient {

    @GetMapping
    List<Object> getAllReportes();

    @PostMapping
    Object crearReporte(@RequestBody Object reporte);

    @PutMapping("/{id}")
    Object actualizarReporte(@PathVariable("id") Long id, @RequestBody Object reporte);

}