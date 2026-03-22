package com.mycompany.piedrazul.domain.service;

import com.mycompany.piedrazul.domain.model.Persona;
import com.mycompany.piedrazul.domain.model.Rol;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IPersonaRepository;
import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
import com.mycompany.piedrazul.utils.PasswordUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    
    private final IUsuarioRepository usuarioRepository;
    private final IPersonaRepository personaRepository;
    
    // Inyección de dependencia por constructor (DIP aplicado)
    public UsuarioService(IUsuarioRepository usuarioRepository, IPersonaRepository personaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
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
    
    // Crear y registrar nuevo usuario
    /*public boolean crearUsuario(String username, String password, String nombreCompleto, Rol rol) {
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
        
        /*String passwordHash = PasswordUtils.hashPassword(password);
        Usuario nuevoUsuario = new Usuario(username, passwordHash, nombreCompleto, rol);
        
        return usuarioRepository.create(nuevoUsuario);
    }*/
    
    public boolean registrarUsuario(
        String username,
        String password,
        Rol rol,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        String genero,
        LocalDate fechaNac,
        String telefono,
        int dni
    ) {

        // Validaciones
        if (usuarioRepository.usernameExists(username)) {
            throw new IllegalArgumentException("El username ya existe");
        }

        if (personaRepository.dniExists(dni)) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }

        // 1. Crear Persona
        Persona persona = new Persona(
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                genero,
                fechaNac,
                telefono,
                dni
        );

        persona = personaRepository.create(persona);

        if (persona == null || persona.getId() == 0) {
            throw new RuntimeException("Error al crear persona");
        }

        // 2. Crear Usuario
        String passwordHash = PasswordUtils.hashPassword(password);

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPasswordHash(passwordHash);
        usuario.setPersonaId(persona.getId());
        usuario.setActivo(true);
        usuario.setIntentosFallidos(0);
        usuario.setRol(rol);

        boolean creadoUsuario = usuarioRepository.create(usuario);

        if (!creadoUsuario) {
            throw new RuntimeException("Error al crear usuario");
        }

        // 🔴 IMPORTANTE: aquí aún NO insertamos rol (falta repo)

        return true;
    }
    /*private boolean esRolValido(Rol rol) {
        return rol != null && (
            rol == "PACIENTE" || 
            rol == "MEDICO_TERAPISTA" || 
            rol == "AGENDADOR"
        );
    }*/
    
    public List<Usuario> obtenerTodosLosPacientes() {
        List<Usuario> todos = usuarioRepository.findAll();
        List<Usuario> pacientes = new ArrayList<>();
        for (Usuario u : todos) {
            if (u.getRol() == Rol.PACIENTE && u.isActivo()) {
                pacientes.add(u);
            }
        }
        return pacientes;
    }

    public List<Usuario> obtenerTodosLosMedicos() {
        List<Usuario> todos = usuarioRepository.findAll();
        List<Usuario> medicos = new ArrayList<>();
        for (Usuario u : todos) {
            if (u.getRol() == Rol.MEDICO_TERAPISTA && u.isActivo()) {
                medicos.add(u);
            }
        }
        return medicos;
    }
}