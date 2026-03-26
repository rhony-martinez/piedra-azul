package com.mycompany.piedrazul.domain.builder;

import com.mycompany.piedrazul.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de AppointmentDirector")
class AppointmentDirectorTest {

    private AppointmentDirector director;
    private Paciente paciente;
    private Medico medico;
    private Usuario agendador;
    private LocalDateTime fechaFutura;

    @BeforeEach
    void setUp() {
        director = new AppointmentDirector();
        
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
    }

    @Test
    @DisplayName("Debe construir cita manual correctamente")
    void testBuildManualAppointment() {
        ManualAppointmentBuilder builder = new ManualAppointmentBuilder();
        director.setBuilder(builder);
        
        Appointment cita = director.buildManualAppointment(
            paciente, medico, fechaFutura, agendador, "Nota de prueba"
        );
        
        assertNotNull(cita);
        assertEquals(paciente, cita.getPaciente());
        assertEquals(medico, cita.getMedico());
        assertEquals(fechaFutura, cita.getFechaHora());
        assertEquals(agendador, cita.getCreadoPor());
        assertEquals("Nota de prueba", cita.getObservacion());
        assertEquals(AppointmentStatus.PROGRAMADA, cita.getEstado());
        assertNotNull(cita.getCreadoEn());
    }

    @Test
    @DisplayName("Debe construir cita manual sin observación")
    void testBuildManualAppointmentSinObservacion() {
        ManualAppointmentBuilder builder = new ManualAppointmentBuilder();
        director.setBuilder(builder);
        
        Appointment cita = director.buildManualAppointment(
            paciente, medico, fechaFutura, agendador, null
        );
        
        assertNotNull(cita);
        assertNull(cita.getObservacion());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el builder no está configurado")
    void testBuilderNoConfigurado() {
        assertThrows(NullPointerException.class, () -> {
            director.buildManualAppointment(
                paciente, medico, fechaFutura, agendador, null
            );
        });
    }
}