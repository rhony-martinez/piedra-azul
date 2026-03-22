package com.mycompany.piedrazul.domain.builder;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.model.Usuario;
import java.time.LocalDateTime;

public abstract class AppointmentBuilder {
    protected Appointment appointment;
    
    public Appointment getAppointment() {
        return appointment;
    }
    
    public void crearNuevaAppointment() {
        appointment = new Appointment();
        // Valores por defecto
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
    }
    
    // Métodos abstractos que deben implementar los builders concretos
    public abstract void buildPatientData(Usuario patient);
    public abstract void buildProfessionalData(Usuario professional);
    public abstract void buildDateTime(LocalDateTime dateTime);
    public abstract void buildAppointmentType(String type);
    public abstract void buildReason(String reason);
    public abstract void buildCreatedBy(Usuario createdBy);
    
    // Métodos opcionales con implementación por defecto
    public void buildNotes(String notes) {
        if (appointment != null) {
            appointment.setNotes(notes);
        }
    }
    
    public void buildStatus(AppointmentStatus status) {
        if (appointment != null) {
            appointment.setStatus(status);
        }
    }
}