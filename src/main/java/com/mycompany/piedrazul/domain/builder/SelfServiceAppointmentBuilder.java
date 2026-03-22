package com.mycompany.piedrazul.domain.builder;

import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.model.Usuario;
import java.time.LocalDateTime;

public class SelfServiceAppointmentBuilder extends AppointmentBuilder {
    
    @Override
    public void crearNuevaAppointment() {
        super.crearNuevaAppointment();
        // Configuración específica para autoservicio
        appointment.setStatus(AppointmentStatus.SCHEDULED); // Pendiente de confirmación
    }
    
    @Override
    public void buildPatientData(Usuario patient) {
        if (appointment != null) {
            // En autoservicio, el paciente es quien crea la cita
            if (patient != null && patient.getRol() == com.mycompany.piedrazul.domain.model.Rol.PACIENTE) {
                appointment.setPatient(patient);
            } else {
                throw new IllegalArgumentException("Solo pacientes pueden crear citas por autoservicio");
            }
        }
    }
    
    @Override
    public void buildProfessionalData(Usuario professional) {
        if (appointment != null) {
            if (professional != null && professional.getRol() == com.mycompany.piedrazul.domain.model.Rol.MEDICO_TERAPISTA) {
                appointment.setProfessional(professional);
            } else {
                throw new IllegalArgumentException("Debe seleccionar un médico válido");
            }
        }
    }
    
    @Override
    public void buildDateTime(LocalDateTime dateTime) {
        if (appointment != null) {
            // En autoservicio, validar horarios disponibles (simplificado)
            if (dateTime != null && dateTime.isAfter(LocalDateTime.now())) {
                // Validar que sea en horario laboral (8am - 6pm)
                int hour = dateTime.getHour();
                if (hour >= 8 && hour <= 18) {
                    appointment.setDateTime(dateTime);
                } else {
                    throw new IllegalArgumentException("Las citas solo pueden agendarse en horario laboral (8am - 6pm)");
                }
            } else {
                throw new IllegalArgumentException("La fecha de la cita debe ser futura");
            }
        }
    }
    
    @Override
    public void buildAppointmentType(String type) {
        if (appointment != null) {
            // Tipos permitidos para autoservicio
            if (type != null && (type.equals("CONSULTA") || type.equals("CONTROL"))) {
                appointment.setAppointmentType(type);
            } else {
                throw new IllegalArgumentException("Tipo de cita no disponible para autoservicio");
            }
        }
    }
    
    @Override
    public void buildReason(String reason) {
        if (appointment != null) {
            // Motivo opcional en autoservicio
            appointment.setReason(reason != null ? reason : "Sin motivo especificado");
        }
    }
    
    @Override
    public void buildCreatedBy(Usuario createdBy) {
        if (appointment != null) {
            // En autoservicio, el creador es el mismo paciente
            if (createdBy != null && createdBy.getRol() == com.mycompany.piedrazul.domain.model.Rol.PACIENTE) {
                appointment.setCreatedBy(createdBy);
            } else {
                appointment.setCreatedBy(appointment.getPatient());
            }
        }
    }
}