package com.mycompany.piedrazul.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de la clase Paciente")
class PacienteTest {

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(1);
        paciente.setPrimerNombre("Carlos");
        paciente.setPrimerApellido("Martínez");
        paciente.setGenero("HOMBRE");
        paciente.setFechaNacimiento(LocalDate.of(1995, 8, 10));
        paciente.setTelefono("3201234567");
        paciente.setDni(12345678);
        paciente.setCorreo("carlos.martinez@email.com");
    }

    @Test
    @DisplayName("Debe crear un paciente con todos sus atributos")
    void testCrearPacienteCompleto() {
        assertEquals(1, paciente.getId());
        assertEquals("Carlos", paciente.getPrimerNombre());
        assertEquals("Martínez", paciente.getPrimerApellido());
        assertEquals("HOMBRE", paciente.getGenero());
        assertEquals(LocalDate.of(1995, 8, 10), paciente.getFechaNacimiento());
        assertEquals("3201234567", paciente.getTelefono());
        assertEquals(12345678, paciente.getDni());
    }

    @Test
    @DisplayName("toString debe mostrar nombre completo y DNI")
    void testToString() {
        String resultado = paciente.toString();
        assertTrue(resultado.contains("Carlos"));
        assertTrue(resultado.contains("Martínez"));
        assertTrue(resultado.contains(String.valueOf(12345678)));
    }
}