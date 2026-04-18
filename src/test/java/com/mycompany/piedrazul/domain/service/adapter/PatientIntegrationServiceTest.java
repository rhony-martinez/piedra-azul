package com.mycompany.piedrazul.domain.service.adapter;

import com.mycompany.piedrazul.domain.model.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de PatientIntegrationService")
class PatientIntegrationServiceTest {

    @Mock
    private PatientDataProvider mockProvider;

    private PatientIntegrationService integrationService;

    @BeforeEach
    void setUp() {
        integrationService = new PatientIntegrationService(mockProvider);
    }

    @Test
    @DisplayName("Debe obtener paciente externo correctamente")
    void testObtenerPacienteExterno() {
        Paciente pacienteEsperado = new Paciente();
        pacienteEsperado.setPrimerNombre("Jose");
        pacienteEsperado.setPrimerApellido("Lopez");

        when(mockProvider.getPaciente()).thenReturn(pacienteEsperado);

        Paciente resultado = integrationService.obtenerPacienteExterno();

        assertNotNull(resultado);
        assertEquals("Jose", resultado.getPrimerNombre());
        assertEquals("Lopez", resultado.getPrimerApellido());
        verify(mockProvider, times(1)).getPaciente();
    }

    @Test
    @DisplayName("Debe lanzar excepción si el provider retorna null")
    void testObtenerPacienteExternoNull() {
        when(mockProvider.getPaciente()).thenReturn(null);

        Paciente resultado = integrationService.obtenerPacienteExterno();

        assertNull(resultado);
    }
}