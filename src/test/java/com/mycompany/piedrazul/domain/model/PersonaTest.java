package com.mycompany.piedrazul.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de la clase Persona")
class PersonaTest {

    private Persona persona;

    @BeforeEach
    void setUp() {
        persona = new Persona();
        persona.setId(1);
        persona.setPrimerNombre("Juan");
        persona.setSegundoNombre("Carlos");
        persona.setPrimerApellido("Pérez");
        persona.setSegundoApellido("Gómez");
        persona.setGenero("HOMBRE");
        persona.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        persona.setTelefono("3001234567");
        persona.setDni(12345678);
        persona.setCorreo("juan.perez@email.com");
    }

    @Test
    @DisplayName("Debe crear una persona con todos sus atributos")
    void testCrearPersonaCompleta() {
        assertEquals(1, persona.getId());
        assertEquals("Juan", persona.getPrimerNombre());
        assertEquals("Carlos", persona.getSegundoNombre());
        assertEquals("Pérez", persona.getPrimerApellido());
        assertEquals("Gómez", persona.getSegundoApellido());
        assertEquals("HOMBRE", persona.getGenero());
        assertEquals(LocalDate.of(1990, 5, 15), persona.getFechaNacimiento());
        assertEquals("3001234567", persona.getTelefono());
        assertEquals(12345678, persona.getDni());
        assertEquals("juan.perez@email.com", persona.getCorreo());
    }

    @Test
    @DisplayName("Debe poder modificar los atributos de una persona")
    void testModificarPersona() {
        persona.setPrimerNombre("Pedro");
        persona.setPrimerApellido("López");
        persona.setDni(87654321);

        assertEquals("Pedro", persona.getPrimerNombre());
        assertEquals("López", persona.getPrimerApellido());
        assertEquals(87654321, persona.getDni());
    }

    @Test
    @DisplayName("Debe permitir nombres opcionales vacíos")
    void testNombresOpcionales() {
        persona.setSegundoNombre("");
        persona.setSegundoApellido("");

        assertEquals("", persona.getSegundoNombre());
        assertEquals("", persona.getSegundoApellido());
    }
}