package com.mycompany.piedrazul.domain.service;

import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.builder.AppointmentDirector;
import com.mycompany.piedrazul.domain.builder.ManualAppointmentBuilder;
import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.Paciente;
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
        if (appointment.getPaciente() == null || appointment.getPaciente().getId() <= 0) {
            throw new IllegalArgumentException("Paciente inválido");
        }
        if (appointment.getMedico() == null || appointment.getMedico().getId() <= 0) {
            throw new IllegalArgumentException("Profesional inválido");
        }
        if (appointment.getFechaHora() == null) {
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

    /*
     * public boolean confirmarCita(int id) {
     * Appointment cita = appointmentRepository.findById(id);
     * if (cita != null) {
     * cita.setStatus(AppointmentStatus.CONFIRMED);
     * return appointmentRepository.update(cita);
     * }
     * return false;
     * }
     */

    public boolean cancelarCita(int id) {
        return appointmentRepository.cancel(id);
    }

    /*
     * public boolean reprogramarCita(Appointment nuevaCita) {
     * if (nuevaCita.getOriginalAppointment() == null) {
     * throw new IllegalArgumentException("Debe especificar la cita original");
     * }
     * return appointmentRepository.save(nuevaCita) != null;
     * }
     */

    public Appointment crearCitaManual(
            Paciente paciente,
            Medico medico,
            LocalDateTime fechaHora,
            Usuario usuarioCreador,
            String observacion) {

        fechaHora = normalizarHora(fechaHora);

        // 1. Validación solapamiento paciente
        if (appointmentRepository.existsByPacienteAndFecha(paciente.getId(), fechaHora)) {
            throw new IllegalArgumentException(
                    "El paciente ya tiene agendada una cita en este horario");
        }

        // 2. Validación simultaneidad (MÉDICO OCUPADO)
        if (appointmentRepository.existsByMedicoAndFecha(medico.getId(), fechaHora)) {
            throw new IllegalArgumentException(
                    "Horario no disponible para agendamiento");
        }

        AppointmentDirector director = new AppointmentDirector();
        ManualAppointmentBuilder builder = new ManualAppointmentBuilder();

        director.setBuilder(builder);

        Appointment cita = director.buildManualAppointment(
                paciente,
                medico,
                fechaHora,
                usuarioCreador,
                observacion);

        try {
            return appointmentRepository.save(cita);
        } catch (Exception e) {
            if (e.getMessage().contains("uq_med_fecha_hora")) {
                throw new IllegalArgumentException("Horario no disponible para agendamiento");
            }
            throw e;
        }
    }

    private LocalDateTime normalizarHora(LocalDateTime fecha) {
        return fecha.withMinute(0).withSecond(0).withNano(0);
    }

    public List<Appointment> listar(String medico, String fecha) {
        return appointmentRepository.listar(medico, fecha);
    }

    public List<Appointment> listarPorMedicoYFecha(String medico, String fecha) {
        return appointmentRepository.listarPorMedicoYFecha(medico, fecha);
    }
}