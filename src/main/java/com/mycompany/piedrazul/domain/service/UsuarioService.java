package com.mycompany.piedrazul.domain.service;

import com.mycompany.piedrazul.domain.model.MedicoEstado;
import com.mycompany.piedrazul.domain.model.Persona;
import com.mycompany.piedrazul.domain.model.Rol;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IMedicoRepository;
import com.mycompany.piedrazul.domain.repository.IPacienteRepository;
import com.mycompany.piedrazul.domain.repository.IPersonaRepository;
import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
import com.mycompany.piedrazul.utils.PasswordUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    
    private final IUsuarioRepository usuarioRepository;
    private final IPersonaRepository personaRepository;
    private final IPacienteRepository pacienteRepository;
    private final IMedicoRepository medicoRepository;
    
    // Inyección de dependencia por constructor (DIP aplicado)
    public UsuarioService(IUsuarioRepository usuarioRepository, IPersonaRepository personaRepository, IPacienteRepository pacienteRepository, IMedicoRepository medicoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
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
    
    // Registrar un usuario (con correo)
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
        int dni,
        String correo
    ) {

        System.out.println("=== UsuarioService.registrarUsuario ===");
        System.out.println("Username: " + username);
        System.out.println("Rol: " + rol);
        System.out.println("DNI: " + dni);
        System.out.println("Correo: " + (correo == null ? "null" : correo));
        
        // Validaciones
        if (usuarioRepository.usernameExists(username)) {
            throw new IllegalArgumentException("El username ya existe");
        }

        if (personaRepository.dniExists(dni)) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }

        // 1. Crear Persona (con correo)
        String correoFinal = (correo == null || correo.trim().isEmpty()) ? "" : correo;
        
        Persona persona = new Persona(
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                genero,
                fechaNac,
                telefono,
                dni,
                correoFinal
        );

        System.out.println("Creando persona...");
        persona = personaRepository.create(persona);

        if (persona == null || persona.getId() == 0) {
            throw new RuntimeException("Error al crear persona");
        }
        System.out.println("Persona creada con ID: " + persona.getId());

        // 2. Crear Usuario
        String passwordHash = PasswordUtils.hashPassword(password);
        System.out.println("Password hasheada correctamente");

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPasswordHash(passwordHash);
        usuario.setPersonaId(persona.getId());
        usuario.setActivo(true);
        usuario.setIntentosFallidos(0);
        usuario.setRol(rol);

        System.out.println("Creando usuario...");
        boolean creadoUsuario = usuarioRepository.create(usuario);

        if (!creadoUsuario) {
            throw new RuntimeException("Error al crear usuario");
        }
        System.out.println("Usuario creado correctamente");

        // 3. Crear registros específicos según rol
        switch (rol) {
            case PACIENTE -> {
                System.out.println("Creando registro de paciente...");
                boolean creadoPaciente = pacienteRepository.create(persona.getId());
                if (!creadoPaciente) {
                    throw new RuntimeException("Error al crear paciente");
                }
                System.out.println("Paciente creado correctamente");
            }
        
            case MEDICO_TERAPISTA -> {
                System.out.println("Creando registro de médico...");
                String tipo = "MEDICO";
                MedicoEstado estado = MedicoEstado.ACTIVO;
                boolean creadoMedico = medicoRepository.create(persona.getId(), tipo, estado);
                if (!creadoMedico) {
                    throw new RuntimeException("Error al crear médico");
                }
                System.out.println("Médico creado correctamente");
            }

            default -> {
                System.out.println("Rol " + rol + " no requiere registro adicional");
            }
        }
        
        System.out.println("=== REGISTRO COMPLETADO CON ÉXITO ===");
        return true;
    }
    
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
    
    // Método para verificar si un usuario existe por username (sin autenticar)
    public Usuario obtenerUsuarioPorUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return usuarioRepository.findByUsername(username);
    }
    
}