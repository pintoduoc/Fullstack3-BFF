# BFF - Backend For Frontend

Spring Boot 4.0.x que actúa como intermediario entre el frontend Vue.js y los microservicios internos del sistema de emergencias Valle del Sol.

## Endpoints

| Método | Ruta | Descripción | Proxy a |
|---|---|---|---|
| GET | `/api/bff/reportes` | Obtener todos los reportes | Report Service |
| POST | `/api/bff/reportes` | Crear reporte | Report Service |
| PUT | `/api/bff/reportes/{id}` | Actualizar reporte | Report Service |
| GET | `/api/bff/alertas` | Obtener todas las alertas | Alert Service |
| POST | `/api/bff/alertas` | Generar alerta desde reporte | Alert Service |
| GET | `/api/bff/dashboard/estadisticas` | Estadísticas para dashboard | Report Service |
| GET | `/api/bff/login/{rut}` | Inicio de sesión | User Service |

## Tecnologías

- Spring Boot 4.0.6 / Java 21
- Spring Cloud Circuit Breaker (Resilience4j + Feign)
- Eureka Client + RestTemplate load‑balanced
- JaCoCo (cobertura ≥ 60%)

## Tests

```bash
mvnw test
```
