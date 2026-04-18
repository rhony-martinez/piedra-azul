package com.mycompany.piedrazul.domain.service.scheduler;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;

import java.time.LocalDateTime;

public class SelfServiceAppointmentScheduler extends AppointmentScheduler {

    private static final int MAX_DIAS_ANTICIPACION = 30;

    public SelfServiceAppointmentScheduler(IAppointmentRepository repository) {
        super(repository);
    }

    @Override
    protected void validateUser(Appointment appointment) {

        if (appointment.getPaciente() == null) {
            throw new IllegalArgumentException("Paciente requerido");
        }

        if (appointment.getCreadoPor() == null) {
            throw new IllegalArgumentException("Usuario requerido");
        }
    }

    @Override
    protected void checkAvailability(Appointment appointment) {

        LocalDateTime fechaHora = appointment.getFechaHora();

        // 1. Validar fecha futura
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("No puede agendar en el pasado");
        }

        // 2. Límite de anticipación
        LocalDateTime limite = LocalDateTime.now().plusDays(MAX_DIAS_ANTICIPACION);

        if (fechaHora.isAfter(limite)) {
            throw new IllegalStateException(
                    "No puede agendar una cita más allá del límite permitido");
        }

        // 3. Solapamiento paciente
        boolean pacienteOcupado = repository.existsByPacienteAndFecha(
                appointment.getPaciente().getId(),
                fechaHora);

        if (pacienteOcupado) {
            throw new IllegalStateException(
                    "Usted ya tiene una cita en este horario");
        }

        // 4. Disponibilidad médico
        boolean medicoOcupado = repository.existsByMedicoAndFechaHora(
                appointment.getMedico().getId(),
                fechaHora);

        if (medicoOcupado) {
            throw new IllegalStateException(
                    "Horario no disponible para agendamiento");
        }
    }

    @Override
    protected void assignProfessional(Appointment appointment) {
        // correcto (no-op)
    }

    @Override
    protected void confirmAppointment(Appointment appointment) {
        appointment.confirmar(); // transición real PROGRAMADA a CONFIRMADA por ser autónomo
    }
}