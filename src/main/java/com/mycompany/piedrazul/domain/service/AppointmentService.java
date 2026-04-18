package com.mycompany.piedrazul.domain.service;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;

import java.time.LocalDate;
import java.util.ArrayList;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService {

    private final IAppointmentRepository appointmentRepository;

    public AppointmentService(IAppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // =========================
    // CREACIÓN BÁSICA
    // =========================
    public Appointment crearCita(Appointment appointment) {
        validarCita(appointment);
        return appointmentRepository.save(appointment);
    }

    private void validarCita(Appointment appointment) {

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

        if (appointment.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede crear una cita en el pasado");
        }
    }

    // =========================
    // CONSULTAS
    // =========================
    public Appointment obtenerCitaPorId(int id) {
        return appointmentRepository.findById(id);
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

    // =========================
    // ACTUALIZACIÓN
    // =========================
    public boolean actualizarCita(Appointment appointment) {

        if (appointment == null || appointment.getId() <= 0) {
            throw new IllegalArgumentException("Cita inválida");
        }

        return appointmentRepository.update(appointment);
    }

    public boolean cancelarCita(int id) {

        Appointment cita = appointmentRepository.findById(id);

        if (cita == null) {
            return false;
        }

        cita.cancelar();

        return appointmentRepository.update(cita);
    }

    // =========================
    // FILTROS
    // =========================
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

    // =========================
    // DISPONIBILIDAD
    // =========================
    public boolean verificarDisponibilidadMedico(int medicoId, LocalDateTime fechaHora) {

        LocalDateTime normalizada = fechaHora.withSecond(0).withNano(0);

        return !appointmentRepository.existsByMedicoAndFechaHora(medicoId, normalizada);
    }

}