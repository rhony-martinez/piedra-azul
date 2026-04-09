package com.mycompany.piedrazul.domain.builder;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.Paciente;
import com.mycompany.piedrazul.domain.model.Usuario;
import java.time.LocalDateTime;

public class AppointmentDirector {

    private AppointmentBuilder builder;

    public void setBuilder(AppointmentBuilder builder) {
        this.builder = builder;
    }

    public Appointment buildManualAppointment(
            Paciente paciente,
            Medico medico,
            LocalDateTime fechaHora,
            Usuario creadoPor,
            String observacion) {

        builder.crearNueva();

        builder.buildPaciente(paciente);
        builder.buildMedico(medico);
        builder.buildFechaHora(fechaHora);
        builder.buildCreadoPor(creadoPor);
        builder.buildObservacion(observacion);

        return builder.getResult();
    }

    // Construir cita por autoservicio (paciente)
    public Appointment buildSelfServiceAppointment(
            Paciente paciente,
            Medico medico,
            LocalDateTime fechaHora,
            Usuario usuario,
            String observacion) {

        builder.crearNueva();
        builder.buildPaciente(paciente);
        builder.buildMedico(medico);
        builder.buildFechaHora(fechaHora);
        builder.buildCreadoPor(usuario);
        builder.buildObservacion(observacion);

        return builder.getResult();
    }

    // Construir cita reprogramada
    /*
     * public Appointment buildRescheduledAppointment(Appointment
     * originalAppointment,
     * Usuario professional,
     * LocalDateTime newDateTime,
     * Usuario rescheduledBy) {
     * // Nota: El builder ya debe estar configurado con la cita original
     * appointmentBuilder.crearNuevaAppointment();
     * appointmentBuilder.buildProfessionalData(professional);
     * appointmentBuilder.buildDateTime(newDateTime);
     * appointmentBuilder.buildCreatedBy(rescheduledBy);
     * 
     * return appointmentBuilder.getAppointment();
     * }
     */

    // Construir cita reprogramada (manteniendo el mismo profesional)
    /*
     * public Appointment buildRescheduledAppointmentSameProfessional(Appointment
     * originalAppointment,
     * LocalDateTime newDateTime,
     * Usuario rescheduledBy) {
     * return buildRescheduledAppointment(originalAppointment,
     * originalAppointment.getMedico(),
     * newDateTime,
     * rescheduledBy);
     * }
     */
}