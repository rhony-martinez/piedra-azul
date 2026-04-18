package com.mycompany.piedrazul.domain.decorator;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.Paciente;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.state.AppointmentState;
import java.time.LocalDateTime;

public abstract class AppointmentDecorator extends Appointment {
    protected Appointment wrappedAppointment;

    public AppointmentDecorator(Appointment appointment) {
        this.wrappedAppointment = appointment;
    }

    // Delegar todos los métodos importantes
    
    @Override
    public int getId() { return wrappedAppointment.getId(); }
    @Override
    public void setId(int id) { wrappedAppointment.setId(id); }
    
    @Override
    public Paciente getPaciente() { return wrappedAppointment.getPaciente(); }
    @Override
    public void setPaciente(Paciente paciente) { wrappedAppointment.setPaciente(paciente); }
    
    @Override
    public Medico getMedico() { return wrappedAppointment.getMedico(); }
    @Override
    public void setMedico(Medico medico) { wrappedAppointment.setMedico(medico); }
    
    @Override
    public Usuario getCreadoPor() { return wrappedAppointment.getCreadoPor(); }
    @Override
    public void setCreadoPor(Usuario creadoPor) { wrappedAppointment.setCreadoPor(creadoPor); }
    
    @Override
    public LocalDateTime getFechaHora() { return wrappedAppointment.getFechaHora(); }
    @Override
    public void setFechaHora(LocalDateTime fechaHora) { wrappedAppointment.setFechaHora(fechaHora); }
    
    @Override
    public AppointmentStatus getEstado() { return wrappedAppointment.getEstado(); }
    // @Override
    // public void setEstado(AppointmentStatus estado) { wrappedAppointment.setEstado(estado); }
    
    @Override
    public String getObservacion() { return wrappedAppointment.getObservacion(); }
    @Override
    public void setObservacion(String observacion) { wrappedAppointment.setObservacion(observacion); }
    
    @Override
    public LocalDateTime getCreadoEn() { return wrappedAppointment.getCreadoEn(); }
    @Override
    public void setCreadoEn(LocalDateTime creadoEn) { wrappedAppointment.setCreadoEn(creadoEn); }
    
    @Override
    public AppointmentState getAppointmentState() { return wrappedAppointment.getAppointmentState(); }
    @Override
    public void setAppointmentState(AppointmentState state) { wrappedAppointment.setAppointmentState(state); }
    
    @Override
    public void programar() { wrappedAppointment.programar(); }
    @Override
    public void confirmar() { wrappedAppointment.confirmar(); }
    @Override
    public void atender() { wrappedAppointment.atender(); }
    @Override
    public void noAsistir() { wrappedAppointment.noAsistir(); }
    @Override
    public void reagendar() { wrappedAppointment.reagendar(); }
    @Override
    public void cancelar() { wrappedAppointment.cancelar(); }
    
    @Override
    public String getDescription() {
        return wrappedAppointment.getDescription();
    }
    
    @Override
    public String toString() {
        return wrappedAppointment.toString();
    }
}