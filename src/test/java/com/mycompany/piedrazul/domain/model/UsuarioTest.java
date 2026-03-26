package com.mycompany.piedrazul.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de la clase Usuario")
class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("jperez");
        usuario.setPasswordHash("hash123456");
        usuario.setRol(Rol.PACIENTE);
        usuario.setActivo(true);
        usuario.setPersonaId(100);
        usuario.setIntentosFallidos(0);
    }

    @Test
    @DisplayName("Debe crear un usuario con todos sus atributos")
    void testCrearUsuarioCompleto() {
        assertEquals(1, usuario.getId());
        assertEquals("jperez", usuario.getUsername());
        assertEquals("hash123456", usuario.getPasswordHash());
        assertEquals(Rol.PACIENTE, usuario.getRol());
        assertTrue(usuario.isActivo());
        assertEquals(100, usuario.getPersonaId());
        assertEquals(0, usuario.getIntentosFallidos());
    }

    @Test
    @DisplayName("Constructor con parámetros debe funcionar correctamente")
    void testConstructorConParametros() {
        Usuario nuevo = new Usuario("jdoe", "hash456", Rol.MEDICO_TERAPISTA, 200);
        
        assertEquals("jdoe", nuevo.getUsername());
        assertEquals("hash456", nuevo.getPasswordHash());
        assertEquals(Rol.MEDICO_TERAPISTA, nuevo.getRol());
        assertTrue(nuevo.isActivo());
        assertEquals(200, nuevo.getPersonaId());
        assertEquals(0, nuevo.getIntentosFallidos());
    }

    @Test
    @DisplayName("Debe poder desactivar un usuario")
    void testDesactivarUsuario() {
        usuario.setActivo(false);
        assertFalse(usuario.isActivo());
    }

    @Test
    @DisplayName("Debe poder registrar intentos fallidos")
    void testIntentosFallidos() {
        usuario.setIntentosFallidos(3);
        assertEquals(3, usuario.getIntentosFallidos());
        
        usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
        assertEquals(4, usuario.getIntentosFallidos());
    }

    @Test
    @DisplayName("toString debe devolver el username")
    void testToString() {
        assertEquals("jperez", usuario.toString());
    }
}