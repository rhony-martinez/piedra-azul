package com.mycompany.piedrazul.domain.state;

import com.mycompany.piedrazul.domain.model.Appointment;

public class CanceladaState implements AppointmentState {
    
    @Override
    public void programar(Appointment cita) {
        throw new IllegalStateException("No se puede programar una cita cancelada");
    }
    
    @Override
    public void confirmar(Appointment cita) {
        throw new IllegalStateException("No se puede confirmar una cita cancelada");
    }
    
    @Override
    public void atender(Appointment cita) {
        throw new IllegalStateException("No se puede atender una cita cancelada");
    }
    
    @Override
    public void noAsistir(Appointment cita) {
        throw new IllegalStateException("No se puede marcar como no asistida una cita cancelada");
    }
    
    @Override
    public void reagendar(Appointment cita) {
        throw new IllegalStateException("No se puede reagendar una cita cancelada");
    }
    
    @Override
    public void cancelar(Appointment cita) {
        throw new IllegalStateException("La cita ya está cancelada");
    }
    
    @Override
    public String getNombreEstado() {
        return "CANCELADA";
    }
}