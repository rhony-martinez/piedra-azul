package com.mycompany.piedrazul.domain.service;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.builder.ManualAppointmentBuilder;
import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService {

    private final IAppointmentRepository appointmentRepository;

    public AppointmentService(IAppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment crearCita(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("La cita no puede ser nula");
        }
        if (appointment.getPatient() == null || appointment.getPatient().getId() <= 0) {
            throw new IllegalArgumentException("Paciente inválido");
        }
        if (appointment.getProfessional() == null || appointment.getProfessional().getId() <= 0) {
            throw new IllegalArgumentException("Profesional inválido");
        }
        if (appointment.getDateTime() == null) {
            throw new IllegalArgumentException("Fecha y hora requeridas");
        }

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> obtenerCitasPaciente(Usuario paciente) {
        return appointmentRepository.findByPatient(paciente);
    }

    public List<Appointment> obtenerCitasProfesional(Usuario profesional) {
        return appointmentRepository.findByProfessional(profesional);
    }

    public List<Appointment> obtenerProximasCitas(Usuario usuario) {
        return appointmentRepository.findUpcoming(usuario);
    }

    public List<Appointment> obtenerHistorial(Usuario usuario) {
        return appointmentRepository.findHistory(usuario);
    }

    public boolean confirmarCita(int id) {
        Appointment cita = appointmentRepository.findById(id);
        if (cita != null) {
            cita.setStatus(AppointmentStatus.CONFIRMED);
            return appointmentRepository.update(cita);
        }
        return false;
    }

    public boolean cancelarCita(int id) {
        return appointmentRepository.cancel(id);
    }

    public boolean reprogramarCita(Appointment nuevaCita) {
        if (nuevaCita.getOriginalAppointment() == null) {
            throw new IllegalArgumentException("Debe especificar la cita original");
        }
        return appointmentRepository.save(nuevaCita) != null;
    }

    public Appointment crearCitaManual(
            Paciente paciente,
            Medico medico,
            LocalDateTime fechaHora,
            Usuario usuarioCreador,
            String observacion) {

        // 1. Validar fecha límite
        if (fechaHora.isAfter(LocalDateTime.now().plusMonths(3))) {
            throw new IllegalArgumentException("No puede agendar una cita más allá de 3 meses");
        }

        // 2. Validar solapamiento paciente
        boolean pacienteOcupado = appointmentRepository
                .existsByPacienteAndFecha(paciente.getId(), fechaHora);

        if (pacienteOcupado) {
            throw new IllegalArgumentException(
                    "El paciente ya tiene una cita en ese horario");
        }

        // 3. Construcción con Builder
        ManualAppointmentBuilder builder = new ManualAppointmentBuilder();
        builder.crearNueva();
        builder.buildPaciente(paciente);
        builder.buildMedico(medico);
        builder.buildFechaHora(fechaHora);
        builder.buildCreadoPor(usuarioCreador);
        builder.buildObservacion(observacion);

        Appointment cita = builder.getResult();

        // 4. Guardar (maneja simultaneidad)
        try {
            return appointmentRepository.save(cita);
        } catch (Exception e) {
            throw new IllegalArgumentException("Horario no disponible");
        }
    }
}