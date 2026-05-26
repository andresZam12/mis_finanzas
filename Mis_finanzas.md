# Sistema de Finanzas Personales

Proyecto final de Programación Orientada a Objetos — UCC Pasto, 2026.

## Stack

- **Backend**: Spring Boot 3.4.1 · Java 24 · Spring Security + JWT · JPA/Hibernate
- **Frontend**: Angular 21.2 (standalone components) — carpeta `finanzasFront/` en el nivel superior
- **Base de datos**: PostgreSQL 17 · base de datos `finanzas_db`

## Ejecutar el proyecto

### Requisitos previos
- Java 24+
- PostgreSQL 17 corriendo en `localhost:5432`
- Crear la base de datos: `CREATE DATABASE finanzas_db;`

### Backend
1. Copia `src/main/resources/application.properties.example` → `application.properties`
2. Rellena tu contraseña de PostgreSQL y la clave JWT
3. Inicia PostgreSQL: `net start postgresql-x64-17`
4. Ejecuta: `mvn spring-boot:run` o desde VS Code con el botón Run
5. API disponible en `http://localhost:8081`

### Frontend (carpeta `finanzasFront/`)
1. `npm install`
2. `ng serve`
3. App disponible en `http://localhost:4200`

## Arquitectura

```
Controller → Service → Repository → Model
```

Cada capa solo conoce a la siguiente. Ningún controlador accede directamente a los repositorios.

## Conceptos POO aplicados

| Concepto | Dónde |
|---|---|
| Abstracción | `Movimiento` (abstract) + `Recurrente` (interface) |
| Herencia | `Ingreso` y `Gasto` extienden `Movimiento` |
| Polimorfismo | `registrar()` implementado distinto en Ingreso y Gasto |
| Composición recursiva | `Menu` con relación padre → hijos (árbol n-ario) |
| Encapsulamiento | Todos los campos `private` con getters/setters |
| Inyección de dependencias | Constructor injection en todos los servicios y controladores |

## Endpoints principales

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/auth/login` | Iniciar sesión → devuelve JWT |
| POST | `/auth/registro` | Registrar nuevo usuario |
| GET | `/movimientos/ingresos/{uid}` | Listar ingresos del usuario |
| POST | `/movimientos/ingresos` | Registrar ingreso |
| GET | `/movimientos/gastos/{uid}` | Listar gastos |
| POST | `/movimientos/gastos` | Registrar gasto |
| GET | `/deudas/{uid}` | Listar deudas |
| POST | `/deudas` | Registrar deuda |
| POST | `/deudas/{id}/abonar?monto=X` | Abonar a una deuda |
| PUT | `/deudas/{id}/pagar` | Marcar deuda como pagada |
| GET | `/metas/{uid}/todas` | Listar metas de ahorro |
| GET | `/ahorros/{uid}` | Listar ahorros |
| GET | `/reportes` | Reporte financiero mensual |
| GET | `/menus` | Árbol de menús desde BD |
| GET | `/categorias` | Listar categorías |

## Notas importantes

- `MenuSeeder` (`config/MenuSeeder.java`) puebla la tabla `menu` automáticamente al primer arranque si está vacía.
- `ReporteFinanciero` **no es `@Entity`** — se genera en memoria sin persistirse.
- `Movimiento` usa `@MappedSuperclass` — no crea tabla propia; sus campos se mapean en `ingreso` y `gasto`.
- Al registrar un ingreso con `porcentajeAhorro > 0`, el sistema crea un `Ahorro` automáticamente.
- Al abonar una deuda, el sistema crea un `Gasto` automáticamente para mantener el balance correcto.
- Todas las contraseñas se encriptan con **BCrypt** antes de guardarse.
- El JWT se valida en cada petición mediante `JwtFilter` (antes de llegar al controlador).

## Diagramas UML

Los diagramas en PlantUML están en la raíz del proyecto:

- `diagrama_conceptual.puml` — clases, herencia, polimorfismo, composición
- `diagrama_capas.puml` — arquitectura por capas
- `diagrama_contexto.puml` — contexto del sistema y actores externos
- `diagrama_despliegue.puml` — infraestructura física (nodos y artefactos)
- `diagrama_funcional.puml` — casos de uso

Para visualizarlos: instala la extensión **PlantUML** en VS Code y presiona `Alt+D`.
