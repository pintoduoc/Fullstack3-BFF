package com.duoc.bffservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "alert-service", url = "http://alert-service:8083/api/alerta-emergencia")
public interface AlertClient {

    @GetMapping
    List<Object> getTodasLasAlertas();

    @PostMapping
    Object emitirAlerta(@RequestBody Object alerta);

}