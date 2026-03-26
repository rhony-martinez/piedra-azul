package com.mycompany.piedrazul.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de la clase Appointment")
class AppointmentTest {

    private Appointment appointment;
    private Paciente paciente;
    private Medico medico;
    private Usuario creadoPor;
    private LocalDateTime fechaHora;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(1);
        paciente.setPrimerNombre("Juan");
        paciente.setPrimerApellido("Pérez");

        medico = new Medico();
        medico.setId(1);
        medico.setPrimerNombre("María");
        medico.setPrimerApellido("Gómez");
        medico.setTipoProfesional("MEDICO");

        creadoPor = new Usuario();
        creadoPor.setId(1);
        creadoPor.setUsername("agendador1");

        fechaHora = LocalDateTime.now().plusDays(3).withHour(10).withMinute(0);

        appointment = new Appointment();
        appointment.setId(1);
        appointment.setPaciente(paciente);
        appointment.setMedico(medico);
        appointment.setFechaHora(fechaHora);
        appointment.setEstado(AppointmentStatus.PROGRAMADA);
        appointment.setObservacion("Dolor de cabeza");
        appointment.setCreadoPor(creadoPor);
        appointment.setCreadoEn(LocalDateTime.now());
    }

    @Test
    @DisplayName("Debe crear una cita con todos sus atributos")
    void testCrearCitaCompleta() {
        assertEquals(1, appointment.getId());
        assertEquals(paciente, appointment.getPaciente());
        assertEquals(medico, appointment.getMedico());
        assertEquals(fechaHora, appointment.getFechaHora());
        assertEquals(AppointmentStatus.PROGRAMADA, appointment.getEstado());
        assertEquals("Dolor de cabeza", appointment.getObservacion());
        assertEquals(creadoPor, appointment.getCreadoPor());
        assertNotNull(appointment.getCreadoEn());
    }

    @Test
    @DisplayName("Debe poder cambiar el estado de la cita")
    void testCambiarEstado() {
        appointment.setEstado(AppointmentStatus.ATENDIDA);
        assertEquals(AppointmentStatus.ATENDIDA, appointment.getEstado());

        appointment.setEstado(AppointmentStatus.CANCELADA);
        assertEquals(AppointmentStatus.CANCELADA, appointment.getEstado());

        appointment.setEstado(AppointmentStatus.NO_ASISTIDA);
        assertEquals(AppointmentStatus.NO_ASISTIDA, appointment.getEstado());
    }

    @Test
    @DisplayName("toString debe mostrar información relevante")
    void testToString() {
        String resultado = appointment.toString();
        assertTrue(resultado.contains("Juan"));
        assertTrue(resultado.contains("María"));
        assertTrue(resultado.contains("PROGRAMADA"));
    }
}