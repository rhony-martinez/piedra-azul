package com.mycompany.piedrazul.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de PasswordUtils")
class PasswordUtilsTest {

    @Test
    @DisplayName("Hash de contraseña debe ser consistente")
    void hashPasswordDebeSerConsistente() {
        String password = "Admin123!";
        String hash1 = PasswordUtils.hashPassword(password);
        String hash2 = PasswordUtils.hashPassword(password);
        
        assertNotNull(hash1);
        assertEquals(hash1, hash2);
        assertNotEquals(password, hash1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Admin123!", "Pass123@", "Test1#Ab", "C0ntr@senia"})
    @DisplayName("Contraseñas válidas deben pasar la validación")
    void validarContrasenaDebeAceptarValidas(String password) {
        assertTrue(PasswordUtils.validarContrasena(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "abc", "abcdef", "123456", "Admin123", "admin123!"})
    @DisplayName("Contraseñas inválidas deben ser rechazadas")
    void validarContrasenaDebeRechazarInvalidas(String password) {
        assertFalse(PasswordUtils.validarContrasena(password));
    }

    @Test
    @DisplayName("Verificar password debe comparar correctamente")
    void verifyPasswordDebeFuncionar() {
        String password = "Admin123!";
        String hash = PasswordUtils.hashPassword(password);
        
        assertTrue(PasswordUtils.verifyPassword(password, hash));
        assertFalse(PasswordUtils.verifyPassword("WrongPass!", hash));
    }
}