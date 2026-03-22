package com.mycompany.piedrazul.domain.model;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private Usuario patient;           // Datos del paciente
    private Usuario professional;       // Datos del médico/terapista
    private LocalDateTime dateTime;     // Fecha y hora de la cita
    private String appointmentType;     // Tipo de cita (consulta, terapia, control)
    private AppointmentStatus status;    // Estado de la cita
    private String reason;              // Motivo de la consulta
    private String notes;               // Notas adicionales
    private Usuario createdBy;           // Usuario que creó la cita
    private LocalDateTime createdAt;     // Fecha de creación
    private Appointment originalAppointment; // Para citas reprogramadas
    
    // Constructor vacío
    public Appointment() {}
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Usuario getPatient() { return patient; }
    public void setPatient(Usuario patient) { this.patient = patient; }
    
    public Usuario getProfessional() { return professional; }
    public void setProfessional(Usuario professional) { this.professional = professional; }
    
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    
    public String getAppointmentType() { return appointmentType; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }
    
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public Usuario getCreatedBy() { return createdBy; }
    public void setCreatedBy(Usuario createdBy) { this.createdBy = createdBy; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
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