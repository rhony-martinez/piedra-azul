package com.mycompany.piedrazul.domain.builder;

import com.mycompany.piedrazul.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de ManualAppointmentBuilder")
class ManualAppointmentBuilderTest {

    private ManualAppointmentBuilder builder;
    private Paciente paciente;
    private Medico medico;
    private Usuario agendador;
    private LocalDateTime fechaFutura;
    private LocalDateTime fechaPasada;

    @BeforeEach
    void setUp() {
        builder = new ManualAppointmentBuilder();
        
        paciente = new Paciente();
        paciente.setId(1);
        paciente.setPrimerNombre("Juan");
        paciente.setPrimerApellido("Pérez");
        
        medico = new Medico();
        medico.setId(1);
        medico.setPrimerNombre("María");
        medico.setPrimerApellido("Gómez");
        
        agendador = new Usuario();
        agendador.setId(1);
        agendador.setUsername("agendador1");
        agendador.setRol(Rol.AGENDADOR);
        
        fechaFutura = LocalDateTime.now().plusDays(2);
        fechaPasada = LocalDateTime.now().minusDays(1);
    }

    @Test
    @DisplayName("Debe crear una nueva cita correctamente")
    void testCrearNueva() {
        builder.crearNueva();
        Appointment appointment = builder.getResult();
        
        assertNotNull(appointment);
        assertNotNull(appointment.getCreadoEn());
        assertEquals(AppointmentStatus.PROGRAMADA, appointment.getEstado());
    }

    @Test
    @DisplayName("Debe construir datos del paciente válidos")
    void testBuildPacienteValido() {
        builder.crearNueva();
        builder.buildPaciente(paciente);
        
        assertEquals(paciente, builder.getResult().getPaciente());
    }

    @Test
    @DisplayName("Debe lanzar excepción con paciente nulo")
    void testBuildPacienteNull() {
        builder.crearNueva();
        
        assertThrows(IllegalArgumentException.class, () -> {
            builder.buildPaciente(null);
        });
    }

    @Test
    @DisplayName("Debe construir datos del médico válidos")
    void testBuildMedicoValido() {
        builder.crearNueva();
        builder.buildMedico(medico);
        
        assertEquals(medico, builder.getResult().getMedico());
    }

    @Test
    @DisplayName("Debe lanzar excepción con médico nulo")
    void testBuildMedicoNull() {
        builder.crearNueva();
        
        assertThrows(IllegalArgumentException.class, () -> {
            builder.buildMedico(null);
        });
    }

    @Test
    @DisplayName("Debe construir fecha válida futura")
    void testBuildFechaHoraValida() {
        builder.crearNueva();
        builder.buildFechaHora(fechaFutura);
        
        assertEquals(fechaFutura, builder.getResult().getFechaHora());
    }

    @Test
    @DisplayName("Debe lanzar excepción con fecha pasada")
    void testBuildFechaHoraPasada() {
        builder.crearNueva();
        
        assertThrows(IllegalArgumentException.class, () -> {
            builder.buildFechaHora(fechaPasada);
        });
    }

    @Test
    @DisplayName("Debe construir creador válido")
    void testBuildCreadoPorValido() {
        builder.crearNueva();
        builder.buildCreadoPor(agendador);
        
        assertEquals(agendador, builder.getResult().getCreadoPor());
    }

    @Test
    @DisplayName("Debe lanzar excepción con creador nulo")
    void testBuildCreadoPorNull() {
        builder.crearNueva();
        
        assertThrows(IllegalArgumentException.class, () -> {
            builder.buildCreadoPor(null);
        });
    }

    @Test
    @DisplayName("Debe construir observación opcional")
    void testBuildObservacion() {
        builder.crearNueva();
        builder.buildObservacion("Nota de prueba");
        
        assertEquals("Nota de prueba", builder.getResult().getObservacion());
    }
}