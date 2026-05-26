# 💰 Sistema de Finanzas Personales

Proyecto final de **Programación Orientada a Objetos** — Universidad Cooperativa de Colombia, Sede Pasto · 2026.

Aplicación web full-stack para la gestión de finanzas personales: ingresos, gastos, deudas, metas de ahorro y reportes financieros mensuales.

---

## 🛠️ Tecnologías

| Capa | Tecnología |
|---|---|
| Backend | Java 24 · Spring Boot 3.4.1 · Spring Security · JWT |
| Frontend | Angular 21.2 · TypeScript · standalone components |
| Base de datos | PostgreSQL 17 · JPA / Hibernate |
| Build | Maven · Angular CLI |

---

## ✅ Funcionalidades

- Registro e inicio de sesión con autenticación JWT
- Registro de ingresos y gastos con categoría y tipo de pago
- Ahorro automático al registrar ingresos (porcentaje configurable)
- Gestión de deudas: registrar, abonar parcialmente o marcar como pagada
- Metas de ahorro mensuales con verificación de cumplimiento
- Dashboard con resumen financiero y desglose por tipo de pago
- Reporte financiero mensual
- Menú de navegación dinámico cargado desde la base de datos

---

## 🧱 Conceptos POO aplicados

| Concepto | Implementación |
|---|---|
| **Abstracción** | Clase abstracta `Movimiento` e interfaz `Recurrente` |
| **Herencia** | `Ingreso` y `Gasto` extienden `Movimiento` |
| **Polimorfismo** | `registrar()` con comportamiento distinto en cada subclase |
| **Composición recursiva** | `Menu` con relación padre → hijos (árbol n-ario) |
| **Encapsulamiento** | Campos `private` con acceso por getters/setters |
| **Inyección de dependencias** | Constructor injection en servicios y controladores |

---

## 🗂️ Arquitectura

```
Controller  →  Service  →  Repository  →  Model
```

Cada capa solo conoce a la inmediatamente siguiente. Los controladores nunca acceden directamente a los repositorios ni al modelo.

---

## 🚀 Cómo ejecutar el proyecto

### Requisitos
- Java 24+
- PostgreSQL 17 corriendo en `localhost:5432`
- Node.js 18+ y Angular CLI

### 1. Base de datos
```sql
CREATE DATABASE finanzas_db;
```

### 2. Backend
```bash
# Copia la plantilla de configuración
cp src/main/resources/application.properties.example src/main/resources/application.properties

# Edita application.properties con tu contraseña de PostgreSQL y clave JWT

# Inicia el servidor
mvn spring-boot:run
```
API disponible en `http://localhost:8081`

### 3. Frontend
```bash
cd ../finanzasFront
npm install
ng serve
```
App disponible en `http://localhost:4200`

---

## 📁 Estructura del backend

```
src/main/java/com/ucc/finanzas/
├── config/         # Configuración CORS y MenuSeeder
├── controller/     # Controladores REST
├── dto/            # Objetos de transferencia de datos
├── model/          # Entidades JPA y clases de dominio
├── repository/     # Interfaces Spring Data JPA
├── security/       # JWT, filtros y configuración de seguridad
└── service/        # Lógica de negocio
```

---

## 📊 Diagramas UML

Los diagramas en formato PlantUML están en la raíz del proyecto:

| Archivo | Contenido |
|---|---|
| `diagrama_conceptual.puml` | Clases, herencia, polimorfismo, composición |
| `diagrama_capas.puml` | Arquitectura por capas |
| `diagrama_contexto.puml` | Sistema y actores externos |
| `diagrama_despliegue.puml` | Infraestructura física |
| `diagrama_funcional.puml` | Casos de uso |

Para visualizarlos: instala la extensión **PlantUML** en VS Code y presiona `Alt + D`.

---

## 👤 Autor

**Andrés Zambrano**  
Ingeniería de Sistemas — Universidad Cooperativa de Colombia, Pasto  
Materia: Programación Orientada a Objetos · 2026
