package com.mycompany.piedrazul.domain.builder;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.Paciente;
import com.mycompany.piedrazul.domain.model.Usuario;
import java.time.LocalDateTime;

public abstract class AppointmentBuilder {

    protected Appointment appointment;

    public void crearNueva() {
        appointment = new Appointment();
        appointment.setCreadoEn(LocalDateTime.now());
        appointment.setEstado(AppointmentStatus.PROGRAMADA);
    }

    public Appointment getResult() {
        return appointment;
    }

    public abstract void buildPaciente(Paciente paciente);
    public abstract void buildMedico(Medico medico);
    public abstract void buildFechaHora(LocalDateTime fechaHora);
    public abstract void buildCreadoPor(Usuario usuario);

    public void buildObservacion(String observacion) {
        appointment.setObservacion(observacion);
    }
}