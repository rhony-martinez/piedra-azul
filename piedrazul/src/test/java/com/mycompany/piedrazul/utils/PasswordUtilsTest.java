package com.mycompany.piedrazul.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
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
        assertEquals(hash1, hash2, "Mismo password = mismo hash");
        assertNotEquals(password, hash1, "Hash != password original");
    }
    
    @Test
    @DisplayName("Passwords diferentes generan hashes diferentes")
    void hashPasswordDebeSerUnico() {
        String hash1 = PasswordUtils.hashPassword("Admin123!");
        String hash2 = PasswordUtils.hashPassword("Admin1234!");
        
        assertNotEquals(hash1, hash2, "Passwords diferentes = hashes diferentes");
    }
    
    @Test
    @DisplayName("Hash de null lanza excepción")
    void hashPasswordNullLanzaExcepcion() {
        assertThrows(NullPointerException.class, () -> {
            PasswordUtils.hashPassword(null);
        });
    }
    
   @ParameterizedTest
@ValueSource(strings = {
    "",                          // Vacía
    "abc",                        // Muy corta
    "abcdef",                      // Solo minúsculas
    "ABCDEF",                       // Solo mayúsculas
    "123456",                        // Solo números
    "!@#$%^",                         // Solo especiales
    "abc123",                          // Sin mayúscula ni especial
    "ABC123",                           // Sin minúscula ni especial
    "Abcdef",                            // Sin número ni especial
    "Admin123",                           // Sin especial
    "admin123!",                           // Sin mayúscula
    "ADMIN123!",                            // Sin minúscula
    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"        // Muy larga sin requisitos
})
@DisplayName("Contraseñas inválidas son rechazadas")
void validarContrasenaRechazaInvalidas(String password) {
    assertFalse(PasswordUtils.validarContrasena(password));
}
    
    @Test
    @DisplayName("Null es rechazado")
    void validarContrasenaNullEsFalse() {
        assertFalse(PasswordUtils.validarContrasena(null));
    }
    
    @Test
    @DisplayName("Verificar password funciona correctamente")
    void verifyPasswordFunciona() {
        String password = "Admin123!";
        String hash = PasswordUtils.hashPassword(password);
        
        assertTrue(PasswordUtils.verifyPassword(password, hash));
        assertFalse(PasswordUtils.verifyPassword("WrongPass!", hash));
        assertFalse(PasswordUtils.verifyPassword(password, "hashIncorrecto"));
    }
}
