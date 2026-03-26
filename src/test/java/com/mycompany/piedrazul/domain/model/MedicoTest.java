package com.mycompany.piedrazul.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de la clase Medico")
class MedicoTest {

    private Medico medico;

    @BeforeEach
    void setUp() {
        medico = new Medico();
        medico.setId(1);
        medico.setPrimerNombre("María");
        medico.setSegundoNombre("Isabel");
        medico.setPrimerApellido("Gómez");
        medico.setSegundoApellido("Rodríguez");
        medico.setGenero("MUJER");
        medico.setFechaNacimiento(LocalDate.of(1985, 3, 20));
        medico.setTelefono("3109876543");
        medico.setDni(98765432);
        medico.setCorreo("maria.gomez@email.com");
        medico.setTipoProfesional("MEDICO");
    }

    @Test
    @DisplayName("Debe crear un médico con todos sus atributos")
    void testCrearMedicoCompleto() {
        assertEquals(1, medico.getId());
        assertEquals("María", medico.getPrimerNombre());
        assertEquals("Gómez", medico.getPrimerApellido());
        assertEquals("MEDICO", medico.getTipoProfesional());
    }

    @Test
    @DisplayName("Debe poder cambiar el tipo profesional")
    void testCambiarTipoProfesional() {
        medico.setTipoProfesional("TERAPISTA");
        assertEquals("TERAPISTA", medico.getTipoProfesional());
    }

    @Test
    @DisplayName("toString debe mostrar nombre completo y tipo profesional")
    void testToString() {
        String resultado = medico.toString();
        assertTrue(resultado.contains("María"));
        assertTrue(resultado.contains("Gómez"));
        assertTrue(resultado.contains("MEDICO"));
    }
}
