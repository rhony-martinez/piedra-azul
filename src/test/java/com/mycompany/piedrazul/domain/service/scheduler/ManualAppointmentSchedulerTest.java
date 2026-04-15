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
@DisplayName("Pruebas de ManualAppointmentScheduler (Template Method)")
class ManualAppointmentSchedulerTest {

    @Mock
    private IAppointmentRepository appointmentRepository;

    private ManualAppointmentScheduler scheduler;
    private Appointment appointment;
    private Medico medico;
    private Paciente paciente;
    private Usuario creadoPor;

    @BeforeEach
    void setUp() {
        scheduler = new ManualAppointmentScheduler(appointmentRepository);

        medico = new Medico();
        medico.setId(1);
        medico.setPrimerNombre("María");
        medico.setPrimerApellido("Gómez");

        paciente = new Paciente();
        paciente.setId(1);
        paciente.setPrimerNombre("Juan");
        paciente.setPrimerApellido("Pérez");

        creadoPor = new Usuario();
        creadoPor.setId(1);
        creadoPor.setUsername("agendador1");

        appointment = new Appointment();
        appointment.setMedico(medico);
        appointment.setPaciente(paciente);
        appointment.setCreadoPor(creadoPor);
        appointment.setFechaHora(LocalDateTime.now().plusDays(2).withSecond(0).withNano(0));
    }

    @Test
    @DisplayName("Debe agendar cita manual cuando todo está correcto")
    void testScheduleExitoso() {
        when(appointmentRepository.existsByMedicoAndFechaHora(anyInt(), any())).thenReturn(false);
        when(appointmentRepository.existsByPacienteAndFecha(anyInt(), any())).thenReturn(false);

        assertDoesNotThrow(() -> scheduler.schedule(appointment));

        verify(appointmentRepository, times(1)).existsByMedicoAndFechaHora(anyInt(), any());
        verify(appointmentRepository, times(1)).existsByPacienteAndFecha(anyInt(), any());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el usuario creador es nulo")
    void testValidateUserFallaPorCreadorNulo() {
        appointment.setCreadoPor(null);

        assertThrows(IllegalArgumentException.class, () -> scheduler.schedule(appointment));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el médico ya tiene cita en ese horario")
    void testCheckAvailabilityMedicoOcupado() {
        when(appointmentRepository.existsByMedicoAndFechaHora(anyInt(), any())).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> scheduler.schedule(appointment));
        assertEquals("El médico ya tiene una cita en ese horario", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el paciente ya tiene cita en ese horario")
    void testCheckAvailabilityPacienteOcupado() {
        when(appointmentRepository.existsByMedicoAndFechaHora(anyInt(), any())).thenReturn(false);
        when(appointmentRepository.existsByPacienteAndFecha(anyInt(), any())).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> scheduler.schedule(appointment));
        assertEquals("El paciente ya tiene una cita en ese horario", exception.getMessage());
    }

    @Test
    @DisplayName("Debe dejar la cita en estado PROGRAMADA después de confirmar")
    void testConfirmAppointment() {
        when(appointmentRepository.existsByMedicoAndFechaHora(anyInt(), any())).thenReturn(false);
        when(appointmentRepository.existsByPacienteAndFecha(anyInt(), any())).thenReturn(false);

        scheduler.schedule(appointment);

        assertEquals(AppointmentStatus.PROGRAMADA, appointment.getEstado());
    }
}