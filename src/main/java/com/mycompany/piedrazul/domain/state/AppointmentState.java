package com.mycompany.piedrazul.domain.state;

import com.mycompany.piedrazul.domain.model.Appointment;

public interface AppointmentState {
    void programar(Appointment cita);
    void confirmar(Appointment cita);
    void atender(Appointment cita);
    void noAsistir(Appointment cita);
    void reagendar(Appointment cita);
    void cancelar(Appointment cita);
    
    String getNombreEstado();
}