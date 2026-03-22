package com.mycompany.piedrazul.domain.builder;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.model.Usuario;
import java.time.LocalDateTime;

public class RescheduledAppointmentBuilder extends AppointmentBuilder {
    
    private Appointment originalAppointment;
    
    public RescheduledAppointmentBuilder(Appointment originalAppointment) {
        this.originalAppointment = originalAppointment;
    }
    
    @Override
    public void crearNuevaAppointment() {
        super.crearNuevaAppointment();
        
        if (originalAppointment != null && appointment != null) {
            // Copiar datos de la cita original
            appointment.setPatient(originalAppointment.getPatient());
            appointment.setProfessional(originalAppointment.getProfessional());
            appointment.setAppointmentType(originalAppointment.getAppointmentType());
            appointment.setReason(originalAppointment.getReason());
            appointment.setOriginalAppointment(originalAppointment);
            appointment.setStatus(AppointmentStatus.RESCHEDULED);
            
            // Agregar nota sobre reprogramación
            String originalNotes = originalAppointment.getNotes();
            String rescheduleNote = "CITA REPROGRAMADA - Original: " + 
                                   originalAppointment.getDateTime() + 
                                   (originalNotes != null ? " - Notas originales: " + originalNotes : "");
            appointment.setNotes(rescheduleNote);
        }
    }
    
    @Override
    public void buildPatientData(Usuario patient) {
        if (appointment != null && patient != null) {
            // Si se proporciona un paciente diferente, validar
            if (!patient.equals(originalAppointment.getPatient())) {
                throw new IllegalArgumentException("No se puede cambiar el paciente al reprogramar");
            }
        }
    }
    
    @Override
    public void buildProfessionalData(Usuario professional) {
        if (appointment != null) {
            // Permitir cambiar de profesional al reprogramar
            if (professional != null && professional.getRol() == com.mycompany.piedrazul.domain.model.Rol.MEDICO_TERAPISTA) {
                appointment.setProfessional(professional);
            } else {
                throw new IllegalArgumentException("Profesional inválido");
            }
        }
    }
    
    @Override
    public void buildDateTime(LocalDateTime dateTime) {
        if (appointment != null) {
            if (dateTime != null && dateTime.isAfter(LocalDateTime.now())) {
                // No puede ser la misma fecha que la original
                if (originalAppointment != null && dateTime.equals(originalAppointment.getDateTime())) {
                    throw new IllegalArgumentException("La nueva fecha debe ser diferente a la original");
                }
                appointment.setDateTime(dateTime);
            } else {
                throw new IllegalArgumentException("La nueva fecha debe ser futura");
            }
        }
    }
    
    @Override
    public void buildAppointmentType(String type) {
        if (appointment != null) {
            // Mantener el mismo tipo de cita
            appointment.setAppointmentType(originalAppointment.getAppointmentType());
        }
    }
    
    @Override
    public void buildReason(String reason) {
        if (appointment != null) {
            // Mantener el mismo motivo
            appointment.setReason(originalAppointment.getReason());
        }
    }
    
    @Override
    public void buildCreatedBy(Usuario createdBy) {
        if (appointment != null) {
            // Quien reprograma puede ser diferente
            if (createdBy != null && 
                (createdBy.getRol() == com.mycompany.piedrazul.domain.model.Rol.MEDICO_TERAPISTA ||
                 createdBy.getRol() == com.mycompany.piedrazul.domain.model.Rol.AGENDADOR ||
                 createdBy.equals(originalAppointment.getPatient()))) {
                appointment.setCreatedBy(createdBy);
            } else {
                throw new IllegalArgumentException("Usuario sin permisos para reprogramar");
            }
        }
    }
}