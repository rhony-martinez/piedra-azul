/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.service.facade;

import com.mycompany.piedrazul.domain.builder.AppointmentDirector;
import com.mycompany.piedrazul.domain.builder.ManualAppointmentBuilder;
import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.Paciente;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;
import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
import com.mycompany.piedrazul.domain.service.AppointmentService;
import com.mycompany.piedrazul.domain.service.NotificationService;
import com.mycompany.piedrazul.domain.service.scheduler.ManualAppointmentScheduler;
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
    private ManualAppointmentScheduler scheduler;

    public AppointmentFacade(AppointmentService appointmentService,
            IAppointmentRepository appointmentRepository,
            NotificationService notificationService,
            IUsuarioRepository usuarioRepository) {

        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
        this.notificationService = notificationService;
        this.usuarioRepository = usuarioRepository;

        this.scheduler = new ManualAppointmentScheduler(appointmentRepository);
    }

    public Appointment crearCitaManual(
            Paciente paciente,
            Medico medico,
            LocalDateTime fechaHora,
            Usuario usuarioCreador,
            String observacion) {

        // 1. Builder
        AppointmentDirector director = new AppointmentDirector();
        ManualAppointmentBuilder builder = new ManualAppointmentBuilder();
        director.setBuilder(builder);

        Appointment cita = director.buildManualAppointment(
                paciente,
                medico,
                fechaHora,
                usuarioCreador,
                observacion);

        // 2. Scheduler (Template Method)
        scheduler.schedule(cita);

        // 3. Persistencia delegada a service y repository
        Appointment guardada = appointmentService.crearCita(cita);

        Usuario usuarioPaciente = usuarioRepository
                .findByPersonaId(cita.getPaciente().getId());

        if (usuarioPaciente != null) {
            String mensaje = construirMensaje(guardada);
            notificationService.notifyUser(usuarioPaciente, mensaje);
        }

        return guardada;
    }

    private String construirMensaje(Appointment cita) {
        return "Cita confirmada:\n" +
                " " + cita.getFechaHora().toLocalDate() +
                "\n " + cita.getFechaHora().toLocalTime();
    }
}
