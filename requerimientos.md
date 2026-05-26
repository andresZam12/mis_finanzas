# Requerimientos del Sistema de Finanzas Personales
**Materia:** Programación Orientada a Objetos  
**Universidad:** Universidad Cooperativa de Colombia — Sede Pasto  
**Proyecto:** Sistema de Finanzas Personales  
**Stack:** Spring Boot 3.4.1 + Angular 21 + PostgreSQL 17  

---

# REQUERIMIENTOS FUNCIONALES

---

## RF-01 — Registro de Usuario

**Narrativa:**  
Como nuevo usuario, quiero registrarme en el sistema con un nombre de usuario, contraseña y nombre completo, para poder acceder a mi cuenta personal de finanzas de forma segura.

**Explicación:**  
El sistema permite que cualquier persona cree una cuenta nueva ingresando un `username` único (sin espacios), una contraseña y su nombre completo. Al registrarse, el sistema encripta la contraseña con BCrypt antes de guardarla en la base de datos y asigna el estado `"ACTIVO"` automáticamente. Si el `username` ya existe, el sistema rechaza el registro con un mensaje de error. El usuario no puede manipular el estado ni la contraseña encriptada directamente.

**Mockup:**
```
┌──────────────────────────────────────────────────────┐
│                  💰 Finanzas                         │
│                                                      │
│         ┌────────────────────────────────┐           │
│         │         Crear cuenta           │           │
│         ├────────────────────────────────┤           │
│         │ Nombre completo                │           │
│         │ ┌──────────────────────────┐   │           │
│         │ │ Andrés Zapata            │   │           │
│         │ └──────────────────────────┘   │           │
│         │ Nombre de usuario              │           │
│         │ ┌──────────────────────────┐   │           │
│         │ │ andres01                 │   │           │
│         │ └──────────────────────────┘   │           │
│         │ Contraseña                     │           │
│         │ ┌──────────────────────────┐   │           │
│         │ │ ••••••••••               │   │           │
│         │ └──────────────────────────┘   │           │
│         │                                │           │
│         │  [ Registrarse ]               │           │
│         │  ¿Ya tienes cuenta? Inicia sesión          │
│         └────────────────────────────────┘           │
└──────────────────────────────────────────────────────┘
```

---

## RF-02 — Inicio de Sesión

**Narrativa:**  
Como usuario registrado, quiero iniciar sesión con mi nombre de usuario y contraseña para acceder de forma segura a mis finanzas personales y que el sistema recuerde mi sesión mediante un token.

**Explicación:**  
El sistema valida las credenciales ingresadas contra la base de datos. Si son correctas, genera un token JWT firmado con una clave secreta y lo devuelve al frontend. Angular almacena el token y lo envía automáticamente en cada petición posterior mediante un `HttpInterceptor`. Si las credenciales son incorrectas, el sistema responde con error `401`. Las rutas del sistema están protegidas por `AuthGuard`, que redirige al login si no hay token válido.

**Mockup:**
```
┌──────────────────────────────────────────────────────┐
│                  💰 Finanzas                         │
│                                                      │
│         ┌────────────────────────────────┐           │
│         │         Iniciar sesión         │           │
│         ├────────────────────────────────┤           │
│         │ Usuario                        │           │
│         │ ┌──────────────────────────┐   │           │
│         │ │ andres01                 │   │           │
│         │ └──────────────────────────┘   │           │
│         │ Contraseña                     │           │
│         │ ┌──────────────────────────┐   │           │
│         │ │ ••••••••••               │   │           │
│         │ └──────────────────────────┘   │           │
│         │                                │           │
│         │  [ Ingresar ]                  │           │
│         │  ¿No tienes cuenta? Regístrate │           │
│         └────────────────────────────────┘           │
└──────────────────────────────────────────────────────┘
```

---

## RF-03 — Registrar Ingreso

**Narrativa:**  
Como usuario autenticado, quiero registrar un ingreso especificando el monto, fuente, descripción, categoría, tipo de pago y porcentaje de ahorro, para llevar el control de mis entradas de dinero y generar ahorro automáticamente si lo deseo.

**Explicación:**  
El usuario completa el formulario de nuevo ingreso. El sistema valida que el monto sea mayor a cero y que la categoría sea de tipo `INGRESO`. Si el campo `porcentajeAhorro` es mayor a cero, el sistema calcula automáticamente el monto a ahorrar (`monto × porcentaje / 100`) y crea un registro en la tabla `ahorro` sin que el usuario tenga que hacerlo manualmente. El `tipoPago` se normaliza a mayúsculas antes de guardarse.

**Mockup:**
```
┌─────────────┬────────────────────────────────────────┐
│ 💰 Finanzas │  📈 Nuevo Ingreso                      │
│─────────────│────────────────────────────────────────│
│ 📊 Dashboard│  ┌──────────────────────────────────┐  │
│ ▾ 💸 Movm. │  │ Monto *                          │  │
│  📈 Ingresos│  │ ┌────────────────────────────┐   │  │
│  📉 Gastos  │  │ │ $ 2.500.000                │   │  │
│  ➕ N.Ing  │  │ └────────────────────────────┘   │  │
│  ➖ N.Gas  │  │ Fuente *                         │  │
│ ▾ 🏷️ Categ.│  │ ┌────────────────────────────┐   │  │
│ ▾ 🤝 Deudas│  │ │ Salario                    │   │  │
│ ▾ 🎯 Metas │  │ └────────────────────────────┘   │  │
│ ▾ 💰 Ahorros│  │ Categoría *                      │  │
│─────────────│  │ ┌────────────────────────────┐   │  │
│ 👤 andres   │  │ │ Salarios ▾                 │   │  │
│ [ Salir ]   │  │ └────────────────────────────┘   │  │
│             │  │ Tipo de pago                     │  │
│             │  │ ┌────────────────────────────┐   │  │
│             │  │ │ Transferencia ▾            │   │  │
│             │  │ └────────────────────────────┘   │  │
│             │  │ % Ahorro   Descripción           │  │
│             │  │ ┌──────┐   ┌─────────────────┐  │  │
│             │  │ │ 10   │   │ Quincena mayo   │  │  │
│             │  │ └──────┘   └─────────────────┘  │  │
│             │  │ ☐ Es recurrente                  │  │
│             │  │                                  │  │
│             │  │  [ Guardar ingreso ]             │  │
│             │  └──────────────────────────────────┘  │
└─────────────┴────────────────────────────────────────┘
```

---

## RF-04 — Registrar Gasto

**Narrativa:**  
Como usuario autenticado, quiero registrar un gasto con su monto, descripción, categoría y tipo de pago, para llevar el control de mis salidas de dinero y saber en qué estoy gastando.

**Explicación:**  
El usuario completa el formulario de nuevo gasto. El sistema valida que el monto sea mayor a cero y que la categoría seleccionada sea de tipo `GASTO`. El campo `tipoPago` permite identificar si el pago fue en efectivo, tarjeta, transferencia u otro medio. El tipo se normaliza a mayúsculas (ej: `"efectivo"` → `"EFECTIVO"`) antes de persistirse. A diferencia del ingreso, el gasto no genera ahorro automático.

**Mockup:**
```
┌─────────────┬────────────────────────────────────────┐
│ 💰 Finanzas │  📉 Nuevo Gasto                        │
│─────────────│────────────────────────────────────────│
│ 📊 Dashboard│  ┌──────────────────────────────────┐  │
│ ▾ 💸 Movm. │  │ Monto *                          │  │
│  📈 Ingresos│  │ ┌────────────────────────────┐   │  │
│  📉 Gastos  │  │ │ $ 85.000                   │   │  │
│  ➕ N.Ing  │  │ └────────────────────────────┘   │  │
│  ➖ N.Gas  │  │ Categoría *                      │  │
│ ▾ 🏷️ Categ.│  │ ┌────────────────────────────┐   │  │
│ ▾ 🤝 Deudas│  │ │ Alimentación ▾             │   │  │
│ ▾ 🎯 Metas │  │ └────────────────────────────┘   │  │
│ ▾ 💰 Ahorros│  │ Tipo de pago                     │  │
│─────────────│  │ ┌────────────────────────────┐   │  │
│ 👤 andres   │  │ │ Efectivo ▾                 │   │  │
│ [ Salir ]   │  │ └────────────────────────────┘   │  │
│             │  │ Descripción                      │  │
│             │  │ ┌────────────────────────────┐   │  │
│             │  │ │ Mercado semanal            │   │  │
│             │  │ └────────────────────────────┘   │  │
│             │  │ ☐ Es recurrente                  │  │
│             │  │                                  │  │
│             │  │  [ Guardar gasto ]               │  │
│             │  └──────────────────────────────────┘  │
└─────────────┴────────────────────────────────────────┘
```

---

## RF-05 — Listar Ingresos y Gastos

**Narrativa:**  
Como usuario autenticado, quiero ver la lista completa de mis ingresos y gastos registrados, para revisar mi historial financiero y tener visibilidad sobre mis movimientos.

**Explicación:**  
El sistema consulta todos los registros de ingreso y gasto asociados al usuario autenticado y los presenta en tablas separadas. Cada fila muestra el monto, descripción, categoría, tipo de pago, si es recurrente y la fecha. Si no hay registros, se muestra un mensaje indicándolo. Las tablas incluyen botones de edición y eliminación por fila.

**Mockup:**
```
┌─────────────┬────────────────────────────────────────────────────┐
│ 💰 Finanzas │  📈 Mis Ingresos               [➕ Nuevo ingreso] │
│─────────────│────────────────────────────────────────────────────│
│ 📊 Dashboard│  ┌──────┬──────────┬──────────┬─────────┬───────┐  │
│ ▾ 💸 Movm. │  │Monto │Descripción│Categoría │TipoPago │Acción│  │
│  📈 Ingresos│  ├──────┼──────────┼──────────┼─────────┼───────┤  │
│  📉 Gastos  │  │$2.5M │Quincena  │Salarios  │TRANSFER.│✏️ 🗑️│  │
│  ➕ N.Ing  │  │$500k │Freelance │Honorarios│EFECTIVO │✏️ 🗑️│  │
│  ➖ N.Gas  │  │$200k │Arriendo  │Arriendos │TRANSFER.│✏️ 🗑️│  │
│ ▾ 🏷️ Categ.│  └──────┴──────────┴──────────┴─────────┴───────┘  │
│ ▾ 🤝 Deudas│                                                      │
│ ▾ 🎯 Metas │                                                      │
│ ▾ 💰 Ahorros│                                                      │
│─────────────│                                                      │
│ 👤 andres   │                                                      │
│ [ Salir ]   │                                                      │
└─────────────┴────────────────────────────────────────────────────┘
```

---

## RF-06 — Editar y Eliminar Movimiento

**Narrativa:**  
Como usuario autenticado, quiero poder editar un ingreso o gasto previamente registrado o eliminarlo, para corregir errores o remover registros que ya no son relevantes.

**Explicación:**  
Al hacer clic en el ícono de edición (✏️), el sistema carga los datos actuales del movimiento en un formulario editable. El usuario puede modificar monto, descripción, categoría, tipo de pago y recurrencia. Al guardar, el sistema actualiza el registro en la base de datos. Al eliminar (🗑️), el sistema solicita confirmación antes de borrar el registro permanentemente.

**Mockup:**
```
┌─────────────┬────────────────────────────────────────┐
│ 💰 Finanzas │  ✏️ Editar Ingreso                     │
│─────────────│────────────────────────────────────────│
│ 📊 Dashboard│  ┌──────────────────────────────────┐  │
│ ▾ 💸 Movm. │  │ Monto *                          │  │
│  📈 Ingresos│  │ ┌────────────────────────────┐   │  │
│  ...        │  │ │ $ 2.500.000                │   │  │
│             │  │ └────────────────────────────┘   │  │
│             │  │ Categoría *                      │  │
│             │  │ ┌────────────────────────────┐   │  │
│             │  │ │ Salarios ▾                 │   │  │
│             │  │ └────────────────────────────┘   │  │
│             │  │ Tipo de pago                     │  │
│             │  │ ┌────────────────────────────┐   │  │
│             │  │ │ Transferencia ▾            │   │  │
│             │  │ └────────────────────────────┘   │  │
│             │  │ Descripción                      │  │
│             │  │ ┌────────────────────────────┐   │  │
│             │  │ │ Quincena mayo              │   │  │
│             │  │ └────────────────────────────┘   │  │
│             │  │  [ Actualizar ]  [ Cancelar ]    │  │
│             │  └──────────────────────────────────┘  │
│             │                                        │
│             │  ⚠️ ¿Eliminar este movimiento?         │
│             │     [ Eliminar ]  [ Cancelar ]         │
└─────────────┴────────────────────────────────────────┘
```

---

## RF-07 — Gestionar Categorías

**Narrativa:**  
Como usuario autenticado, quiero crear, editar y eliminar categorías de tipo INGRESO o GASTO, para clasificar correctamente mis movimientos financieros según mis necesidades.

**Explicación:**  
El sistema permite al usuario definir sus propias categorías indicando nombre y tipo (`INGRESO` o `GASTO`). El sistema valida que el nombre no esté vacío y que el tipo sea uno de los dos valores permitidos. Las categorías son compartidas por todos los movimientos del sistema. Al eliminar una categoría, el sistema verifica que no tenga movimientos asociados para evitar inconsistencias.

**Mockup:**
```
┌─────────────┬───────────────────────────────────────────────┐
│ 💰 Finanzas │  🏷️ Categorías             [➕ Nueva]        │
│─────────────│───────────────────────────────────────────────│
│ ...         │  ┌────────────────┬──────────┬───────────┐    │
│ ▾ 🏷️ Categ.│  │ Nombre         │ Tipo     │ Acciones  │    │
│  📋 Ver cat.│  ├────────────────┼──────────┼───────────┤    │
│  ➕ Nueva  │  │ Salarios       │ INGRESO  │ ✏️  🗑️   │    │
│             │  │ Freelance      │ INGRESO  │ ✏️  🗑️   │    │
│             │  │ Alimentación   │ GASTO    │ ✏️  🗑️   │    │
│             │  │ Transporte     │ GASTO    │ ✏️  🗑️   │    │
│             │  │ Servicios      │ GASTO    │ ✏️  🗑️   │    │
│             │  └────────────────┴──────────┴───────────┘    │
│             │                                               │
│             │  ┌──────────────────────────────────────┐    │
│             │  │ Nombre: [ Arriendo              ]    │    │
│             │  │ Tipo:   [ GASTO ▾               ]    │    │
│             │  │         [ Guardar ]                  │    │
│             │  └──────────────────────────────────────┘    │
└─────────────┴───────────────────────────────────────────────┘
```

---

## RF-08 — Registrar Deuda

**Narrativa:**  
Como usuario autenticado, quiero registrar una deuda indicando si yo debo o me deben, el monto, la persona involucrada, descripción y tipo de pago, para hacer seguimiento a mis compromisos financieros con otras personas.

**Explicación:**  
El sistema registra deudas de dos tipos: `YO_DEBO` (dinero que el usuario debe a alguien) y `ME_DEBEN` (dinero que alguien le debe al usuario). Al crear la deuda, el estado se asigna automáticamente como `PENDIENTE` sin que el usuario lo indique. El monto debe ser mayor a cero. El campo `tipoPago` indica el medio acordado para el pago (efectivo, transferencia, etc.).

**Mockup:**
```
┌─────────────┬────────────────────────────────────────┐
│ 💰 Finanzas │  🤝 Nueva Deuda                        │
│─────────────│────────────────────────────────────────│
│ ...         │  ┌──────────────────────────────────┐  │
│ ▾ 🤝 Deudas│  │ Persona *                        │  │
│  📋 Mis deu.│  │ ┌────────────────────────────┐   │  │
│  ➕ Nueva  │  │ │ Carlos Pérez               │   │  │
│             │  │ └────────────────────────────┘   │  │
│             │  │ Tipo de deuda *                  │  │
│             │  │ ┌────────────────────────────┐   │  │
│             │  │ │ ● YO DEBO  ○ ME DEBEN      │   │  │
│             │  │ └────────────────────────────┘   │  │
│             │  │ Monto *                          │  │
│             │  │ ┌────────────────────────────┐   │  │
│             │  │ │ $ 150.000                  │   │  │
│             │  │ └────────────────────────────┘   │  │
│             │  │ Tipo de pago                     │  │
│             │  │ ┌────────────────────────────┐   │  │
│             │  │ │ Efectivo ▾                 │   │  │
│             │  │ └────────────────────────────┘   │  │
│             │  │ Descripción                      │  │
│             │  │ ┌────────────────────────────┐   │  │
│             │  │ │ Préstamo para mercado       │   │  │
│             │  │ └────────────────────────────┘   │  │
│             │  │  [ Registrar deuda ]             │  │
│             │  └──────────────────────────────────┘  │
└─────────────┴────────────────────────────────────────┘
```

---

## RF-09 — Abonar a una Deuda

**Narrativa:**  
Como usuario autenticado, quiero registrar un abono parcial a una deuda pendiente, para ir reduciendo el saldo poco a poco y que el sistema registre automáticamente ese pago como un gasto en mi historial.

**Explicación:**  
Desde la lista de deudas, el usuario selecciona "Abonar" en una deuda con estado `PENDIENTE`. Ingresa el monto del abono, el cual no puede superar el saldo restante (`monto - montoPagado`). El sistema suma el abono al `montoPagado` y, si cubre el total, cambia el estado a `PAGADA` automáticamente. Además, crea un registro de `Gasto` equivalente al abono para mantener consistente el balance financiero del usuario.

**Mockup:**
```
┌─────────────┬─────────────────────────────────────────────────────┐
│ 💰 Finanzas │  🤝 Mis Deudas                    [➕ Nueva deuda] │
│─────────────│─────────────────────────────────────────────────────│
│ ...         │ ┌────────┬──────────┬──────┬──────────┬──────────┐  │
│ ▾ 🤝 Deudas│ │Persona │ Tipo     │Monto │ Estado   │Acciones  │  │
│  📋 Mis deu.│ ├────────┼──────────┼──────┼──────────┼──────────┤  │
│  ➕ Nueva  │ │Carlos  │↑ Yo debo │$150k │🟡PENDIENTE│✓ 💰 ✏️🗑️│  │
│             │ └────────┴──────────┴──────┴──────────┴──────────┘  │
│             │                                                      │
│             │ ┌──────────────────────────────────────────────┐    │
│             │ │ 💰 Abonar a Carlos:                          │    │
│             │ │  Monto: ┌───────────────┐                    │    │
│             │ │         │ $ 50.000      │  [Confirmar]       │    │
│             │ │         └───────────────┘  [Cancelar]        │    │
│             │ │  Saldo restante: $150.000                    │    │
│             │ └──────────────────────────────────────────────┘    │
│             │                                                      │
│             │  Total: $150.000                                     │
│             │  Abonado: $50.000  ·  Resta: $100.000               │
└─────────────┴─────────────────────────────────────────────────────┘
```

---

## RF-10 — Marcar Deuda como Pagada

**Narrativa:**  
Como usuario autenticado, quiero marcar una deuda como completamente pagada con un solo clic, para archivarla y limpiar mi lista de compromisos financieros pendientes.

**Explicación:**  
El botón "✓ Pagar" aparece únicamente en deudas con estado `PENDIENTE`. Al hacer clic, el sistema cambia el estado a `PAGADA` y establece `montoPagado = monto` (pago total). Si la deuda ya estaba pagada, el sistema rechaza la operación con un error. Una deuda pagada no puede volver a estado pendiente ni ser modificada.

**Mockup:**
```
┌─────────────┬─────────────────────────────────────────────────────┐
│ 💰 Finanzas │  🤝 Mis Deudas                                      │
│─────────────│─────────────────────────────────────────────────────│
│ ...         │ ┌────────┬──────┬──────────┬──────────────────────┐ │
│             │ │Persona │Monto │ Estado   │ Acciones             │ │
│             │ ├────────┼──────┼──────────┼──────────────────────┤ │
│             │ │Carlos  │$150k │🟡PENDIENTE│[✓ Pagar][💰][✏️][🗑️]│ │
│             │ │María   │$80k  │🟢 PAGADA  │              [🗑️]   │ │
│             │ └────────┴──────┴──────────┴──────────────────────┘ │
│             │                                                      │
│             │  Al hacer clic en [✓ Pagar]:                        │
│             │  ┌────────────────────────────────────┐             │
│             │  │ Carlos  │ $150k │ 🟢 PAGADA │ [🗑️] │            │
│             │  └────────────────────────────────────┘             │
└─────────────┴─────────────────────────────────────────────────────┘
```

---

## RF-11 — Gestionar Metas de Ahorro

**Narrativa:**  
Como usuario autenticado, quiero crear una meta de ahorro para un mes y año específicos con un monto objetivo, para establecer metas financieras mensuales y verificar si las cumplo.

**Explicación:**  
El usuario define cuánto quiere ahorrar en un mes y año determinados. El sistema valida que no exista ya una meta para ese mismo mes/año del mismo usuario. Al consultar la meta del mes actual, el sistema suma todos los ahorros del usuario en ese período y compara con el objetivo usando `verificarCumplimiento()`. Si el ahorro acumulado supera o iguala la meta, se muestra como "¡Cumplida!".

**Mockup:**
```
┌─────────────┬────────────────────────────────────────────┐
│ 💰 Finanzas │  🎯 Meta del Mes — Mayo 2026               │
│─────────────│────────────────────────────────────────────│
│ ...         │  ┌──────────────────────────────────────┐  │
│ ▾ 🎯 Metas │  │  Meta establecida:   $ 500.000       │  │
│  📋 Mis met.│  │  Ahorro acumulado:   $ 325.000       │  │
│  📅 Meta mes│  │                                      │  │
│  ➕ Nueva  │  │  ████████████░░░░░░   65%             │  │
│             │  │                                      │  │
│             │  │  Estado: ⏳ En progreso              │  │
│             │  │  Faltan: $ 175.000                   │  │
│             │  └──────────────────────────────────────┘  │
│             │                                            │
│             │  ┌──────────────────────────────────────┐  │
│             │  │  Nueva meta                          │  │
│             │  │  Monto: ┌────────────┐               │  │
│             │  │         │ $500.000   │               │  │
│             │  │         └────────────┘               │  │
│             │  │  Mes: [05▾]  Año: [2026▾]            │  │
│             │  │  [ Guardar meta ]                    │  │
│             │  └──────────────────────────────────────┘  │
└─────────────┴────────────────────────────────────────────┘
```

---

## RF-12 — Ver Ahorros Acumulados

**Narrativa:**  
Como usuario autenticado, quiero ver el listado de todos mis ahorros generados automáticamente al registrar ingresos, para saber cuánto he ahorrado en total y en qué fechas.

**Explicación:**  
Cada vez que se registra un ingreso con un porcentaje de ahorro mayor a cero, el sistema crea automáticamente un registro en la tabla `ahorro` con el monto calculado, el porcentaje aplicado, la fecha y la descripción. En la vista de ahorros se listan todos los registros del usuario mostrando monto, porcentaje, descripción y fecha. El total acumulado se muestra en la parte superior.

**Mockup:**
```
┌─────────────┬──────────────────────────────────────────────┐
│ 💰 Finanzas │  💰 Mis Ahorros                              │
│─────────────│──────────────────────────────────────────────│
│ ...         │  ┌──────────────────────────────────────┐    │
│ ▾ 💰 Ahorros│  │  Total ahorrado:  $ 475.000          │    │
│  📊 Mis aho.│  └──────────────────────────────────────┘    │
│  ➕ Regist. │                                              │
│             │  ┌────────┬──────┬───────────┬───────────┐   │
│             │  │ Monto  │  %   │Descripción│  Fecha    │   │
│             │  ├────────┼──────┼───────────┼───────────┤   │
│             │  │$250.000│ 10%  │Quincena.. │15/05/2026 │   │
│             │  │$150.000│ 10%  │Quincena.. │01/05/2026 │   │
│             │  │$ 75.000│  5%  │Freelance  │20/04/2026 │   │
│             │  └────────┴──────┴───────────┴───────────┘   │
└─────────────┴──────────────────────────────────────────────┘
```

---

## RF-13 — Ver Dashboard Financiero

**Narrativa:**  
Como usuario autenticado, quiero ver un panel de resumen con mi balance actual, total de ingresos, total de gastos y un desglose por tipo de pago, para tener una visión general e inmediata de mi situación financiera.

**Explicación:**  
El dashboard se carga al ingresar al sistema y muestra cuatro tarjetas resumen: balance total (ingresos - gastos), total de ingresos, total de gastos y total ahorrado. Adicionalmente, presenta una tabla de desglose por tipo de pago (Efectivo, Transferencia, Tarjeta, etc.) mostrando cuánto se ingresó, cuánto se gastó y el balance por cada medio. Los datos se calculan en el frontend combinando todos los movimientos del usuario mediante `forkJoin`.

**Mockup:**
```
┌─────────────┬─────────────────────────────────────────────────────┐
│ 💰 Finanzas │  📊 Dashboard                                       │
│─────────────│─────────────────────────────────────────────────────│
│ 📊 Dashboard│  ┌───────────┐ ┌───────────┐ ┌──────────┐ ┌──────┐ │
│ ▾ 💸 Movm. │  │  Balance  │ │ Ingresos  │ │  Gastos  │ │Ahorr.│ │
│ ...         │  │           │ │           │ │          │ │      │ │
│             │  │ $3.165.000│ │$3.200.000 │ │$ 335.000 │ │$475k │ │
│             │  └───────────┘ └───────────┘ └──────────┘ └──────┘ │
│             │                                                      │
│             │  Desglose por tipo de pago:                          │
│             │  ┌──────────────┬───────────┬──────────┬──────────┐ │
│             │  │ Tipo de pago │ Ingresos  │  Gastos  │ Balance  │ │
│             │  ├──────────────┼───────────┼──────────┼──────────┤ │
│             │  │ TRANSFERENCIA│ $3.000.000│ $150.000 │$2.850.000│ │
│             │  │ EFECTIVO     │ $  200.000│ $185.000 │$   15.000│ │
│             │  │ Sin especif. │ $       0 │ $      0 │$        0│ │
│             │  └──────────────┴───────────┴──────────┴──────────┘ │
└─────────────┴─────────────────────────────────────────────────────┘
```

---

## RF-14 — Generar Reporte Financiero Mensual

**Narrativa:**  
Como usuario autenticado, quiero generar un reporte financiero de un mes y año específicos, para analizar en detalle mis ingresos, gastos y balance en ese período.

**Explicación:**  
El usuario selecciona el mes y año para el reporte. El sistema suma todos los ingresos y gastos de ese período para el usuario, calcula el balance (`totalIngresos - totalGastos`) y devuelve un objeto `ReporteFinanciero` generado en memoria (no persiste en BD). Si no hay movimientos en el período, se muestra el reporte con valores en cero. El reporte incluye el nombre del usuario y puede mostrarse en pantalla.

**Mockup:**
```
┌─────────────┬────────────────────────────────────────────┐
│ 💰 Finanzas │  📋 Reporte Financiero                     │
│─────────────│────────────────────────────────────────────│
│ ...         │  Mes: [05 ▾]   Año: [2026 ▾]  [Generar]   │
│             │                                            │
│             │  ┌──────────────────────────────────────┐  │
│             │  │  ===== Reporte Financiero =====      │  │
│             │  │  Usuario:        Andrés Zapata       │  │
│             │  │  Período:        05 / 2026           │  │
│             │  │                                      │  │
│             │  │  Total ingresos: $ 3.200.000         │  │
│             │  │  Total gastos:   $   335.000         │  │
│             │  │  ─────────────────────────────       │  │
│             │  │  Balance:        $ 2.865.000  ✅     │  │
│             │  │  ================================    │  │
│             │  └──────────────────────────────────────┘  │
└─────────────┴────────────────────────────────────────────┘
```

---

## RF-15 — Menú de Navegación Dinámico

**Narrativa:**  
Como usuario autenticado, quiero navegar por un menú lateral cargado desde la base de datos, para que el sistema pueda actualizar las opciones de navegación sin necesidad de modificar el código fuente.

**Explicación:**  
Al iniciar sesión, el frontend realiza una petición `GET /menus` al backend, que retorna el árbol de menús almacenado en la tabla `menu`. Cada ítem tiene nombre, URL, ícono, orden y referencia a su padre (estructura recursiva padre-hijos). Los ítems raíz sin URL son grupos desplegables; los ítems con URL son enlaces de navegación. El `MenuSeeder` puebla la tabla automáticamente al primer arranque si está vacía.

**Mockup:**
```
┌────────────────────────┐
│ 💰 Finanzas            │  ← Cargado desde tabla 'menu' en BD
│────────────────────────│
│ 📊 Dashboard           │  ← ítem raíz con URL
│ ▾ 💸 Movimientos       │  ← grupo (sin URL, tiene hijos)
│    📈 Ingresos         │  ← hijo con URL
│    📉 Gastos           │  ← hijo con URL
│    ➕ Nuevo ingreso    │  ← hijo con URL
│    ➖ Nuevo gasto      │  ← hijo con URL
│ ▸ 🏷️ Categorías        │  ← grupo colapsado
│ ▾ 🤝 Deudas            │  ← grupo expandido
│    📋 Mis deudas       │
│    ➕ Nueva deuda      │
│ ▾ 🎯 Metas             │
│    📋 Mis metas        │
│    📅 Meta del mes     │
│    ➕ Nueva meta       │
│ ▾ 💰 Ahorros           │
│    📊 Mis ahorros      │
│    ➕ Registrar ahorro │
│────────────────────────│
│ 👤 andres              │
│ [ Salir ]              │
└────────────────────────┘
```

---
---

# REQUERIMIENTOS NO FUNCIONALES

---

## RNF-01 — Seguridad

**Narrativa:**  
Como administrador del sistema, quiero que todas las contraseñas estén encriptadas y que cada petición al servidor sea validada mediante un token, para proteger los datos financieros personales de los usuarios.

**Explicación:**  
El sistema implementa dos mecanismos de seguridad complementarios. Primero, las contraseñas se encriptan con **BCrypt** antes de almacenarse en la base de datos; nunca se guardan en texto plano. Segundo, la autenticación es **stateless** mediante **JWT (JSON Web Token)**: al iniciar sesión se emite un token firmado que el frontend adjunta en cada petición HTTP mediante `AuthInterceptor`. El `JwtFilter` de Spring Security valida el token antes de que cualquier petición llegue a los controladores. Las rutas públicas se limitan exclusivamente a `/auth/**`.

**Mockup:**
```
  Cliente                   Servidor
     │                          │
     │── POST /auth/login ──────►│
     │   { username, password }  │  1. Valida credenciales
     │                          │  2. Encripta con BCrypt
     │◄── 200 OK ───────────────│  3. Genera JWT
     │   { token: "eyJ..." }     │
     │                          │
     │── GET /deudas/1 ─────────►│
     │   Authorization: Bearer   │  4. JwtFilter valida token
     │   eyJ0eXAi...             │  5. Si inválido → 401
     │                          │  6. Si válido → procesa
     │◄── 200 OK ───────────────│
     │   [ lista de deudas ]     │
```

---

## RNF-02 — Usabilidad

**Narrativa:**  
Como usuario sin conocimientos técnicos, quiero que la interfaz sea clara, intuitiva y consistente, para poder registrar y consultar mis finanzas sin necesidad de un manual de usuario.

**Explicación:**  
La interfaz sigue un diseño consistente en todas las vistas: sidebar de navegación a la izquierda, contenido principal a la derecha, tablas con columnas claras y botones de acción estandarizados (✏️ editar, 🗑️ eliminar, ✓ pagar, 💰 abonar). Los estados se distinguen visualmente con badges de color (🟡 PENDIENTE, 🟢 PAGADA). Los errores de validación se muestran en línea, sin recargar la página. Los montos se formatean automáticamente en pesos colombianos (COP).

**Mockup:**
```
  Consistencia visual en toda la app:

  Badges de estado:        Botones de acción:
  ┌─────────────┐          ┌──────────────────────────┐
  │ 🟡 PENDIENTE│          │ [✓ Pagar] [💰] [✏️] [🗑️]│
  │ 🟢 PAGADA   │          └──────────────────────────┘
  │ 🔵 INGRESO  │
  │ 🔴 GASTO    │          Formato de moneda:
  └─────────────┘          $ 2.500.000 (COP)

  Mensaje de error inline:
  ┌────────────────────────────────┐
  │ Monto: [ -500 ]                │
  │ ⚠️ El monto debe ser mayor a 0 │
  └────────────────────────────────┘
```

---

## RNF-03 — Rendimiento

**Narrativa:**  
Como usuario, quiero que el sistema responda rápidamente a mis acciones, para no perder tiempo esperando mientras gestiono mis finanzas.

**Explicación:**  
El backend utiliza Spring Data JPA para generar consultas SQL optimizadas con filtros por `usuario_id`, evitando cargar datos de otros usuarios. El frontend carga los componentes de forma lazy (standalone components) y las peticiones al dashboard se realizan en paralelo con `forkJoin` para reducir el tiempo de espera. La autenticación stateless con JWT elimina la necesidad de consultar sesiones en base de datos en cada petición, reduciendo la latencia.

**Mockup:**
```
  Carga del Dashboard (peticiones en paralelo):

  Frontend                    Backend
     │                            │
     │── GET /movimientos/ingresos ►│ ──┐
     │── GET /movimientos/gastos ──►│   │ Simultáneas
     │── GET /ahorros/1 ───────────►│   │ (forkJoin)
     │── GET /deudas/1 ────────────►│ ──┘
     │                             │
     │◄── Respuestas combinadas ───│
     │                             │
     │  Dashboard renderizado      │
     │  en una sola espera         │
```

---

## RNF-04 — Mantenibilidad

**Narrativa:**  
Como desarrollador, quiero que el código esté organizado en capas bien definidas y que cada clase tenga una única responsabilidad, para poder agregar nuevas funcionalidades o corregir errores sin afectar el resto del sistema.

**Explicación:**  
El sistema sigue una **arquitectura por capas** estricta: `Controller → Service → Repository → Model`. Ningún controlador accede directamente al repositorio ni al modelo; todo pasa por el servicio. Las clases de modelo aplican el **Principio de Responsabilidad Única**: `Usuario` solo maneja datos del usuario, `Deuda` solo maneja lógica de deudas, etc. El uso de **inyección de dependencias por constructor** (sin Lombok) facilita las pruebas y el reemplazo de componentes.

**Mockup:**
```
  Estructura de capas — cada capa solo conoce a la siguiente:

  ┌─────────────────────────────────┐
  │       DeudaController           │  ← Solo recibe/devuelve HTTP
  │  depende de ▼                   │
  ├─────────────────────────────────┤
  │       DeudaService              │  ← Solo aplica lógica de negocio
  │  depende de ▼                   │
  ├─────────────────────────────────┤
  │  DeudaRepository + GastoRepo    │  ← Solo consulta/persiste datos
  │  depende de ▼                   │
  ├─────────────────────────────────┤
  │     Deuda (modelo JPA)          │  ← Solo representa los datos
  └─────────────────────────────────┘
```

---

## RNF-05 — Escalabilidad

**Narrativa:**  
Como desarrollador, quiero que el sistema esté diseñado para permitir agregar nuevos módulos (ej: presupuestos, inversiones) sin modificar los módulos existentes, para extender las funcionalidades del sistema en el futuro.

**Explicación:**  
La arquitectura por capas y el uso de Spring Boot permiten agregar nuevos módulos de forma independiente: basta con crear un nuevo `Model`, `Repository`, `Service` y `Controller` sin modificar los existentes. El menú dinámico almacenado en BD permite agregar nuevas rutas de navegación simplemente insertando registros en la tabla `menu`, sin tocar el código del frontend. La interfaz `Recurrente` puede ser implementada por nuevas clases en el futuro (ej: `Presupuesto implements Recurrente`).

**Mockup:**
```
  Agregar módulo "Presupuesto" sin modificar los existentes:

  Existente:              Nuevo (independiente):
  ┌───────────────┐       ┌──────────────────────┐
  │ DeudaController│      │ PresupuestoController │
  │ DeudaService   │      │ PresupuestoService    │
  │ DeudaRepository│      │ PresupuestoRepository │
  │ Deuda.java     │      │ Presupuesto.java      │
  └───────────────┘       └──────────────────────┘
         ↑                         ↑
    Sin cambios              Nuevo módulo
                         (solo insertar en BD:)
                    INSERT INTO menu VALUES
                    ('Presupuestos','/presupuestos','💼',7,null)
```

---

## RNF-06 — Disponibilidad y Persistencia de Datos

**Narrativa:**  
Como usuario, quiero que mis datos financieros se guarden de forma permanente en una base de datos, para que no se pierdan al cerrar el navegador o reiniciar el servidor.

**Explicación:**  
Todos los datos del sistema se persisten en **PostgreSQL 17** mediante **Hibernate/JPA** con `ddl-auto=update`. Esto garantiza que las tablas se creen y actualicen automáticamente al arrancar la aplicación sin perder datos existentes. La única excepción es `ReporteFinanciero`, que se genera en memoria bajo demanda porque sus datos ya están persistidos en las tablas `ingreso` y `gasto`. El `MenuSeeder` garantiza que los datos del menú existan siempre al arrancar, verificando antes de insertar para evitar duplicados.

**Mockup:**
```
  Ciclo de vida de los datos:

  Usuario cierra     Servidor se         Datos en
  el navegador   →   reinicia       →    PostgreSQL
       │                  │               intactos
       │                  │                  │
       ▼                  ▼                  ▼
  JWT expirado      Spring Boot         7 tablas con
  (debe re-login)   recarga tablas      todos los
                    (ddl=update)        registros
                    MenuSeeder          preservados
                    verifica si
                    tabla vacía
```

---

*Documento generado para el proyecto final de Programación Orientada a Objetos — UCC Pasto, 2026*
