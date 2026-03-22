package com.mycompany.piedrazul.domain.builder;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.model.Usuario;
import java.time.LocalDateTime;

public class AppointmentDirector {
    
    private AppointmentBuilder appointmentBuilder;
    
    public void setAppointmentBuilder(AppointmentBuilder builder) {
        this.appointmentBuilder = builder;
    }
    
    public Appointment getAppointment() {
        return appointmentBuilder.getAppointment();
    }
    
    // Construir cita manual (por médico/agendador)
    public Appointment buildManualAppointment(Usuario patient, Usuario professional, 
                                              LocalDateTime dateTime, String type, 
                                              String reason, String notes, 
                                              Usuario createdBy) {
        appointmentBuilder.crearNuevaAppointment();
        appointmentBuilder.buildPatientData(patient);
        appointmentBuilder.buildProfessionalData(professional);
        appointmentBuilder.buildDateTime(dateTime);
        appointmentBuilder.buildAppointmentType(type);
        appointmentBuilder.buildReason(reason);
        appointmentBuilder.buildNotes(notes);
        appointmentBuilder.buildCreatedBy(createdBy);
        
        return appointmentBuilder.getAppointment();
    }
    
    // Construir cita por autoservicio (paciente)
    public Appointment buildSelfServiceAppointment(Usuario patient, Usuario professional, 
                                                   LocalDateTime dateTime, String type, 
                                                   String reason, Usuario createdBy) {
        appointmentBuilder.crearNuevaAppointment();
        appointmentBuilder.buildPatientData(patient);
        appointmentBuilder.buildProfessionalData(professional);
        appointmentBuilder.buildDateTime(dateTime);
        appointmentBuilder.buildAppointmentType(type);
        appointmentBuilder.buildReason(reason);
        appointmentBuilder.buildCreatedBy(createdBy);
        
        return appointmentBuilder.getAppointment();
    }
    
    // Construir cita reprogramada
    public Appointment buildRescheduledAppointment(Appointment originalAppointment, 
                                                   Usuario professional, 
                                                   LocalDateTime newDateTime, 
                                                   Usuario rescheduledBy) {
        // Nota: El builder ya debe estar configurado con la cita original
        appointmentBuilder.crearNuevaAppointment();
        appointmentBuilder.buildProfessionalData(professional);
        appointmentBuilder.buildDateTime(newDateTime);
        appointmentBuilder.buildCreatedBy(rescheduledBy);
        
        return appointmentBuilder.getAppointment();
    }
    
    // Construir cita reprogramada (manteniendo el mismo profesional)
    public Appointment buildRescheduledAppointmentSameProfessional(Appointment originalAppointment, 
                                                                   LocalDateTime newDateTime, 
                                                                   Usuario rescheduledBy) {
        return buildRescheduledAppointment(originalAppointment, 
                                           originalAppointment.getProfessional(), 
                                           newDateTime, 
                                           rescheduledBy);
    }
}