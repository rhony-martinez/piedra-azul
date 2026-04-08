/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.service.scheduler;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;

/**
 * Template Method para el agendamiento de citas.
 * Define el flujo general del algoritmo.
 * 
 * @author asus
 */
/**
 * Template Method para agendamiento de citas.
 */
public abstract class AppointmentScheduler {

    protected final IAppointmentRepository repository;

    public AppointmentScheduler(IAppointmentRepository repository) {
        this.repository = repository;
    }

    public final Appointment schedule(Appointment appointment) {
        validateUser(appointment);
        checkAvailability(appointment);
        assignProfessional(appointment);
        confirmAppointment(appointment);

        return save(appointment);
    }

    protected abstract void validateUser(Appointment appointment);

    protected abstract void checkAvailability(Appointment appointment);

    protected abstract void assignProfessional(Appointment appointment);

    protected abstract void confirmAppointment(Appointment appointment);

    protected Appointment save(Appointment appointment) {
        return repository.save(appointment);
    }
}
