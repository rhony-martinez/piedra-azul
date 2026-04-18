package com.mycompany.piedrazul.domain.service.scheduler;

import com.mycompany.piedrazul.domain.model.*;
import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de SelfServiceAppointmentScheduler (Template Method)")
class SelfServiceAppointmentSchedulerTest {

    @Mock
    private IAppointmentRepository appointmentRepository;

    private SelfServiceAppointmentScheduler scheduler;
    private Appointment appointment;
    private Medico medico;
    private Paciente paciente;
    private Usuario pacienteUsuario;

    @BeforeEach
    void setUp() {
        scheduler = new SelfServiceAppointmentScheduler(appointmentRepository);

        medico = new Medico();
        medico.setId(1);
        medico.setPrimerNombre("María");
        medico.setPrimerApellido("Gómez");

        paciente = new Paciente();
        paciente.setId(1);
        paciente.setPrimerNombre("Juan");
        paciente.setPrimerApellido("Pérez");

        pacienteUsuario = new Usuario();
        pacienteUsuario.setId(1);
        pacienteUsuario.setUsername("juanperez");
        pacienteUsuario.setRol(Rol.PACIENTE);

        appointment = new Appointment();
        appointment.setMedico(medico);
        appointment.setPaciente(paciente);
        appointment.setCreadoPor(pacienteUsuario);
        appointment.setFechaHora(LocalDateTime.now().plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0));
    }

    @Test
    @DisplayName("Debe agendar cita por autoservicio cuando todo está correcto")
    void testScheduleExitoso() {
        when(appointmentRepository.existsByPacienteAndFecha(anyInt(), any())).thenReturn(false);
        when(appointmentRepository.existsByMedicoAndFechaHora(anyInt(), any())).thenReturn(false);

        assertDoesNotThrow(() -> scheduler.schedule(appointment));

        verify(appointmentRepository, times(1)).existsByPacienteAndFecha(anyInt(), any());
        verify(appointmentRepository, times(1)).existsByMedicoAndFechaHora(anyInt(), any());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el paciente es nulo")
    void testValidateUserPacienteNulo() {
        appointment.setPaciente(null);

        assertThrows(IllegalArgumentException.class, () -> scheduler.schedule(appointment));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el creadoPor es nulo")
    void testValidateUserCreadoPorNulo() {
        appointment.setCreadoPor(null);

        assertThrows(IllegalArgumentException.class, () -> scheduler.schedule(appointment));
    }

    @Test
    @DisplayName("Debe lanzar excepción si la fecha es pasada")
    void testCheckAvailabilityFechaPasada() {
        appointment.setFechaHora(LocalDateTime.now().minusDays(1));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> scheduler.schedule(appointment));
        assertEquals("No puede agendar en el pasado", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si la fecha supera el límite de 30 días")
    void testCheckAvailabilityFueraDelLimite() {
        appointment.setFechaHora(LocalDateTime.now().plusDays(31));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> scheduler.schedule(appointment));
        assertEquals("No puede agendar una cita más allá del límite permitido", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el paciente ya tiene cita en ese horario")
    void testCheckAvailabilityPacienteOcupado() {
        when(appointmentRepository.existsByPacienteAndFecha(anyInt(), any())).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> scheduler.schedule(appointment));
        assertEquals("Usted ya tiene una cita en este horario", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el médico está ocupado")
    void testCheckAvailabilityMedicoOcupado() {
        when(appointmentRepository.existsByPacienteAndFecha(anyInt(), any())).thenReturn(false);
        when(appointmentRepository.existsByMedicoAndFechaHora(anyInt(), any())).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> scheduler.schedule(appointment));
        assertEquals("Horario no disponible para agendamiento", exception.getMessage());
    }

    @Test
    @DisplayName("Debe dejar la cita en estado CONFIRMADA")
    void testConfirmAppointment() {
        // Arrange
        when(appointmentRepository.existsByPacienteAndFecha(anyInt(), any())).thenReturn(false);
        when(appointmentRepository.existsByMedicoAndFechaHora(anyInt(), any())).thenReturn(false);

        // Act
        scheduler.schedule(appointment);

        // Assert
        assertEquals(AppointmentStatus.CONFIRMADA, appointment.getEstado());
    }

    @Test
    @DisplayName("Test de transición de estado")
    void shouldBeAbleToAttendAfterSelfServiceScheduling() {
        scheduler.schedule(appointment);

        appointment.atender();

        assertEquals(AppointmentStatus.ATENDIDA, appointment.getEstado());
    }
}