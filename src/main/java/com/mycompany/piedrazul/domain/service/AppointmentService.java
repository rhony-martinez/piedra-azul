package com.mycompany.piedrazul.domain.service;

import com.mycompany.piedrazul.domain.builder.AppointmentDirector;
import com.mycompany.piedrazul.domain.builder.ManualAppointmentBuilder;
import com.mycompany.piedrazul.domain.builder.SelfServiceAppointmentBuilder;
import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.Paciente;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;
import com.mycompany.piedrazul.domain.service.scheduler.ManualAppointmentScheduler;
import com.mycompany.piedrazul.domain.service.scheduler.SelfServiceAppointmentScheduler;

import java.time.LocalDate;
import java.util.ArrayList;

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

    public List<Appointment> obtenerTodasLasCitas() {
        return appointmentRepository.findAll();
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

    // Obtener citas por médico y fecha
    public List<Appointment> obtenerCitasPorMedicoYFecha(int medicoId, LocalDate fecha) {
        List<Appointment> todas = appointmentRepository.findAll();
        List<Appointment> filtradas = new ArrayList<>();

        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.atTime(23, 59, 59);

        for (Appointment cita : todas) {
            if (cita.getFechaHora() == null)
                continue;

            boolean coincideMedico = (medicoId == 0) ||
                    (cita.getMedico() != null && cita.getMedico().getId() == medicoId);
            boolean coincideFecha = !cita.getFechaHora().isBefore(inicioDia) &&
                    !cita.getFechaHora().isAfter(finDia);

            if (coincideMedico && coincideFecha) {
                filtradas.add(cita);
            }
        }

        return filtradas;
    }

    public boolean verificarDisponibilidadMedico(int medicoId, LocalDateTime fechaHora) {
        // Redondear a minutos para evitar problemas con segundos
        LocalDateTime fechaHoraSinSegundos = fechaHora.withSecond(0).withNano(0);
        boolean existe = appointmentRepository.existsByMedicoAndFechaHora(medicoId, fechaHoraSinSegundos);
        return !existe; // Retorna true si NO existe (está disponible)
    }

    public Appointment crearCitaAutonoma(
            Paciente paciente,
            Medico medico,
            LocalDateTime fechaHora,
            Usuario usuario,
            String observacion) {
        // 1. Builder
        AppointmentDirector director = new AppointmentDirector();
        SelfServiceAppointmentBuilder builder = new SelfServiceAppointmentBuilder();
        director.setBuilder(builder);
        Appointment cita = director.buildSelfServiceAppointment(
                paciente,
                medico,
                fechaHora,
                usuario,
                observacion);
        // 2. Template Method
        SelfServiceAppointmentScheduler scheduler = new SelfServiceAppointmentScheduler(appointmentRepository);
        scheduler.schedule(cita);

        return crearCita(cita);
    }
}