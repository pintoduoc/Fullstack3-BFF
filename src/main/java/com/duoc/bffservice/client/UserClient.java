package com.duoc.bffservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "http://user-service:8082/api/usuario")
public interface UserClient {

    @GetMapping("/rut/{rut}")
    Object getUsuarioByRut(@PathVariable("rut") String rut);

    @GetMapping
    Object getAllUsuarios();

    @PostMapping
    Object createUsuario(@RequestBody Object usuario);

}