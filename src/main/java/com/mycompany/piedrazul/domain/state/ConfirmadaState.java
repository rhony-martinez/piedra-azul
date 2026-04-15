package com.mycompany.piedrazul.domain.state;

import com.mycompany.piedrazul.domain.model.Appointment;

public class ConfirmadaState implements AppointmentState {
    
    @Override
    public void programar(Appointment cita) {
        throw new IllegalStateException("La cita ya está confirmada, no puede reprogramarse sin reagendar");
    }
    
    @Override
    public void confirmar(Appointment cita) {
        throw new IllegalStateException("La cita ya está confirmada");
    }
    
    @Override
    public void atender(Appointment cita) {
        cita.setAppointmentState(new AtendidaState());
    }
    
    @Override
    public void noAsistir(Appointment cita) {
        cita.setAppointmentState(new NoAsistidaState());
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
        return "CONFIRMADA";
    }
}