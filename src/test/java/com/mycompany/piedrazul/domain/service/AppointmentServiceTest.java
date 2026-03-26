package com.mycompany.piedrazul.domain.service;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de AppointmentService")
class AppointmentServiceTest {

    @Mock
    private IAppointmentRepository appointmentRepository;
    
    private AppointmentService appointmentService;
    private Appointment appointment;
    private Paciente paciente;
    private Medico medico;
    private Usuario creadoPor;
    private LocalDateTime fechaHora;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService(appointmentRepository);
        
        paciente = new Paciente();
        paciente.setId(1);
        paciente.setPrimerNombre("Juan");
        paciente.setPrimerApellido("Pérez");
        
        medico = new Medico();
        medico.setId(1);
        medico.setPrimerNombre("María");
        medico.setPrimerApellido("Gómez");
        
        creadoPor = new Usuario();
        creadoPor.setId(1);
        creadoPor.setUsername("agendador1");
        
        fechaHora = LocalDateTime.now().plusDays(3);
        
        appointment = new Appointment();
        appointment.setPaciente(paciente);
        appointment.setMedico(medico);
        appointment.setFechaHora(fechaHora);
        appointment.setEstado(AppointmentStatus.PROGRAMADA);
        appointment.setCreadoPor(creadoPor);
    }

    @Test
    @DisplayName("Debe crear una cita exitosamente")
    void testCrearCitaExitoso() {
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        
        Appointment resultado = appointmentService.crearCita(appointment);
        
        assertNotNull(resultado);
        assertEquals(paciente, resultado.getPaciente());
        assertEquals(medico, resultado.getMedico());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    @DisplayName("Debe lanzar excepción si la cita es nula")
    void testCrearCitaNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.crearCita(null);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si el paciente es inválido")
    void testCrearCitaPacienteInvalido() {
        appointment.setPaciente(null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.crearCita(appointment);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si el médico es inválido")
    void testCrearCitaMedicoInvalido() {
        appointment.setMedico(null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.crearCita(appointment);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si la fecha es nula")
    void testCrearCitaFechaNula() {
        appointment.setFechaHora(null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.crearCita(appointment);
        });
    }

    @Test
    @DisplayName("Debe cancelar una cita exitosamente")
    void testCancelarCita() {
        when(appointmentRepository.cancel(1)).thenReturn(true);
        
        boolean resultado = appointmentService.cancelarCita(1);
        
        assertTrue(resultado);
        verify(appointmentRepository).cancel(1);
    }

    @Test
    @DisplayName("Debe fallar al cancelar una cita inexistente")
    void testCancelarCitaInexistente() {
        when(appointmentRepository.cancel(999)).thenReturn(false);
        
        boolean resultado = appointmentService.cancelarCita(999);
        
        assertFalse(resultado);
        verify(appointmentRepository).cancel(999);
    }

    @Test
@DisplayName("Debe crear cita manual usando el builder")
void testCrearCitaManual() {
    // Configurar el mock para que devuelva la cita con observación
    Appointment citaConObservacion = new Appointment();
    citaConObservacion.setPaciente(paciente);
    citaConObservacion.setMedico(medico);
    citaConObservacion.setFechaHora(fechaHora);
    citaConObservacion.setCreadoPor(creadoPor);
    citaConObservacion.setObservacion("Observación");
    citaConObservacion.setEstado(AppointmentStatus.PROGRAMADA);
    citaConObservacion.setCreadoEn(LocalDateTime.now());
    
    when(appointmentRepository.save(any(Appointment.class))).thenReturn(citaConObservacion);
    
    Appointment resultado = appointmentService.crearCitaManual(
        paciente, medico, fechaHora, creadoPor, "Observación"
    );
    
    assertNotNull(resultado);
    assertEquals(paciente, resultado.getPaciente());
    assertEquals(medico, resultado.getMedico());
    assertEquals(fechaHora, resultado.getFechaHora());
    assertEquals(creadoPor, resultado.getCreadoPor());
    assertEquals("Observación", resultado.getObservacion());
}
}