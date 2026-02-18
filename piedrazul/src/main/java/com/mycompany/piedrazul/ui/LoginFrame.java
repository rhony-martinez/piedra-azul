/*/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.ui;

import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.service.UsuarioService;
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
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        mainPanel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("PIEDRAZUL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("INICIAR SESIÓN");
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setContentAreaFilled(false);
        btnRegistrar.setForeground(Color.BLUE);
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(lblSub);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(new JLabel("Usuario"));
        mainPanel.add(txtUsername);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(new JLabel("Contraseña"));
        mainPanel.add(txtPassword);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(btnLogin);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnRegistrar);
        
        btnRegistrar.addActionListener(e -> {
            new RegistroFrame(usuarioService).setVisible(true);
        });
        
        btnLogin.addActionListener(e -> login());
        txtPassword.addActionListener(e -> login());

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

