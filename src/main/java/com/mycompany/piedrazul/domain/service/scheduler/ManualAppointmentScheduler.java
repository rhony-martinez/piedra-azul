/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.service.scheduler;

import java.time.LocalDateTime;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;

/**
 *
 * @author asus
 */
public class ManualAppointmentScheduler extends AppointmentScheduler {

    public ManualAppointmentScheduler(IAppointmentRepository repository) {
        super(repository);
    }

    @Override
    protected void validateUser(Appointment appointment) {
        if (appointment.getCreadoPor() == null) {
            throw new IllegalArgumentException("Usuario creador requerido");
        }
    }

    @Override
    protected void checkAvailability(Appointment appointment) {

        LocalDateTime fechaNormalizada = appointment.getFechaHora()
                .withSecond(0)
                .withNano(0);

        appointment.setFechaHora(fechaNormalizada);

        boolean ocupado = repository.existsByMedicoAndFechaHora(
                appointment.getMedico().getId(),
                appointment.getFechaHora());

        boolean agendado = repository.existsByPacienteAndFecha(
                appointment.getPaciente().getId(),
                appointment.getFechaHora());

        if (ocupado) {
            throw new IllegalStateException("El médico ya tiene una cita en ese horario");
        }

        if (agendado) {
            throw new IllegalStateException("El paciente ya tiene una cita en ese horario");
        }
    }

    @Override
    protected void assignProfessional(Appointment appointment) {
        // En manual ya viene asignado, no hacer nada
    }

    @Override
    protected void confirmAppointment(Appointment appointment) {
        // La cita ya está en PROGRAMADA por defecto
    }
}
