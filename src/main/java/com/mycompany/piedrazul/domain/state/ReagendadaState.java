package com.mycompany.piedrazul.domain.state;

import com.mycompany.piedrazul.domain.model.Appointment;

public class ReagendadaState implements AppointmentState {
    
    @Override
    public void programar(Appointment cita) {
        throw new IllegalStateException("La cita ya fue reagendada");
    }
    
    @Override
    public void confirmar(Appointment cita) {
        cita.setAppointmentState(new ConfirmadaState());
    }
    
    @Override
    public void atender(Appointment cita) {
        throw new IllegalStateException("Debe confirmar la nueva fecha antes de atender");
    }
    
    @Override
    public void noAsistir(Appointment cita) {
        throw new IllegalStateException("Debe confirmar la nueva fecha antes de marcar no asistencia");
    }
    
  @Override
public void reagendar(Appointment cita) {
    throw new IllegalStateException("La cita ya fue reagendada, solo se permite una vez");
}
    
    @Override
    public void cancelar(Appointment cita) {
        cita.setAppointmentState(new CanceladaState());
    }
    
    @Override
    public String getNombreEstado() {
        return "REAGENDADA";
    }
}