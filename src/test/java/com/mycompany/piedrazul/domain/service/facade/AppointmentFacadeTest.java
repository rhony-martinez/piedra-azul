package com.mycompany.piedrazul.domain.service.facade;

import com.mycompany.piedrazul.domain.model.*;
import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;
import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
import com.mycompany.piedrazul.domain.service.AppointmentService;
import com.mycompany.piedrazul.domain.service.NotificationService;
import com.mycompany.piedrazul.domain.service.scheduler.AppointmentScheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de AppointmentFacade")
class AppointmentFacadeTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private IAppointmentRepository appointmentRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private AppointmentScheduler manualScheduler;

    @Mock
    private AppointmentScheduler selfServiceScheduler;

    private AppointmentFacade appointmentFacade;
    private Paciente paciente;
    private Medico medico;
    private Usuario usuarioCreador;
    private LocalDateTime fechaHora;

    @BeforeEach
    void setUp() {
        appointmentFacade = new AppointmentFacade(
                appointmentService,
                appointmentRepository,
                notificationService,
                usuarioRepository,
                manualScheduler,
                selfServiceScheduler);

        paciente = new Paciente();
        paciente.setId(1);
        paciente.setPrimerNombre("Juan");
        paciente.setPrimerApellido("Pérez");

        medico = new Medico();
        medico.setId(1);
        medico.setPrimerNombre("María");
        medico.setPrimerApellido("Gómez");

        usuarioCreador = new Usuario();
        usuarioCreador.setId(1);
        usuarioCreador.setUsername("agendador1");
        usuarioCreador.setRol(Rol.AGENDADOR);

        fechaHora = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0);
    }

    @Test
    @DisplayName("Debe crear cita manual exitosamente a través de la fachada")
    void testCrearCitaManualExitoso() {
        when(usuarioRepository.findByPersonaId(anyInt())).thenReturn(usuarioCreador);

        Appointment citaMock = new Appointment();
        citaMock.setId(1);
        citaMock.setPaciente(paciente);
        citaMock.setMedico(medico);
        citaMock.setFechaHora(fechaHora);
        citaMock.setCreadoPor(usuarioCreador);
        citaMock.setObservacion("Observación de prueba");

        when(appointmentService.crearCita(any(Appointment.class))).thenReturn(citaMock);

        Appointment resultado = appointmentFacade.crearCitaManual(
                paciente, medico, fechaHora, usuarioCreador, "Observación de prueba");

        assertNotNull(resultado);
        assertEquals(paciente, resultado.getPaciente());
        assertEquals(medico, resultado.getMedico());
        assertEquals(fechaHora, resultado.getFechaHora());
        assertEquals(usuarioCreador, resultado.getCreadoPor());

        verify(notificationService, times(1)).notifyUser(eq(usuarioCreador), anyString());
    }

    @Test
    @DisplayName("Debe crear cita manual sin notificar si no se encuentra usuario")
    void testCrearCitaManualSinNotificacion() {

        // Arrange
        when(usuarioRepository.findByPersonaId(anyInt())).thenReturn(null);

        Appointment citaMock = new Appointment();
        citaMock.setId(1);
        citaMock.setPaciente(paciente); // ✔ IMPORTANTE
        citaMock.setMedico(medico); // ✔ IMPORTANTE
        citaMock.setFechaHora(fechaHora);
        citaMock.setCreadoPor(usuarioCreador);
        citaMock.setObservacion("Observación");

        when(appointmentService.crearCita(any(Appointment.class))).thenReturn(citaMock);

        // Act
        Appointment resultado = appointmentFacade.crearCitaManual(
                paciente, medico, fechaHora, usuarioCreador, null);

        // Assert
        assertNotNull(resultado);

        verify(notificationService, never()).notifyUser(any(), anyString());
    }

    @Test
    @DisplayName("Debe construir mensaje de notificación correctamente")
    void testConstruirMensaje() {
        when(usuarioRepository.findByPersonaId(anyInt())).thenReturn(usuarioCreador);

        Appointment citaMock = new Appointment();
        citaMock.setId(1);
        citaMock.setPaciente(paciente);
        citaMock.setMedico(medico);
        citaMock.setFechaHora(fechaHora);
        citaMock.setCreadoPor(usuarioCreador);

        when(appointmentService.crearCita(any(Appointment.class))).thenReturn(citaMock);

        appointmentFacade.crearCitaManual(paciente, medico, fechaHora, usuarioCreador, null);

        verify(notificationService, times(1)).notifyUser(eq(usuarioCreador), argThat(mensaje -> mensaje != null &&
                mensaje.contains(fechaHora.toLocalDate().toString()) &&
                mensaje.contains(fechaHora.toLocalTime().toString())));
    }

    @Test
    @DisplayName("Flujo de agendamiento autónomo")
    void shouldUseSelfServiceScheduler() {
        appointmentFacade.crearCitaAutonoma(paciente, medico, fechaHora, usuarioCreador, null);

        verify(selfServiceScheduler).schedule(any());
    }

}