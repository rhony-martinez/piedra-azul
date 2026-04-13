package com.mycompany.piedrazul.domain.builder;

import com.mycompany.piedrazul.domain.model.Paciente;
import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.Usuario;

import java.time.LocalDateTime;

/**
 * Builder para citas creadas por autoservicio (paciente).
 */
public class SelfServiceAppointmentBuilder extends AppointmentBuilder {

    @Override
    public void crearNueva() {
        super.crearNueva();

        //  NO seteamos estado aquí, lo hace el Template
        appointment.setCreadoEn(LocalDateTime.now());
    }

    @Override
    public void buildPaciente(Paciente paciente) {
        if (appointment == null) return;

        if (paciente == null) {
            throw new IllegalArgumentException("Paciente requerido");
        }

        appointment.setPaciente(paciente);
    }

    @Override
    public void buildMedico(Medico medico) {
        if (appointment == null) return;

        if (medico == null) {
            throw new IllegalArgumentException("Debe seleccionar un médico");
        }

        appointment.setMedico(medico);
    }

    @Override
    public void buildFechaHora(LocalDateTime fechaHora) {
        if (appointment == null) return;

        if (fechaHora == null) {
            throw new IllegalArgumentException("Fecha y hora requeridas");
        }

        appointment.setFechaHora(fechaHora);
    }

    @Override
    public void buildCreadoPor(Usuario usuario) {
        if (appointment == null) return;

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario requerido");
        }

        appointment.setCreadoPor(usuario);
    }

    @Override
    public void buildObservacion(String observacion) {
        if (appointment == null) return;

        // En autoservicio puede ser opcional
        appointment.setObservacion(
                observacion != null ? observacion : "Cita generada por autoservicio"
        );
    }
}