package com.mycompany.piedrazul.domain.service;

import com.mycompany.piedrazul.domain.model.Rol;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
import com.mycompany.piedrazul.utils.PasswordUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("Pruebas de UsuarioService")
class UsuarioServiceTest {

    private IUsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;
    
    @BeforeEach
    void setUp() {
        // Crear mock del repositorio
        usuarioRepository = mock(IUsuarioRepository.class);
        usuarioService = new UsuarioService(usuarioRepository);
    }
    
    @Test
    @DisplayName("Autenticar retorna usuario cuando credenciales correctas")
    void autenticarExitoso() {
        // Preparar
        String username = "jperez";
        String password = "Admin123!";
        String passwordHash = PasswordUtils.hashPassword(password);
        
        Usuario usuarioMock = new Usuario(username, passwordHash, "Juan Pérez", Rol.ADMINISTRADOR);
        usuarioMock.setActivo(true);
        
        when(usuarioRepository.findByUsername(username)).thenReturn(usuarioMock);
        when(usuarioRepository.authenticate(username, passwordHash)).thenReturn(usuarioMock);
        
        // Ejecutar
        Usuario resultado = usuarioService.autenticar(username, password);
        
        // Verificar
        assertAll(
            () -> assertNotNull(resultado),
            () -> assertEquals(username, resultado.getUsername()),
            () -> verify(usuarioRepository).resetearIntentosFallidos(username),
            () -> verify(usuarioRepository, never()).registrarIntentoFallido(anyString())
        );
    }
    
    @Test
    @DisplayName("Autenticar retorna null cuando usuario no existe")
    void autenticarUsuarioNoExiste() {
        String username = "noexiste";
        String password = "Admin123!";
        
        when(usuarioRepository.findByUsername(username)).thenReturn(null);
        
        Usuario resultado = usuarioService.autenticar(username, password);
        
        assertNull(resultado);
        verify(usuarioRepository, never()).authenticate(anyString(), anyString());
    }
    
    @Test
    @DisplayName("Autenticar lanza excepción cuando usuario inactivo")
    void autenticarUsuarioInactivo() {
        String username = "jinactivo";
        String password = "Admin123!";
        String passwordHash = PasswordUtils.hashPassword(password);
        
        Usuario usuarioMock = new Usuario(username, passwordHash, "Juan Inactivo", Rol.ADMINISTRADOR);
        usuarioMock.setActivo(false);
        
        when(usuarioRepository.findByUsername(username)).thenReturn(usuarioMock);
        
        IllegalStateException exception = assertThrows(
            IllegalStateException.class, 
            () -> usuarioService.autenticar(username, password)
        );
        
        assertEquals("Usuario inactivo", exception.getMessage());
    }
    
    @Test
    @DisplayName("Autenticar registra intento fallido con password incorrecto")
    void autenticarPasswordIncorrecto() {
        String username = "jperez";
        String passwordCorrecto = "Admin123!";
        String passwordIncorrecto = "WrongPass!";
        String passwordHash = PasswordUtils.hashPassword(passwordCorrecto);
        
        Usuario usuarioMock = new Usuario(username, passwordHash, "Juan Pérez", Rol.ADMINISTRADOR);
        usuarioMock.setActivo(true);
        
        when(usuarioRepository.findByUsername(username)).thenReturn(usuarioMock);
        when(usuarioRepository.authenticate(eq(username), anyString())).thenReturn(null);
        
        Usuario resultado = usuarioService.autenticar(username, passwordIncorrecto);
        
        assertNull(resultado);
        verify(usuarioRepository).registrarIntentoFallido(username);
    }
    
   @ParameterizedTest
@NullSource
@ValueSource(strings = {"", "   "})
@DisplayName("Autenticar retorna null con username inválido")
void autenticarUsernameInvalido(String username) {
    Usuario resultado = usuarioService.autenticar(username, "Admin123!");
    
    assertNull(resultado);
    verify(usuarioRepository, never()).findByUsername(anyString());
}
    
    @Test
    @DisplayName("Crear usuario exitosamente")
    void crearUsuarioExitoso() {
        String username = "nuevo_user";
        String password = "Admin123!";
        String nombre = "Nuevo Usuario";
        Rol rol = Rol.PACIENTE;
        
        when(usuarioRepository.usernameExists(username)).thenReturn(false);
        when(usuarioRepository.create(any(Usuario.class))).thenReturn(true);
        
        boolean resultado = usuarioService.crearUsuario(username, password, nombre, rol);
        
        assertTrue(resultado);
        verify(usuarioRepository).create(argThat(usuario -> 
            usuario.getUsername().equals(username) &&
            usuario.getNombreCompleto().equals(nombre) &&
            usuario.getRol().equals(rol) &&
            usuario.isActivo()
        ));
    }
    
    @Test
    @DisplayName("Crear usuario falla si username ya existe")
    void crearUsuarioUsernameExistente() {
        String username = "existente";
        String password = "Admin123!";
        String nombre = "Usuario Existente";
        Rol rol = Rol.PACIENTE;
        
        when(usuarioRepository.usernameExists(username)).thenReturn(true);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> usuarioService.crearUsuario(username, password, nombre, rol)
        );
        
        assertEquals("El nombre de usuario ya existe", exception.getMessage());
        verify(usuarioRepository, never()).create(any(Usuario.class));
    }
    
    @ParameterizedTest
    @EnumSource(Rol.class)
    @DisplayName("Crear usuario acepta todos los roles")
    void crearUsuarioAceptaTodosLosRoles(Rol rol) {
        String username = "usuario";
        String password = "Admin123!";
        String nombre = "Nombre";
        
        when(usuarioRepository.usernameExists(username)).thenReturn(false);
        when(usuarioRepository.create(any(Usuario.class))).thenReturn(true);
        
        boolean resultado = usuarioService.crearUsuario(username, password, nombre, rol);
        
        assertTrue(resultado);
        verify(usuarioRepository).create(argThat(usuario -> 
            usuario.getRol().equals(rol)
        ));
    }
    
    @Test
    @DisplayName("Crear usuario rechaza username vacío")
    void crearUsuarioUsernameVacio() {
        when(usuarioRepository.usernameExists(anyString())).thenReturn(false);
        
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, 
                () -> usuarioService.crearUsuario("", "Admin123!", "Nombre", Rol.PACIENTE)),
            () -> assertThrows(IllegalArgumentException.class, 
                () -> usuarioService.crearUsuario("   ", "Admin123!", "Nombre", Rol.PACIENTE)),
            () -> assertThrows(IllegalArgumentException.class, 
                () -> usuarioService.crearUsuario(null, "Admin123!", "Nombre", Rol.PACIENTE))
        );
    }
    
    @Test
    @DisplayName("Crear usuario rechaza nombre vacío")
    void crearUsuarioNombreVacio() {
        String username = "usuario";
        String password = "Admin123!";
        
        when(usuarioRepository.usernameExists(username)).thenReturn(false);
        
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, 
                () -> usuarioService.crearUsuario(username, password, "", Rol.PACIENTE)),
            () -> assertThrows(IllegalArgumentException.class, 
                () -> usuarioService.crearUsuario(username, password, "   ", Rol.PACIENTE)),
            () -> assertThrows(IllegalArgumentException.class, 
                () -> usuarioService.crearUsuario(username, password, null, Rol.PACIENTE))
        );
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
        "", "abc", "abcdef", "ABCDEF", "123456", "!@#$%",
        "abc123", "ABC123", "Abcdef", "Admin123", "admin123!"
    })
    @DisplayName("Crear usuario rechaza contraseñas inválidas")
    void crearUsuarioPasswordInvalido(String passwordInvalida) {
        String username = "usuario";
        String nombre = "Nombre";
        
        when(usuarioRepository.usernameExists(username)).thenReturn(false);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> usuarioService.crearUsuario(username, passwordInvalida, nombre, Rol.PACIENTE)
        );
        
        assertEquals(
            "La contraseña debe tener mínimo 6 caracteres, al menos un dígito, una mayúscula y un carácter especial",
            exception.getMessage()
        );
        
        verify(usuarioRepository, never()).create(any(Usuario.class));
    }
}