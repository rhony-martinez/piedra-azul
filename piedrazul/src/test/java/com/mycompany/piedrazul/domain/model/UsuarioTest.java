package com.mycompany.piedrazul.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de la entidad Usuario")
class UsuarioTest {

    @Test
    @DisplayName("Constructor debe inicializar correctamente")
    void constructorDebeInicializar() {
        Usuario usuario = new Usuario(
            "jperez", 
            "hash123", 
            "Juan Pérez", 
            Rol.ADMINISTRADOR
        );
        
        assertAll("Propiedades del usuario",
            () -> assertEquals("jperez", usuario.getUsername()),
            () -> assertEquals("hash123", usuario.getPasswordHash()),
            () -> assertEquals("Juan Pérez", usuario.getNombreCompleto()),
            () -> assertEquals(Rol.ADMINISTRADOR, usuario.getRol()),
            () -> assertTrue(usuario.isActivo()),
            () -> assertEquals(0, usuario.getIntentosFallidos())
        );
    }
    
    @Test
    @DisplayName("Setters y getters deben funcionar correctamente")
    void settersYGetters() {
        Usuario usuario = new Usuario();
        
        usuario.setId(1);
        usuario.setUsername("usuario");
        usuario.setPasswordHash("hash");
        usuario.setNombreCompleto("Nombre Completo");
        usuario.setRol(Rol.MEDICO_TERAPISTA);
        usuario.setActivo(false);
        usuario.setIntentosFallidos(3);
        
        assertAll("Propiedades modificadas",
            () -> assertEquals(1, usuario.getId()),
            () -> assertEquals("usuario", usuario.getUsername()),
            () -> assertEquals("hash", usuario.getPasswordHash()),
            () -> assertEquals("Nombre Completo", usuario.getNombreCompleto()),
            () -> assertEquals(Rol.MEDICO_TERAPISTA, usuario.getRol()),
            () -> assertFalse(usuario.isActivo()),
            () -> assertEquals(3, usuario.getIntentosFallidos())
        );
    }
    
    @Test
    @DisplayName("Debe permitir todos los roles del enum")
    void debePermitirTodosLosRoles() {
        Usuario usuario = new Usuario();
        
        usuario.setRol(Rol.ADMINISTRADOR);
        assertEquals(Rol.ADMINISTRADOR, usuario.getRol());
        
        usuario.setRol(Rol.MEDICO_TERAPISTA);
        assertEquals(Rol.MEDICO_TERAPISTA, usuario.getRol());
        
        usuario.setRol(Rol.PACIENTE);
        assertEquals(Rol.PACIENTE, usuario.getRol());
        
        usuario.setRol(Rol.AGENDADOR);
        assertEquals(Rol.AGENDADOR, usuario.getRol());
    }
}
