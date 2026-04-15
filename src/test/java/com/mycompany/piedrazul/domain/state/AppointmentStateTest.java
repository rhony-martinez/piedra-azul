package com.mycompany.piedrazul.domain.state;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentStateTest {
    
    private Appointment cita;
    
    @BeforeEach
    void setUp() {
        cita = new Appointment();
        System.out.println("=== INICIO PRUEBA ===");
    }
    
    @AfterEach
    void tearDown() {
        System.out.println("=== FIN PRUEBA ===\n");
    }
    
    @Test
    @DisplayName("1. Una cita nueva debe estar PROGRAMADA")
    void testCitaNuevaProgramada() {
        assertEquals(AppointmentStatus.PROGRAMADA, cita.getEstado());
        assertTrue(cita.getAppointmentState() instanceof ProgramadaState);
    }
    
    @Test
    @DisplayName("2. PROGRAMADA → CONFIRMADA")
    void testConfirmarCita() {
        cita.confirmar();
        assertEquals(AppointmentStatus.CONFIRMADA, cita.getEstado());
        assertTrue(cita.getAppointmentState() instanceof ConfirmadaState);
    }
    
    @Test
    @DisplayName("3. CONFIRMADA → ATENDIDA")
    void testAtenderCita() {
        cita.confirmar(); // Primero confirmar
        assertEquals(AppointmentStatus.CONFIRMADA, cita.getEstado());
        
        cita.atender();
        assertEquals(AppointmentStatus.ATENDIDA, cita.getEstado());
        assertTrue(cita.getAppointmentState() instanceof AtendidaState);
    }
    
    @Test
    @DisplayName("4. CONFIRMADA → NO ASISTIDA")
    void testNoAsistirCita() {
        cita.confirmar();
        cita.noAsistir();
        assertEquals(AppointmentStatus.NO_ASISTIDA, cita.getEstado());
        assertTrue(cita.getAppointmentState() instanceof NoAsistidaState);
    }
    
    @Test
    @DisplayName("5. CONFIRMADA → REAGENDADA")
    void testReagendarCita() {
        cita.confirmar();
        cita.reagendar();
        assertEquals(AppointmentStatus.REAGENDADA, cita.getEstado());
        assertTrue(cita.getAppointmentState() instanceof ReagendadaState);
    }
    
    @Test
    @DisplayName("6. PROGRAMADA → REAGENDADA (directo)")
    void testReagendarCitaDirecto() {
        cita.reagendar();
        assertEquals(AppointmentStatus.REAGENDADA, cita.getEstado());
        assertTrue(cita.getAppointmentState() instanceof ReagendadaState);
    }
    
    @Test
    @DisplayName("7. PROGRAMADA → CANCELADA")
    void testCancelarCitaProgramada() {
        cita.cancelar();
        assertEquals(AppointmentStatus.CANCELADA, cita.getEstado());
        assertTrue(cita.getAppointmentState() instanceof CanceladaState);
    }
    
    @Test
    @DisplayName("8. CONFIRMADA → CANCELADA")
    void testCancelarCitaConfirmada() {
        cita.confirmar();
        cita.cancelar();
        assertEquals(AppointmentStatus.CANCELADA, cita.getEstado());
        assertTrue(cita.getAppointmentState() instanceof CanceladaState);
    }
    
    @Test
    @DisplayName("9. REAGENDADA → CONFIRMADA")
    void testConfirmarCitaReagendada() {
        cita.reagendar();
        assertEquals(AppointmentStatus.REAGENDADA, cita.getEstado());
        
        cita.confirmar();
        assertEquals(AppointmentStatus.CONFIRMADA, cita.getEstado());
        assertTrue(cita.getAppointmentState() instanceof ConfirmadaState);
    }
    
    @Test
    @DisplayName("10. No se puede atender una cita PROGRAMADA")
    void testNoSePuedeAtenderProgramada() {
        assertThrows(IllegalStateException.class, () -> {
            cita.atender();
        });
    }
    
    @Test
    @DisplayName("11. No se puede atender una cita ya atendida")
    void testNoSePuedeAtenderAtendida() {
        cita.confirmar();
        cita.atender();
        
        assertThrows(IllegalStateException.class, () -> {
            cita.atender();
        });
    }
    
    @Test
    @DisplayName("12. No se puede reagendar una cita ya atendida")
    void testNoSePuedeReagendarAtendida() {
        cita.confirmar();
        cita.atender();
        
        assertThrows(IllegalStateException.class, () -> {
            cita.reagendar();
        });
    }
    
    @Test
    @DisplayName("13. No se puede reagendar una cita cancelada")
    void testNoSePuedeReagendarCancelada() {
        cita.cancelar();
        
        assertThrows(IllegalStateException.class, () -> {
            cita.reagendar();
        });
    }
    
    @Test
    @DisplayName("14. No se puede reagendar una cita ya reagendada (solo una vez)")
    void testNoSePuedeReagendarDosVeces() {
        cita.reagendar();
        
        assertThrows(IllegalStateException.class, () -> {
            cita.reagendar();
        });
    }
    
    @Test
    @DisplayName("15. Flujo completo: PROGRAMADA → CONFIRMADA → ATENDIDA")
    void testFlujoCompleto() {
        assertEquals(AppointmentStatus.PROGRAMADA, cita.getEstado());
        
        cita.confirmar();
        assertEquals(AppointmentStatus.CONFIRMADA, cita.getEstado());
        
        cita.atender();
        assertEquals(AppointmentStatus.ATENDIDA, cita.getEstado());
    }
    
    @Test
    @DisplayName("16. Flujo con reagendamiento: PROGRAMADA → REAGENDADA → CONFIRMADA → ATENDIDA")
    void testFlujoConReagendamiento() {
        cita.reagendar();
        assertEquals(AppointmentStatus.REAGENDADA, cita.getEstado());
        
        cita.confirmar();
        assertEquals(AppointmentStatus.CONFIRMADA, cita.getEstado());
        
        cita.atender();
        assertEquals(AppointmentStatus.ATENDIDA, cita.getEstado());
    }
}