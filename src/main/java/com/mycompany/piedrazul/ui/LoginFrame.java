package com.mycompany.piedrazul.ui;

import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.service.UsuarioService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

public class LoginFrame extends JFrame {

    private UsuarioService usuarioService;
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistrar;
    
    // Labels para mensajes de error
    private JLabel lblErrorUsuario;
    private JLabel lblErrorPassword;
    
    // Bordes
    private Border bordeNormal;
    private Border bordeError;

    public LoginFrame(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        initComponents();
    }

    private void initComponents() {
        setTitle("PIEDRAZUL - Inicio");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Definir bordes
        bordeNormal = BorderFactory.createLineBorder(new Color(40, 170, 200), 2);
        bordeError = BorderFactory.createLineBorder(new Color(255, 150, 150), 2);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(220, 220, 220));

        // ==============================
        // Barra superior turquesa
        // ==============================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(40, 170, 200));
        topBar.setPreferredSize(new Dimension(800, 80));

        JLabel lblTitulo = new JLabel("PIEDRAZUL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 36));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 10));

        topBar.add(lblTitulo, BorderLayout.WEST);

        // ==============================
        // Centro (formulario) con GridBagLayout
        // ==============================
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(220, 220, 220));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Panel interno del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(220, 220, 220));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(8, 20, 8, 20);
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Título del formulario
        JLabel lblSub = new JLabel("INICIAR SESIÓN");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblSub.setForeground(new Color(70, 170, 200));
        formGbc.gridx = 0;
        formGbc.gridy = row;
        formGbc.gridwidth = 2;
        formGbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(lblSub, formGbc);
        row++;
        
        // Espacio vertical
        formGbc.gridy = row;
        formGbc.insets = new Insets(20, 20, 8, 20);
        formPanel.add(new JLabel(), formGbc);
        row++;
        
        // Label Usuario
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblUsuario.setForeground(new Color(70, 170, 200));
        formGbc.gridy = row;
        formGbc.gridwidth = 2;
        formGbc.anchor = GridBagConstraints.WEST;
        formGbc.insets = new Insets(5, 20, 5, 20);
        formPanel.add(lblUsuario, formGbc);
        row++;
        
        // Campo Usuario
        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(350, 45));
        txtUsername.setBackground(new Color(180, 210, 220));
        txtUsername.setBorder(bordeNormal);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formGbc.gridy = row;
        formGbc.insets = new Insets(2, 20, 2, 20);
        formPanel.add(txtUsername, formGbc);
        row++;
        
        // Label error Usuario
        lblErrorUsuario = new JLabel(" ");
        lblErrorUsuario.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblErrorUsuario.setForeground(new Color(255, 120, 120));
        formGbc.gridy = row;
        formGbc.insets = new Insets(0, 20, 10, 20);
        formPanel.add(lblErrorUsuario, formGbc);
        row++;
        
        // Espacio vertical
        formGbc.gridy = row;
        formGbc.insets = new Insets(5, 20, 5, 20);
        formPanel.add(new JLabel(), formGbc);
        row++;
        
        // Label Contraseña
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPass.setForeground(new Color(70, 170, 200));
        formGbc.gridy = row;
        formGbc.insets = new Insets(5, 20, 5, 20);
        formPanel.add(lblPass, formGbc);
        row++;
        
        // Campo Contraseña
        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(350, 45));
        txtPassword.setBackground(new Color(180, 210, 220));
        txtPassword.setBorder(bordeNormal);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formGbc.gridy = row;
        formGbc.insets = new Insets(2, 20, 2, 20);
        formPanel.add(txtPassword, formGbc);
        row++;
        
        // Label error Contraseña
        lblErrorPassword = new JLabel(" ");
        lblErrorPassword.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblErrorPassword.setForeground(new Color(255, 120, 120));
        formGbc.gridy = row;
        formGbc.insets = new Insets(0, 20, 10, 20);
        formPanel.add(lblErrorPassword, formGbc);
        row++;
        
        // Espacio vertical
        formGbc.gridy = row;
        formGbc.insets = new Insets(15, 20, 15, 20);
        formPanel.add(new JLabel(), formGbc);
        row++;
        
        // Botón Iniciar Sesión
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(new Color(70, 170, 200));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(250, 50));
        btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        formGbc.gridy = row;
        formGbc.insets = new Insets(5, 20, 10, 20);
        formPanel.add(btnLogin, formGbc);
        row++;
        
        // Botón Registrar
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBackground(new Color(70, 170, 200));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(180, 40));
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        formGbc.gridy = row;
        formGbc.insets = new Insets(5, 20, 10, 20);
        formPanel.add(btnRegistrar, formGbc);
        row++;
        
        // Texto decorativo
        JLabel lblInicio = new JLabel("Inicio");
        lblInicio.setFont(new Font("Segoe UI", Font.ITALIC, 22));
        lblInicio.setForeground(new Color(70, 170, 200));
        formGbc.gridy = row;
        formGbc.insets = new Insets(15, 20, 10, 20);
        formPanel.add(lblInicio, formGbc);
        
        // Centrar el formulario
        centerPanel.add(formPanel, gbc);
        
        // ==============================
        // Eventos de validación en tiempo real
        // ==============================
        
        // Validar usuario cuando pierde el foco
        txtUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                validarUsuarioEnTiempoReal();
            }
        });
        
        // Validar contraseña cuando pierde el foco
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                validarPasswordEnTiempoReal();
            }
        });
        
        // Limpiar error de usuario cuando empieza a escribir
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                limpiarErrorUsuario();
            }
        });
        
        // Limpiar error de contraseña cuando empieza a escribir
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                limpiarErrorPassword();
            }
        });
        
        // Acciones de botones
        btnRegistrar.addActionListener(e -> {
            new RegistroFrame(usuarioService).setVisible(true);
            this.dispose();
        });
        
        btnLogin.addActionListener(e -> login());
        txtPassword.addActionListener(e -> login());
        
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void validarUsuarioEnTiempoReal() {
        String username = txtUsername.getText().trim();
        if (username.isEmpty()) {
            mostrarErrorUsuario("El usuario no puede estar vacío");
        } else {
            limpiarErrorUsuario();
        }
    }
    
    private void validarPasswordEnTiempoReal() {
        String password = new String(txtPassword.getPassword());
        if (password.isEmpty()) {
            mostrarErrorPassword("La contraseña no puede estar vacía");
        } else {
            limpiarErrorPassword();
        }
    }
    
    private void mostrarErrorUsuario(String mensaje) {
        txtUsername.setBorder(bordeError);
        lblErrorUsuario.setText(mensaje);
        lblErrorUsuario.setForeground(new Color(255, 120, 120));
    }
    
    private void mostrarErrorPassword(String mensaje) {
        txtPassword.setBorder(bordeError);
        lblErrorPassword.setText(mensaje);
        lblErrorPassword.setForeground(new Color(255, 120, 120));
    }
    
    private void limpiarErrorUsuario() {
        txtUsername.setBorder(bordeNormal);
        lblErrorUsuario.setText(" ");
    }
    
    private void limpiarErrorPassword() {
        txtPassword.setBorder(bordeNormal);
        lblErrorPassword.setText(" ");
    }
    
    private void limpiarTodosErrores() {
        limpiarErrorUsuario();
        limpiarErrorPassword();
    }
    
    private void login() {
        // Limpiar errores anteriores
        limpiarTodosErrores();

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        boolean hasError = false;

        // Validar campos vacíos
        if (username.isEmpty()) {
            mostrarErrorUsuario("El usuario es obligatorio");
            hasError = true;
        }

        if (password.isEmpty()) {
            mostrarErrorPassword("La contraseña es obligatoria");
            hasError = true;
        }

        if (hasError) {
            return;
        }

        try {
            // Primero verificamos si el usuario existe
            Usuario usuarioExistente = usuarioService.obtenerUsuarioPorUsername(username);

            if (usuarioExistente == null) {
                // El usuario NO existe
                mostrarErrorUsuario("Usuario incorrecto");
                txtPassword.setText("");
                return;
            }

            // El usuario existe, ahora verificamos la contraseña
            Usuario usuario = usuarioService.autenticar(username, password);

            if (usuario == null) {
                // Usuario existe pero contraseña incorrecta
                mostrarErrorPassword("Contraseña incorrecta");
                txtPassword.setText("");
                return;
            }

            // Login exitoso
            MenuPrincipalFrame menuFrame = new MenuPrincipalFrame(usuario, usuarioService);
            menuFrame.setVisible(true);
            this.dispose();

        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al conectar con el servidor",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}