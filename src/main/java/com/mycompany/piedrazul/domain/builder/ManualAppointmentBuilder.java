package com.mycompany.piedrazul.domain.builder;

import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.Paciente;
import com.mycompany.piedrazul.domain.model.Usuario;
import java.time.LocalDateTime;

public class ManualAppointmentBuilder extends AppointmentBuilder {

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
            throw new IllegalArgumentException("Médico requerido");
        }
        appointment.setMedico(medico);
    }

    @Override
    public void buildFechaHora(LocalDateTime fechaHora) {
        if (fechaHora == null || fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Fecha inválida");
        }

        appointment.setFechaHora(
            fechaHora.withSecond(0).withNano(0)
        );
    }

    @Override
    public void buildCreadoPor(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario creador requerido");
        }
        appointment.setCreadoPor(usuario);
    }
}