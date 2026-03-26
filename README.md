# 🏥 Sistema de Agendamiento de Citas Médicas - Piedrazul

## 📌 Descripción

Este proyecto corresponde al desarrollo de un sistema de software para la gestión y agendamiento de citas médicas en la red de servicios de salud Piedrazul, en el marco del curso **Ingeniería de Software II (2026.1)**.

El sistema busca evolucionar una solución de escritorio existente, mejorando la organización del proceso de asignación de citas, reduciendo errores humanos y facilitando la gestión operativa del personal médico.

---

## 🎯 Objetivo del sistema

Diseñar e implementar una aplicación que permita:

- Automatizar el proceso de asignación de citas
- Reducir la carga operativa del personal médico
- Garantizar consistencia en la agenda de los profesionales
- Implementar control de acceso basado en roles

---

## 🏗️ Estado actual del proyecto

El sistema se encuentra en la **primera iteración**, bajo una arquitectura **monolítica en capas (MVC)**, cumpliendo con un conjunto inicial de funcionalidades de alto valor.

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

### 📅 Agendamiento manual de citas (Implementación principal)

Se implementó el caso de uso de **agendamiento manual de citas**, permitiendo a usuarios con rol de **agendador o médico** registrar citas en el sistema.

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

Se implementa el patrón **Builder** para la construcción de citas:

- `AppointmentBuilder`
- `ManualAppointmentBuilder`
- `RescheduledAppointmentBuilder`
- `SelfServiceAppointmentBuilder`
- `AppointmentDirector`

Esto permite:

- Separar la construcción del objeto `Appointment`
- Facilitar la extensión a otros tipos de agendamiento
- Mejorar la mantenibilidad del sistema

## 🧩 Estructura por capas implementada

El sistema sigue una organización clara basada en responsabilidades:
```
domain/
 ├── model/        → Entidades del dominio (Appointment, Usuario, Medico, etc.)
 ├── service/      → Lógica de negocio (AppointmentService)
 ├── repository/   → Interfaces de acceso a datos
 └── builder/      → Construcción de objetos complejos

infrastructure/
 └── persistence/  → Implementaciones de repositorios + conexión a BD

ui/
 ├── panel/        → Paneles por rol
 ├── appointments/ → Interfaces de gestión de citas
 └── frames        → Login y navegación principal
```
## 🧩 Arquitectura

Se implementa una arquitectura **monolítica en capas**:

1. **Presentación (ui)**  
   Interfaz gráfica desarrollada en Swing, encargada de la interacción con el usuario.

2. **Aplicación / Servicio (service)**  
   Contiene la lógica de negocio y reglas del sistema:
   - Validaciones
   - Orquestación del agendamiento
   - Control de disponibilidad

3. **Dominio (domain)**  
   Define las entidades principales:
   - `Appointment`
   - `Usuario`
   - `Medico`
   - `Paciente`

4. **Persistencia (infrastructure.persistence)**  
   Encargada del acceso a datos:
   - Implementaciones de repositorios
   - Conexión a base de datos (`ConnectionFactory`)


## 🗄️ Modelo de datos (resumen)

Se implementa un modelo relacional con entidades principales como:

- `Usuario`, `Rol`
- `Persona`, `Paciente`, `Medico`
- `Cita`
- `HistoriaClinica`

Restricción clave

 `UNIQUE (medico_id, fecha_hora_cita)`

Garantiza la no duplicidad de citas por médico en el mismo horario.

🛠️ Tecnologías
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
- Validación avanzada de disponibilidad (intervalos y horarios)
- Manejo de festivos
- Agendamiento autónomo de citas
- Auditoría de acciones
- Exportación de citas

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