package com.mycompany.piedrazul.domain.model;

import com.mycompany.piedrazul.domain.state.AppointmentState;
import com.mycompany.piedrazul.domain.state.ProgramadaState;
import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private Paciente paciente;     
    private Medico medico;         
    private Usuario creadoPor;
    private LocalDateTime fechaHora;
    private AppointmentStatus estado;
    private String observacion;
    private LocalDateTime creadoEn;
    
    // State pattern
    private AppointmentState appointmentState;

    public Appointment() {
        this.appointmentState = new ProgramadaState();
        this.estado = AppointmentStatus.PROGRAMADA;
    }
    
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
    
    public AppointmentState getAppointmentState() {
        return appointmentState;
    }
    
    public void setAppointmentState(AppointmentState state) {
        this.appointmentState = state;
        // Sincronizar con el enum
        switch (state.getNombreEstado()) {
            case "PROGRAMADA": 
                this.estado = AppointmentStatus.PROGRAMADA; 
                break;
            case "CONFIRMADA": 
                this.estado = AppointmentStatus.CONFIRMADA; 
                break;
            case "CANCELADA": 
                this.estado = AppointmentStatus.CANCELADA; 
                break;
            case "ATENDIDA": 
                this.estado = AppointmentStatus.ATENDIDA; 
                break;
            case "NO_ASISTIDA": 
                this.estado = AppointmentStatus.NO_ASISTIDA; 
                break;
            case "REAGENDADA": 
                this.estado = AppointmentStatus.REAGENDADA; 
                break;
            default: 
                this.estado = AppointmentStatus.PROGRAMADA;
        }
    }
    
    // Métodos de transición del patrón State
    public void programar() { 
        appointmentState.programar(this); 
    }
    
    public void confirmar() { 
        appointmentState.confirmar(this); 
    }
    
    public void atender() { 
        appointmentState.atender(this); 
    }
    
    public void noAsistir() { 
        appointmentState.noAsistir(this); 
    }
    
    public void reagendar() { 
        appointmentState.reagendar(this); 
    }
    
    public void cancelar() { 
        appointmentState.cancelar(this); 
    }
    
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", paciente=" + (paciente != null ? paciente.getPrimerNombre() + " " + paciente.getPrimerApellido() : "null") +
                ", medico=" + (medico != null ? medico.getPrimerNombre() + " " + medico.getPrimerApellido() : "null") +
                ", fechaHora=" + fechaHora +
                ", estado=" + estado +
                ", observacion='" + observacion + '\'' +
                '}';
    }
}