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
    }

    @Override
    public void buildPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente requerido");
        }
        appointment.setPaciente(paciente);
    }

    @Override
    public void buildMedico(Medico medico) {
        if (medico == null) {
            throw new IllegalArgumentException("Debe seleccionar un médico");
        }
        appointment.setMedico(medico);
    }

    @Override
    public void buildFechaHora(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("Fecha y hora requeridas");
        }

        appointment.setFechaHora(
                fechaHora.withSecond(0).withNano(0));
    }

    @Override
    public void buildCreadoPor(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario requerido");
        }
        appointment.setCreadoPor(usuario);
    }

    @Override
    public void buildObservacion(String observacion) {
        appointment.setObservacion(
                (observacion == null || observacion.isBlank())
                        ? "Cita generada por autoservicio"
                        : observacion);
    }
}