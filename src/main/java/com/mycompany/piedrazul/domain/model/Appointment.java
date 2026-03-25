package com.mycompany.piedrazul.domain.model;

import java.time.LocalDateTime;

public class Appointment {
    private int id;

    private Paciente paciente;     
    private Medico medico;         
    private Usuario creadoPor;     // quién agenda

    private LocalDateTime fechaHora;
    private AppointmentStatus estado;
    private String observacion;

    private LocalDateTime creadoEn;

    public Appointment() {}
    
    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Usuario getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Usuario creadoPor) {
        this.creadoPor = creadoPor;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public AppointmentStatus getEstado() {
        return estado;
    }

    public void setEstado(AppointmentStatus estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
    
    public Appointment getOriginalAppointment() { return originalAppointment; }
    public void setOriginalAppointment(Appointment originalAppointment) { 
        this.originalAppointment = originalAppointment; 
    }
    
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patient=" + (patient != null ? patient.getNombreCompleto() : "null") +
                ", professional=" + (professional != null ? professional.getNombreCompleto() : "null") +
                ", dateTime=" + dateTime +
                ", type='" + appointmentType + '\'' +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }
}