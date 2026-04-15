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
        System.out.println("=== ConfirmadaState.atender() ===");
        System.out.println("Cita ID: " + cita.getId());
        System.out.println("Estado actual: " + cita.getEstado());
        System.out.println("AppointmentState antes: " + cita.getAppointmentState().getClass().getSimpleName());
        
        cita.setAppointmentState(new AtendidaState());
        
        System.out.println("Nuevo estado: " + cita.getEstado());
        System.out.println("AppointmentState después: " + cita.getAppointmentState().getClass().getSimpleName());
        System.out.println("=== Fin ConfirmadaState.atender() ===");
    }
    
    @Override
    public void noAsistir(Appointment cita) {
        System.out.println("=== ConfirmadaState.noAsistir() ===");
        cita.setAppointmentState(new NoAsistidaState());
    }
    
    @Override
    public void reagendar(Appointment cita) {
        System.out.println("=== ConfirmadaState.reagendar() ===");
        cita.setAppointmentState(new ReagendadaState());
    }
    
    @Override
    public void cancelar(Appointment cita) {
        System.out.println("=== ConfirmadaState.cancelar() ===");
        cita.setAppointmentState(new CanceladaState());
    }
    
    @Override
    public String getNombreEstado() {
        return "CONFIRMADA";
    }
}