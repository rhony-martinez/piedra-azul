package com.mycompany.piedrazul.domain.service;

import com.mycompany.piedrazul.domain.model.Rol;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
import com.mycompany.piedrazul.utils.PasswordUtils;

public class UsuarioService {
    
    private final IUsuarioRepository usuarioRepository;
    
    // Inyección de dependencia por constructor (DIP aplicado)
    public UsuarioService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    // Autenticar usuario
    public Usuario autenticar(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }
        
        Usuario usuario = usuarioRepository.findByUsername(username);
        
        if (usuario == null) {
            return null; // Usuario no existe
        }
        
        if (!usuario.isActivo()) {
            throw new IllegalStateException("Usuario inactivo");
        }
        
        String passwordHash = PasswordUtils.hashPassword(password);
        Usuario usuarioAutenticado = usuarioRepository.authenticate(username, passwordHash);
        
        if (usuarioAutenticado != null) {
            usuarioRepository.resetearIntentosFallidos(username);
            return usuarioAutenticado;
        } else {
            usuarioRepository.registrarIntentoFallido(username);
            return null;
        }
    }
    
    // Crear nuevo usuario
    public boolean crearUsuario(String username, String password, String nombreCompleto, Rol rol) {
        // Validaciones
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es requerido");
        }
        
        if (usuarioRepository.usernameExists(username)) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        
        if (!PasswordUtils.validarContrasena(password)) {
            throw new IllegalArgumentException("La contraseña debe tener mínimo 6 caracteres, al menos un dígito, una mayúscula y un carácter especial");
        }
        
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre completo es requerido");
        }
        
        /*if (!esRolValido(rol)) {
            throw new IllegalArgumentException("Rol inválido. Debe ser: PACIENTE, MEDICO_TERAPISTA o AGENDADOR");
        }*/
        
        String passwordHash = PasswordUtils.hashPassword(password);
        Usuario nuevoUsuario = new Usuario(username, passwordHash, nombreCompleto, rol);
        
        return usuarioRepository.create(nuevoUsuario);
    }
    
    /*private boolean esRolValido(Rol rol) {
        return rol != null && (
            rol == "PACIENTE" || 
            rol == "MEDICO_TERAPISTA" || 
            rol == "AGENDADOR"
        );
    }*/
}