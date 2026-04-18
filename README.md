# 🏥 Sistema de Agendamiento de Citas Médicas - Piedrazul

## 📌 Descripción

Este proyecto corresponde al desarrollo de un sistema de software para la gestión y agendamiento de citas médicas en la red de servicios de salud Piedrazul, en el marco del curso **Ingeniería de Software II (2026.1)**.

El sistema evoluciona una solución de escritorio existente, incorporando mejoras en la organización del agendamiento, control de disponibilidad, reducción de errores operativos y una arquitectura orientada a la mantenibilidad y escalabilidad.

---

## 🎯 Objetivo del sistema

Diseñar e implementar una aplicación que permita:

- Automatizar el proceso de asignación de citas
- Reducir la carga operativa del personal médico
- Garantizar consistencia en la agenda de los profesionales
- Permitir agendamiento manual y autónomo
- Implementar control de acceso basado en roles

---

## 🏗️ Estado actual del proyecto

El sistema se encuentra en una **primera iteración avanzada**, bajo una arquitectura **monolítica en capas (MVC)**, incorporando múltiples **patrones de diseño** y **principios SOLID** para soportar escenarios más complejos de agendamiento.

---

## ✅ Funcionalidades implementadas

### 🔐 Autenticación y control de acceso
- Validación de campos obligatorios
- Verificación de credenciales contra base de datos
- Manejo de contraseñas cifradas (`PasswordUtils`)
- Redirección dinámica según rol:
  - Administrador
  - Médico/Terapista
  - Agendador
  - Paciente

---

### 📅 Agendamiento de citas

#### 🧑‍💼 Agendamiento manual

- Registro de citas por parte de agendadores o médicos
- Validación de disponibilidad del profesional
- Prevención de colisiones de agenda
- Persistencia en base de datos

#### 👤 Agendamiento autónomo (self-service)

- Permite al paciente agendar su propia cita
- Integración con proveedor externo simulado (Adapter)
- Reutilización de lógica mediante Template Method

#### 🔄 Gestión de citas
- Listado de citas
- Consulta de historial
- Reagendamiento de citas
- Cancelación de citas

Manejado con el patrón de diseño **State** para controlar los estados de la cita.

#### 🔔 Notificaciones
- Generación de notificaciones asociadas a eventos de citas
- Persistencia mediante repositorio dedicado

#### ✔️ Características implementadas

- Registro de:
  - Paciente
  - Médico
  - Fecha y hora de la cita
  - Observaciones
- Validación de disponibilidad del médico
- Prevención de colisiones de agenda
- Persistencia en base de datos

#### 🔒 Control de consistencia

El sistema garantiza que **no existan dos citas para el mismo médico en la misma fecha y hora**, mediante:

- Validación en lógica de negocio (`AppointmentService`)
- Restricción de unicidad en base de datos:
  ```sql
  UNIQUE (medico_id, fecha_hora_cita)

## 🧱 Uso de patrones de diseño

El sistema incorpora múltiples patrones para mejorar su diseño:

### 🏗️ Creacionales
- **Factory:** Creación centralizada de conexiones `(ConnectionFactory)`
- **Builder:** Construcción flexible de citas `(AppointmentBuilder)`

### 🧠 Comportamiento
- **Template Method:** Definición del flujo de agendamiento `(AppointmentScheduler)`
- **State**: Manejo de estados de la cita `(AppointmentState y derivados)`

### 🧩 Estructurales
- **Facade:** Simplificación del proceso de agendamiento `(AppointmentFacade)`
- **Adapter:** Integración con servicios externos simulados `(ExternalPatientAdapter)`
- **Decorator:** Extensión dinámica de funcionalidades de citas `(PriorityAppointment)`


## 🧩 Arquitectura

Se implementa una arquitectura **monolítica en capas**:

1. **Presentación (ui)**  
   - Interfaz gráfica desarrollada en Swing, encargada de la interacción con el usuario.
   - Formularios de agendamiento, login y gestión.

2. **Aplicación / Servicio (service)**  
   Contiene la lógica de negocio y reglas del sistema:
   - Validaciones
   - Orquestación del agendamiento (con **Facade**)
   - Flujos definidos (con **Template Method**)
   - Control de disponibilidad

3. **Dominio (domain)**  
   - Entidades (Appointment, Usuario, etc.)
   - Estados (state)
   - Construcción de objetos (builder)
   - Extensión de comportamiento (decorator)

4. **Persistencia (infrastructure.persistence)**  
   Encargada del acceso a datos:
   - Implementaciones de repositorios
   - Conexión a base de datos (`ConnectionFactory`)

## 🗂️ Estructura del proyecto

```
domain/
 ├── model/
 ├── builder/
 ├── state/
 ├── decorator/
 ├── service/
 │   ├── scheduler/   (Template Method)
 │   ├── facade/
 │   └── adapter/
 └── repository/

infrastructure/
 └── persistence/

main/

ui/
 ├── appointments/
 ├── panel/
 └── notifications/

 utils/
```


## 🗄️ Modelo de datos (resumen)

Se implementa un modelo relacional con entidades principales como:

- `Usuario`, `Rol`
- `Persona`, `Paciente`, `Medico`
- `Cita`
- `Notificacion`

Restricción clave

 `UNIQUE (medico_id, fecha_hora_cita)`

Garantiza la no duplicidad de citas por médico en el mismo horario.

## 🛠️ Tecnologías
- Lenguaje: Java
- UI: Swing
- Base de datos: PostgreSQL
- Persistencia: JDBC
- Control de versiones: Git + GitHub

⚙️ Ejecución del proyecto
Clonar el repositorio:
```
git clone <URL_DEL_REPOSITORIO>
```
Configurar la base de datos usando el script SQL del proyecto

Ejecutar la clase principal:
```com.mycompany.piedrazul.main.Piedrazul```

## 📈 Próximos pasos
- Validación avanzada de disponibilidad (horarios e intervalos)
- Manejo de festivos y excepciones
- Migración progresiva a microservicios
- Mejora de notificaciones

## 📚 Contexto académico
Proyecto desarrollado para:

Universidad del Cauca

Ingeniería de Software II

Periodo 2026.1
## 👥 Equipo de desarrollo
- Dorado Joaqui Carlos Eduardo
- Martínez Benavides Rhony Daniel
- Moscoso Salazar Juan Esteban
- Obando Quintero Andrés Felipe
- Valdez Jurado Ronal Santiago
