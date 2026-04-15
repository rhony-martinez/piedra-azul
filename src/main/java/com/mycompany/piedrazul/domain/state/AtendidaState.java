package com.mycompany.piedrazul.domain.state;

import com.mycompany.piedrazul.domain.model.Appointment;

public class AtendidaState implements AppointmentState {
    
    @Override
    public void programar(Appointment cita) {
        throw new IllegalStateException("No se puede programar una cita ya atendida");
    }
    
    @Override
    public void confirmar(Appointment cita) {
        throw new IllegalStateException("No se puede confirmar una cita ya atendida");
    }
    
    @Override
    public void atender(Appointment cita) {
        throw new IllegalStateException("La cita ya fue atendida");
    }
    
    @Override
    public void noAsistir(Appointment cita) {
        throw new IllegalStateException("No se puede cambiar a no asistida después de atendida");
    }
    
    @Override
    public void reagendar(Appointment cita) {
        throw new IllegalStateException("No se puede reagendar una cita ya atendida");
    }
    
    @Override
    public void cancelar(Appointment cita) {
        throw new IllegalStateException("No se puede cancelar una cita ya atendida");
    }
    
    @Override
    public String getNombreEstado() {
        return "ATENDIDA";
    }
}