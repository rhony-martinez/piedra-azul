# 🏥 Sistema de Agendamiento de Citas Médicas - Piedrazul

## 📌 Descripción

Este proyecto corresponde al desarrollo de un sistema de software para la gestión y agendamiento de citas médicas en la red de servicios de salud Piedrazul, en el marco del curso **Ingeniería de Software II (2026.1)**.

El sistema busca reemplazar y mejorar una solución de escritorio existente con limitaciones de escalabilidad y usabilidad, permitiendo optimizar procesos como:

- Autenticación de usuarios
- Gestión de roles
- Agendamiento de citas
- Organización operativa del personal médico

## 🎯 Objetivo del sistema

Diseñar e implementar una aplicación que permita:

- Automatizar el proceso de asignación de citas
- Reducir la carga operativa del personal médico
- Mejorar la experiencia del usuario
- Garantizar control de acceso basado en roles

## 🏗️ Estado actual del proyecto

Actualmente, el sistema se encuentra en la **primera iteración** (arquitectura monolítica en capas - estilo MVC).

### ✅ Funcionalidades implementadas

#### 🔐 Autenticación de usuarios
- Validación de campos obligatorios
- Verificación de credenciales
- Redirección según rol:
  - Administrador
  - Médico/Terapista
  - Agendador
  - Paciente

#### ⚠️ Validaciones básicas
- Control de campos vacíos en login
- Notificación de credenciales incorrectas

#### 🧱 Estructura inicial por capas
Separación en módulos tipo:
- `domain`
- `service`
- `repository` / `persistence`
- `presentation` (UI)

## 🧩 Arquitectura

Se está implementando una arquitectura **monolítica en capas (MVC)**, con el objetivo de:

- Separar responsabilidades
- Mejorar la mantenibilidad
- Facilitar futuras refactorizaciones (microservicios en iteraciones posteriores)

### Capas principales

1. **Presentación (UI)**  
   Interfaz gráfica (Swing/JavaFX) encargada de la interacción con el usuario.

2. **Aplicación / Servicio (service)**  
   Contiene la lógica de negocio y orquesta los casos de uso.

3. **Dominio (domain)**  
   Modela las entidades del sistema (Usuario, Rol, etc.).

4. **Persistencia (en construcción)**  
   Encargada del acceso a datos (base de datos).

## 🛠️ Tecnologías

- **Lenguaje:** Java
- **Interfaz gráfica:** Swing / JavaFX (según avance del equipo)
- **Base de datos:** PostgreSQL
- **Control de versiones:** Git + GitHub

## ⚙️ Ejecución del proyecto

> ⚠️ *Esta sección puede evolucionar conforme se estabilice la configuración.*

1. Clonar el repositorio:
   ```bash
   git clone <URL_DEL_REPOSITORIO>

2. Abrir el proyecto en el IDE (IntelliJ / NetBeans / Eclipse)

3. Ejecutar la clase principal del sistema

## 🚧 Trabajo en progreso
- Mejora del flujo de autenticación
- Gestión completa de usuarios
- Agendamiento de citas
- Validaciones de negocio

## 📚 Contexto académico

Proyecto desarrollado para:

- Universidad del Cauca
- Ingeniería de Software II
- Periodo 2026.1

## 👥 Equipo de desarrollo

- Dorado Joaqui Carlos Eduardo
- Martínez Benavides Rhony Daniel
- Moscoso Salazar Juan Esteban
- Obando Quintero Andrés Felipe
- Valdez Jurado Ronal Santiago