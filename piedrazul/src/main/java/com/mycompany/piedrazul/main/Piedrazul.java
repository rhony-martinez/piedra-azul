package com.mycompany.piedrazul.main;

import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.service.UsuarioService;
import com.mycompany.piedrazul.infrastructure.persistence.UsuarioRepositorySQLite;
import com.mycompany.piedrazul.ui.Forms;

public class Piedrazul {
    
    private static UsuarioService usuarioService;
    private static Forms forms;
    
    public static void main(String[] args) {
        // Inicializar dependencias
        UsuarioRepositorySQLite repository = new UsuarioRepositorySQLite();
        usuarioService = new UsuarioService(repository);
        
        
        crearUsuarioAdminPorDefecto();
        
        // Iniciar interfaz gráfica
        forms = new Forms(usuarioService);
        forms.setVisible(true);
    }
    
    private static void crearUsuarioAdminPorDefecto() {
        try {
            // Intentar autenticar para ver si existe
            Usuario admin = usuarioService.autenticar("admin", "Admin123!");
            if (admin != null) {
                System.out.println("Usuario admin ya existe");
                return;
            }
        } catch (Exception e) {
            // No existe, crearlo
            try {
                usuarioService.crearUsuario(
                    "admin", 
                    "Admin123!", 
                    "Administrador del Sistema", 
                    "ADMINISTRADOR"
                );
                System.out.println("Usuario admin creado por defecto");
            } catch (IllegalArgumentException ex) {
                System.out.println("Error al crear admin: " + ex.getMessage());
            }
        }
    }
    
    public static UsuarioService getUsuarioService() {
        return usuarioService;
    }
}