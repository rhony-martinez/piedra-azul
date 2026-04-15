package com.mycompany.piedrazul.domain.state;

import com.mycompany.piedrazul.domain.model.Appointment;

public class NoAsistidaState implements AppointmentState {
    
    @Override
    public void programar(Appointment cita) {
        throw new IllegalStateException("No se puede programar una cita con no asistencia");
    }
    
    @Override
    public void confirmar(Appointment cita) {
        throw new IllegalStateException("No se puede confirmar una cita con no asistencia");
    }
    
    @Override
    public void atender(Appointment cita) {
        throw new IllegalStateException("No se puede atender una cita con no asistencia");
    }
    
    @Override
    public void noAsistir(Appointment cita) {
        throw new IllegalStateException("La cita ya está marcada como no asistida");
    }
    
    @Override
    public void reagendar(Appointment cita) {
        throw new IllegalStateException("No se puede reagendar una cita con no asistencia");
    }
    
    @Override
    public void cancelar(Appointment cita) {
        throw new IllegalStateException("No se puede cancelar una cita con no asistencia");
    }
    
    @Override
    public String getNombreEstado() {
        return "NO_ASISTIDA";
    }
}