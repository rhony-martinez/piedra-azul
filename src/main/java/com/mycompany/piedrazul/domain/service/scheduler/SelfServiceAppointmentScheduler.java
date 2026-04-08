/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.service.scheduler;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;

/**
 *
 * @author asus
 */
public class SelfServiceAppointmentScheduler extends AppointmentScheduler {

    public SelfServiceAppointmentScheduler(IAppointmentRepository repository) {
        super(repository);
    }

    @Override
    protected void validateUser(Appointment appointment) {
        System.out.println("Validando paciente (autogestión)...");
    }

    @Override
    protected void checkAvailability(Appointment appointment) {
        System.out.println("Buscando mejor horario disponible...");
    }

    @Override
    protected void assignProfessional(Appointment appointment) {
        System.out.println("Asignando automáticamente el mejor médico...");
    }

    @Override
    protected void confirmAppointment(Appointment appointment) {
        appointment.setEstado(AppointmentStatus.PROGRAMADA);
        System.out.println("Cita autogestionada confirmada");
    }
}
