/*/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.ui;

import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.service.UsuarioService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JOptionPane;


/**
 *
 * @author asus
 */
public class LoginFrame extends JFrame {

    private UsuarioService usuarioService;
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistrar;

    public LoginFrame(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        initComponents();
    }

    private void initComponents() {
        setTitle("PIEDRAZUL - Inicio");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(220, 220, 220)); // gris claro

        // ==============================
        // Barra superior turquesa
        // ==============================
        JPanel topBar = new JPanel();
        topBar.setBackground(new Color(40, 170, 200));
        topBar.setPreferredSize(new Dimension(600, 70));
        topBar.setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("PIEDRAZUL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        topBar.add(lblTitulo, BorderLayout.WEST);

        // ==============================
        // Centro (formulario)
        // ==============================
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(220, 220, 220));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));

        JLabel lblSub = new JLabel("INICIAR SESIÓN");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblSub.setForeground(new Color(70, 170, 200));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblUsuario.setForeground(new Color(70, 170, 200));

        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtUsername.setBackground(new Color(180, 210, 220));
        txtUsername.setBorder(BorderFactory.createLineBorder(new Color(40,170,200), 2));

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPass.setForeground(new Color(70, 170, 200));

        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPassword.setBackground(new Color(180, 210, 220));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(40,170,200), 2));

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setBackground(new Color(70, 170, 200));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(250, 50));
        btnLogin.setMaximumSize(new Dimension(250, 50));

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.setBackground(new Color(70, 170, 200));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setMaximumSize(new Dimension(160, 40));

        JLabel lblInicio = new JLabel("Inicio");
        lblInicio.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        lblInicio.setForeground(new Color(70, 170, 200));
        lblInicio.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Agregar componentes
        centerPanel.add(lblSub);
        centerPanel.add(Box.createVerticalStrut(40));

        centerPanel.add(lblUsuario);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(txtUsername);
        centerPanel.add(Box.createVerticalStrut(25));

        centerPanel.add(lblPass);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(txtPassword);
        centerPanel.add(Box.createVerticalStrut(30));

        centerPanel.add(btnLogin);
        centerPanel.add(Box.createVerticalStrut(25));
        centerPanel.add(btnRegistrar);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(lblInicio);
        
        btnRegistrar.addActionListener(e -> {
            new RegistroFrame(usuarioService).setVisible(true);
        });
        
        btnLogin.addActionListener(e -> login());
        txtPassword.addActionListener(e -> login());
        
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }
    
    private void login() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese usuario y contraseña",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Usuario usuario = usuarioService.autenticar(username, password);
            //System.out.println("ROL DEL USUARIO: " + usuario.getRol());

            if (usuario == null) {
                JOptionPane.showMessageDialog(this,
                        "Usuario o contraseña incorrectos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
                return;
            }

            new MenuPrincipalFrame(usuario, usuarioService).setVisible(true);
            this.dispose();

        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

