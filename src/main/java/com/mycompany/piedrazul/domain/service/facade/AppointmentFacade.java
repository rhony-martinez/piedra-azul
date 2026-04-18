/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.service.facade;

import com.mycompany.piedrazul.domain.builder.AppointmentDirector;
import com.mycompany.piedrazul.domain.builder.ManualAppointmentBuilder;
import com.mycompany.piedrazul.domain.builder.SelfServiceAppointmentBuilder;
import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.Paciente;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;
import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
import com.mycompany.piedrazul.domain.service.AppointmentService;
import com.mycompany.piedrazul.domain.service.NotificationService;
import com.mycompany.piedrazul.domain.service.scheduler.AppointmentScheduler;

import java.time.LocalDateTime;

/**
 *
 * @author asus
 */
public class AppointmentFacade {

    private final AppointmentService appointmentService;
    private final NotificationService notificationService;
    private final IAppointmentRepository appointmentRepository;
    private final IUsuarioRepository usuarioRepository;
    private final AppointmentScheduler manualScheduler;
    private final AppointmentScheduler selfServiceScheduler;

    public AppointmentFacade(
            AppointmentService appointmentService,
            IAppointmentRepository appointmentRepository,
            NotificationService notificationService,
            IUsuarioRepository usuarioRepository,
            AppointmentScheduler manualScheduler,
            AppointmentScheduler selfServiceScheduler) {

        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
        this.notificationService = notificationService;
        this.usuarioRepository = usuarioRepository;
        this.manualScheduler = manualScheduler;
        this.selfServiceScheduler = selfServiceScheduler;
    }

    public Appointment crearCitaManual(
            Paciente paciente,
            Medico medico,
            LocalDateTime fechaHora,
            Usuario usuarioCreador,
            String observacion) {

        // 1. Builder
        Appointment cita = construirCitaManual(
                paciente,
                medico,
                fechaHora,
                usuarioCreador,
                observacion);

        // 2. Scheduler (Template Method)
        manualScheduler.schedule(cita);

        // 3. Persistencia
        Appointment guardada = appointmentService.crearCita(cita);

        // 4. Notificación
        notificarPaciente(guardada);

        return guardada;
    }

    // =========================
    // MÉTODOS PRIVADOS
    // =========================

    private Appointment construirCitaManual(
            Paciente paciente,
            Medico medico,
            LocalDateTime fechaHora,
            Usuario usuarioCreador,
            String observacion) {

        AppointmentDirector director = new AppointmentDirector();
        ManualAppointmentBuilder builder = new ManualAppointmentBuilder();

        director.setBuilder(builder);

        return director.buildManualAppointment(
                paciente,
                medico,
                fechaHora,
                usuarioCreador,
                observacion);
    }

    private void notificarPaciente(Appointment cita) {

        Usuario usuarioPaciente = usuarioRepository
                .findByPersonaId(cita.getPaciente().getId());

        if (usuarioPaciente == null)
            return;

        String mensaje = construirMensaje(cita);

        notificationService.notifyUser(usuarioPaciente, mensaje);
    }

    private String construirMensaje(Appointment cita) {
        return "Cita confirmada:\n"
                + " " + cita.getFechaHora().toLocalDate()
                + "\n " + cita.getFechaHora().toLocalTime();
    }

    public Appointment crearCitaAutonoma(
            Paciente paciente,
            Medico medico,
            LocalDateTime fechaHora,
            Usuario usuario,
            String observacion) {

        // 1. Builder
        AppointmentDirector director = new AppointmentDirector();
        SelfServiceAppointmentBuilder builder = new SelfServiceAppointmentBuilder();
        director.setBuilder(builder);

        Appointment cita = director.buildSelfServiceAppointment(
                paciente,
                medico,
                fechaHora,
                usuario,
                observacion);

        // 2. Scheduler 
        selfServiceScheduler.schedule(cita);

        // 3. Persistencia
        Appointment guardada = appointmentService.crearCita(cita);

        // 4. Notificación
        notificationService.notifyUser(usuario,
                "Cita agendada correctamente:\n" +
                        cita.getFechaHora().toLocalDate() + " " +
                        cita.getFechaHora().toLocalTime());

        return guardada;
    }
}
