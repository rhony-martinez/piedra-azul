package com.mycompany.piedrazul.domain.decorator;

import com.mycompany.piedrazul.domain.model.Appointment;

public class PriorityAppointment extends AppointmentDecorator {

    public PriorityAppointment(Appointment appointment) {
        super(appointment);
    }

    @Override
    public String getDescription() {
        return wrappedAppointment.getDescription() + " ⭐ [PRIORIDAD ALTA]";
    }
    
    @Override
    public String toString() {
        return wrappedAppointment.toString() + " | PRIORIDAD ALTA";
    }
}