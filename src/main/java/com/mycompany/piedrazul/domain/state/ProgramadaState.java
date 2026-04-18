package com.mycompany.piedrazul.domain.state;

import com.mycompany.piedrazul.domain.model.Appointment;

public class ProgramadaState implements AppointmentState {
    
    @Override
    public void programar(Appointment cita) {
        throw new IllegalStateException("La cita ya está programada");
    }
    
    @Override
    public void confirmar(Appointment cita) {
        cita.setAppointmentState(new ConfirmadaState());
    }
    
    @Override
    public void atender(Appointment cita) {
        throw new IllegalStateException("Debe confirmar la cita antes de atender");
    }
    
    @Override
    public void noAsistir(Appointment cita) {
        throw new IllegalStateException("Debe confirmar la cita antes de marcar como no asistida");
    }
    
    @Override
    public void reagendar(Appointment cita) {
        cita.setAppointmentState(new ReagendadaState());
    }
    
    @Override
    public void cancelar(Appointment cita) {
        cita.setAppointmentState(new CanceladaState());
    }
    
    @Override
    public String getNombreEstado() {
        return "PROGRAMADA";
    }
}