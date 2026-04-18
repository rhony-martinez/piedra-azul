package com.mycompany.piedrazul.domain.model;

import com.mycompany.piedrazul.domain.state.AppointmentState;
import com.mycompany.piedrazul.domain.state.AtendidaState;
import com.mycompany.piedrazul.domain.state.CanceladaState;
import com.mycompany.piedrazul.domain.state.ConfirmadaState;
import com.mycompany.piedrazul.domain.state.NoAsistidaState;
import com.mycompany.piedrazul.domain.state.ProgramadaState;
import com.mycompany.piedrazul.domain.state.ReagendadaState;

import java.time.LocalDateTime;

public class Appointment {

    private int id;
    private Paciente paciente;
    private Medico medico;
    private Usuario creadoPor;
    private LocalDateTime fechaHora;
    private String observacion;
    private LocalDateTime creadoEn;

    // pattern design State
    private AppointmentState appointmentState;

    public Appointment() {
        this.appointmentState = new ProgramadaState();
    }

    // =========================
    // GETTERS / SETTERS
    // =========================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Usuario getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Usuario creadoPor) {
        this.creadoPor = creadoPor;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public AppointmentState getAppointmentState() {
        return appointmentState;
    }

    public void setAppointmentState(AppointmentState state) {
        if (state == null) {
            throw new IllegalArgumentException("El estado no puede ser null");
        }
        this.appointmentState = state;
    }

    // =========================
    // DERIVACIÓN A ENUM (PERSISTENCIA)
    // =========================

    public AppointmentStatus getEstado() {
        return mapStateToStatus();
    }

    private AppointmentStatus mapStateToStatus() {
        if (appointmentState instanceof ProgramadaState) return AppointmentStatus.PROGRAMADA;
        if (appointmentState instanceof ConfirmadaState) return AppointmentStatus.CONFIRMADA;
        if (appointmentState instanceof CanceladaState) return AppointmentStatus.CANCELADA;
        if (appointmentState instanceof AtendidaState) return AppointmentStatus.ATENDIDA;
        if (appointmentState instanceof NoAsistidaState) return AppointmentStatus.NO_ASISTIDA;
        if (appointmentState instanceof ReagendadaState) return AppointmentStatus.REAGENDADA;

        throw new IllegalStateException("Estado desconocido: " + appointmentState.getClass());
    }

    // =========================
    // STATE TRANSITIONS
    // =========================

    public void programar() {
        appointmentState.programar(this);
    }

    public void confirmar() {
        appointmentState.confirmar(this);
    }

    public void atender() {
        appointmentState.atender(this);
    }

    public void noAsistir() {
        appointmentState.noAsistir(this);
    }

    public void reagendar() {
        appointmentState.reagendar(this);
    }

    public void cancelar() {
        appointmentState.cancelar(this);
    }

    // =========================
    // DECORATOR SUPPORT
    // =========================

    public String getDescription() {

        String medicoNombre = (medico != null)
                ? medico.getPrimerNombre() + " " + medico.getPrimerApellido()
                : "médico";

        String pacienteNombre = (paciente != null)
                ? paciente.getPrimerNombre() + " " + paciente.getPrimerApellido()
                : "paciente";

        String fechaStr = (fechaHora != null)
                ? fechaHora.toString()
                : "fecha no definida";

        return "Cita #" + id +
                " | Paciente: " + pacienteNombre +
                " | Médico: " + medicoNombre +
                " | Fecha: " + fechaStr +
                " | Estado: " + getEstado() +
                (observacion != null && !observacion.isEmpty()
                        ? " | Obs: " + observacion
                        : "");
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", paciente=" + (paciente != null
                        ? paciente.getPrimerNombre() + " " + paciente.getPrimerApellido()
                        : "null") +
                ", medico=" + (medico != null
                        ? medico.getPrimerNombre() + " " + medico.getPrimerApellido()
                        : "null") +
                ", fechaHora=" + fechaHora +
                ", estado=" + getEstado() +
                ", observacion='" + observacion + '\'' +
                '}';
    }
}