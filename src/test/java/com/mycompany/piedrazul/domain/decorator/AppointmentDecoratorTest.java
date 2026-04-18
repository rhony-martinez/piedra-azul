package com.mycompany.piedrazul.domain.decorator;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.Paciente;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentDecoratorTest {

    private Appointment citaNormal;
    private PriorityAppointment citaPrioritaria;

    @BeforeEach
    void setUp() {
        // Crear una cita de prueba
        citaNormal = new Appointment();
        citaNormal.setId(100);
        citaNormal.setObservacion("Dolor de cabeza persistente");

        // Crear médico de prueba
        Medico medico = new Medico();
        medico.setPrimerNombre("Dr. Juan");
        medico.setPrimerApellido("Pérez");
        citaNormal.setMedico(medico);

        // Crear paciente de prueba
        Paciente paciente = new Paciente();
        paciente.setPrimerNombre("Carlos");
        paciente.setPrimerApellido("López");
        citaNormal.setPaciente(paciente);

        // USAR STATE, NO ENUM
        citaNormal.confirmar();

        // Aplicar Decorator
        citaPrioritaria = new PriorityAppointment(citaNormal);
    }

    @Test
    @DisplayName("1. El decorador debe agregar [PRIORIDAD ALTA] a la descripción")
    void testDecoratorAgregaPrioridad() {
        String descripcionNormal = citaNormal.getDescription();
        String descripcionDecorada = citaPrioritaria.getDescription();

        System.out.println("Descripción normal: " + descripcionNormal);
        System.out.println("Descripción decorada: " + descripcionDecorada);

        assertTrue(descripcionDecorada.contains("[PRIORIDAD ALTA]"));
        assertTrue(descripcionDecorada.contains("⭐"));
        assertFalse(descripcionNormal.contains("[PRIORIDAD ALTA]"));
    }

    @Test
    @DisplayName("2. El decorador no modifica la cita original")
    void testDecoratorNoModificaOriginal() {
        String descripcionOriginalAntes = citaNormal.getDescription();
        String descripcionDecorada = citaPrioritaria.getDescription();

        // La cita original no debe cambiar
        String descripcionOriginalDespues = citaNormal.getDescription();

        assertEquals(descripcionOriginalAntes, descripcionOriginalDespues);
        assertNotEquals(descripcionOriginalAntes, descripcionDecorada);
    }

    @Test
    @DisplayName("3. El decorador mantiene todos los datos de la cita")
    void testDecoratorMantieneDatos() {
        assertEquals(citaNormal.getId(), citaPrioritaria.getId());
        assertEquals(citaNormal.getEstado(), citaPrioritaria.getEstado());
        assertEquals(citaNormal.getObservacion(), citaPrioritaria.getObservacion());
        assertEquals(citaNormal.getPaciente().getPrimerNombre(),
                citaPrioritaria.getPaciente().getPrimerNombre());
        assertEquals(citaNormal.getMedico().getPrimerNombre(),
                citaPrioritaria.getMedico().getPrimerNombre());
    }

    @Test
    @DisplayName("4. El toString también debe mostrar la prioridad")
    void testDecoratorToString() {
        String toStringNormal = citaNormal.toString();
        String toStringDecorado = citaPrioritaria.toString();

        System.out.println("toString normal: " + toStringNormal);
        System.out.println("toString decorado: " + toStringDecorado);

        assertTrue(toStringDecorado.contains("PRIORIDAD ALTA"));
        assertFalse(toStringNormal.contains("PRIORIDAD ALTA"));
    }

    @Test
    @DisplayName("5. Se pueden decorar múltiples veces (Decorator anidado)")
    void testDecoratorMultiple() {
        PriorityAppointment citaPrioritaria2 = new PriorityAppointment(citaPrioritaria);
        String descripcion = citaPrioritaria2.getDescription();

        System.out.println("Decoración anidada: " + descripcion);

        // Debe contener el marcador de prioridad
        assertTrue(descripcion.contains("[PRIORIDAD ALTA]"));
    }

    @Test
    @DisplayName("6. El decorador respeta el flujo de estados correctamente")
    void testDecoratorConState() {

        // Arrange
        Appointment cita = new Appointment();
        cita.setId(200);
        cita.setObservacion("Consulta general");

        Medico medico = new Medico();
        medico.setPrimerNombre("Dr. Juan");
        medico.setPrimerApellido("Pérez");
        cita.setMedico(medico);

        Paciente paciente = new Paciente();
        paciente.setPrimerNombre("Carlos");
        paciente.setPrimerApellido("López");
        cita.setPaciente(paciente);

        // IMPORTANTE: simular flujo MANUAL → inicia PROGRAMADA
        assertEquals(AppointmentStatus.PROGRAMADA, cita.getEstado());

        // Act
        cita.confirmar(); // ✔ transición válida
        PriorityAppointment citaDecorada = new PriorityAppointment(cita);

        // Assert
        assertEquals(AppointmentStatus.CONFIRMADA, citaDecorada.getEstado());

        // Siguiente transición válida
        citaDecorada.atender();
        assertEquals(AppointmentStatus.ATENDIDA, citaDecorada.getEstado());

        // Decorator sigue funcionando
        assertTrue(citaDecorada.getDescription().contains("[PRIORIDAD ALTA]"));
    }

    @Test
    @DisplayName("7. No debe permitir confirmar una cita ya confirmada")
    void shouldNotConfirmTwice() {
        Appointment cita = new Appointment();
        cita.confirmar();

        assertThrows(IllegalStateException.class, cita::confirmar);
    }

    @Test
    @DisplayName("8. Decorator debe delegar comportamiento al objeto base")
    void shouldDelegateStateTransitions() {
        Appointment cita = new Appointment();
        PriorityAppointment decorada = new PriorityAppointment(cita);

        decorada.confirmar();

        assertEquals(AppointmentStatus.CONFIRMADA, cita.getEstado());
    }
}