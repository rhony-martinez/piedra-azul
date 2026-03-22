package com.mycompany.piedrazul.domain.builder;

import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.model.Usuario;
import java.time.LocalDateTime;

public class ManualAppointmentBuilder extends AppointmentBuilder {
    
    @Override
    public void buildPatientData(Usuario patient) {
        if (appointment != null) {
            // Validar que el paciente sea un PACIENTE
            if (patient != null && patient.getRol() != null) {
                // Verificar que el rol sea PACIENTE
                appointment.setPatient(patient);
            } else {
                throw new IllegalArgumentException("El paciente debe tener un rol válido");
            }
        }
    }
    
    @Override
    public void buildProfessionalData(Usuario professional) {
        if (appointment != null) {
            // Validar que el profesional sea MEDICO_TERAPISTA
            if (professional != null && professional.getRol() == com.mycompany.piedrazul.domain.model.Rol.MEDICO_TERAPISTA) {
                appointment.setProfessional(professional);
            } else {
                throw new IllegalArgumentException("El profesional debe ser un MÉDICO_TERAPISTA");
            }
        }
    }
    
    @Override
    public void buildDateTime(LocalDateTime dateTime) {
        if (appointment != null) {
            // Validar que la fecha sea futura
            if (dateTime != null && dateTime.isAfter(LocalDateTime.now())) {
                appointment.setDateTime(dateTime);
            } else {
                throw new IllegalArgumentException("La fecha de la cita debe ser futura");
            }
        }
    }
    
    @Override
    public void buildAppointmentType(String type) {
        if (appointment != null) {
            // Validar tipo de cita
            if (type != null && !type.trim().isEmpty()) {
                appointment.setAppointmentType(type);
            } else {
                throw new IllegalArgumentException("El tipo de cita es requerido");
            }
        }
    }
    
    @Override
    public void buildReason(String reason) {
        if (appointment != null) {
            if (reason != null && !reason.trim().isEmpty()) {
                appointment.setReason(reason);
            } else {
                throw new IllegalArgumentException("El motivo de la cita es requerido");
            }
        }
    }
    
    @Override
    public void buildCreatedBy(Usuario createdBy) {
        if (appointment != null) {
            // Validar que quien crea tenga permisos (MEDICO_TERAPISTA o AGENDADOR)
            if (createdBy != null && 
                (createdBy.getRol() == com.mycompany.piedrazul.domain.model.Rol.MEDICO_TERAPISTA ||
                 createdBy.getRol() == com.mycompany.piedrazul.domain.model.Rol.AGENDADOR)) {
                appointment.setCreatedBy(createdBy);
            } else {
                throw new IllegalArgumentException("Solo médicos o agendadores pueden crear citas manualmente");
            }
        }
    }
    
    @Override
    public void buildNotes(String notes) {
        if (appointment != null) {
            appointment.setNotes(notes);
        }
    }
}