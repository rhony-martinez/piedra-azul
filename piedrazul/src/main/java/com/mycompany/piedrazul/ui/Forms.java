package com.mycompany.piedrazul.ui;

import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.service.UsuarioService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Forms extends JFrame {
    
    private UsuarioService usuarioService;
    private Usuario usuarioActual;
    
    // Componentes del login
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblMensaje;
    
    public Forms(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        initLoginPanel();
    }
    
    private void initLoginPanel() {
        setTitle("Sistema de Agendamiento - Piedrazul");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitle = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Usuario:"), gbc);
        
        gbc.gridx = 1;
        txtUsername = new JTextField(15);
        formPanel.add(txtUsername, gbc);
        
        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Contraseña:"), gbc);
        
        gbc.gridx = 1;
        txtPassword = new JPasswordField(15);
        formPanel.add(txtPassword, gbc);
        
        // Botón de login
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        formPanel.add(btnLogin, gbc);
        
        // Mensaje de error
        gbc.gridy = 3;
        lblMensaje = new JLabel(" ", SwingConstants.CENTER);
        lblMensaje.setForeground(Color.RED);
        formPanel.add(lblMensaje, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Panel de registro rápido (solo para desarrollo/pruebas)
        JPanel devPanel = new JPanel(new FlowLayout());
        JButton btnCrearUsuario = new JButton("Crear Usuario de Prueba");
        btnCrearUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDialogoCrearUsuario();
            }
        });
        devPanel.add(btnCrearUsuario);
        mainPanel.add(devPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Acción al presionar Enter en el campo de contraseña
        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }
    
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Ingrese usuario y contraseña");
            return;
        }
        
        try {
            usuarioActual = usuarioService.autenticar(username, password);
            
            if (usuarioActual != null) {
                lblMensaje.setText("");
                JOptionPane.showMessageDialog(this, 
                    "Bienvenido " + usuarioActual.getNombreCompleto() + 
                    "\nRol: " + usuarioActual.getRol(), 
                    "Login Exitoso", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Aquí irías a la pantalla principal según el rol
                mostrarPantallaPrincipal();
                
            } else {
                lblMensaje.setText("Usuario o contraseña incorrectos");
                txtPassword.setText("");
            }
        } catch (IllegalStateException e) {
            lblMensaje.setText(e.getMessage());
        }
    }
    
    private void mostrarPantallaPrincipal() {
        // Por ahora solo ocultamos el login
        this.dispose();
        
        // Ventana principal temporal
        JFrame mainFrame = new JFrame("Sistema Principal - Piedrazul");
        mainFrame.setSize(600, 400);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new BorderLayout());
        
        // Información del usuario
        JLabel lblInfo = new JLabel(
            "<html><h2>Bienvenido al Sistema de Agendamiento</h2>"
            + "<p><b>Usuario:</b> " + usuarioActual.getUsername() + "</p>"
            + "<p><b>Nombre:</b> " + usuarioActual.getNombreCompleto() + "</p>"
            + "<p><b>Rol:</b> " + usuarioActual.getRol() + "</p>"
            + "<p><b>Estado:</b> " + (usuarioActual.isActivo() ? "Activo" : "Inactivo") + "</p>"
            + "</html>", 
            SwingConstants.CENTER
        );
        panel.add(lblInfo, BorderLayout.CENTER);
        
        // Botón cerrar sesión
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                usuarioActual = null;
                Forms.this.setVisible(true);
                txtUsername.setText("");
                txtPassword.setText("");
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnCerrarSesion);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainFrame.add(panel);
        mainFrame.setVisible(true);
    }
    
    private void mostrarDialogoCrearUsuario() {
        JDialog dialog = new JDialog(this, "Crear Usuario de Prueba", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Campos del formulario
        JTextField txtNewUsername = new JTextField(15);
        JPasswordField txtNewPassword = new JPasswordField(15);
        JTextField txtNewNombre = new JTextField(15);
        JComboBox<String> cmbRol = new JComboBox<>(new String[]{"PACIENTE", "MEDICO_TERAPISTA", "AGENDADOR"});
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNewUsername, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNewPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNewNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbRol, gbc);
        
        // Botones
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtNewUsername.getText().trim();
                String password = new String(txtNewPassword.getPassword());
                String nombre = txtNewNombre.getText().trim();
                String rol = (String) cmbRol.getSelectedItem();
                
                try {
                    boolean creado = usuarioService.crearUsuario(username, password, nombre, rol);
                    if (creado) {
                        JOptionPane.showMessageDialog(dialog, "Usuario creado exitosamente");
                        dialog.dispose();
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
}