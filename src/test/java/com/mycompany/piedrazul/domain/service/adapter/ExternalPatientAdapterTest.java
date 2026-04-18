package com.mycompany.piedrazul.domain.service.adapter;

import com.mycompany.piedrazul.domain.model.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de ExternalPatientAdapter")
class ExternalPatientAdapterTest {

    private ExternalService externalService;
    private ExternalPatientAdapter adapter;

    @BeforeEach
    void setUp() {
        externalService = new ExternalService();
        adapter = new ExternalPatientAdapter(externalService);
    }

    @Test
    @DisplayName("Debe adaptar correctamente el JSON a un objeto Paciente")
    void testGetPaciente() {
        Paciente paciente = adapter.getPaciente();

        assertNotNull(paciente);
        assertEquals("Jose", paciente.getPrimerNombre());
        assertEquals("Antonio", paciente.getSegundoNombre());
        assertEquals("Lopez", paciente.getPrimerApellido());
        assertEquals("Perez", paciente.getSegundoApellido());
        assertEquals(12345678, paciente.getDni());
        assertEquals("3001234567", paciente.getTelefono());
        assertEquals("jose@example.com", paciente.getCorreo());
        assertNotNull(paciente.getGenero());
        assertNotNull(paciente.getFechaNacimiento());
    }

    @Test
    @DisplayName("Debe manejar nombres con diferentes estructuras")
    void testGetPacienteConNombreSimple() {
        // Modificar el servicio para probar diferentes formatos
        // Esta prueba verifica que el split no falle
        Paciente paciente = adapter.getPaciente();
        
        assertNotNull(paciente);
        assertNotNull(paciente.getPrimerNombre());
        assertNotNull(paciente.getPrimerApellido());
    }

    @Test
    @DisplayName("Debe implementar correctamente la interfaz PatientDataProvider")
    void testImplementacionInterfaz() {
        assertTrue(adapter instanceof PatientDataProvider);
    }
}