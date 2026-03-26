package com.mycompany.piedrazul.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas del enum AppointmentStatus")
class AppointmentStatusTest {

    @Test
    @DisplayName("Debe contener todos los estados esperados")
    void testEstadosExistentes() {
        AppointmentStatus[] estados = AppointmentStatus.values();
        assertEquals(4, estados.length);
        
        assertTrue(containsStatus(estados, AppointmentStatus.PROGRAMADA));
        assertTrue(containsStatus(estados, AppointmentStatus.CANCELADA));
        assertTrue(containsStatus(estados, AppointmentStatus.ATENDIDA));
        assertTrue(containsStatus(estados, AppointmentStatus.NO_ASISTIDA));
    }

    private boolean containsStatus(AppointmentStatus[] estados, AppointmentStatus status) {
        for (AppointmentStatus s : estados) {
            if (s == status) return true;
        }
        return false;
    }

    @Test
    @DisplayName("Cada estado debe tener su nombre correcto")
    void testNombreEstados() {
        assertEquals("PROGRAMADA", AppointmentStatus.PROGRAMADA.name());
        assertEquals("CANCELADA", AppointmentStatus.CANCELADA.name());
        assertEquals("ATENDIDA", AppointmentStatus.ATENDIDA.name());
        assertEquals("NO_ASISTIDA", AppointmentStatus.NO_ASISTIDA.name());
    }
}