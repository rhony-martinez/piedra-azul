/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.ui;

import com.mycompany.piedrazul.domain.model.Rol;
import com.mycompany.piedrazul.domain.service.UsuarioService;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author asus
 */

public class RegistroFrame extends JFrame {

    private final UsuarioService usuarioService;

    private JTextField txtNombreCompleto;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<Rol> cmbRol;

    public RegistroFrame(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        initComponents();
    }

    private void initComponents() {

        setTitle("PIEDRAZUL - Registro");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        mainPanel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("PIEDRAZUL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("REGISTRAR USUARIO");
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtNombreCompleto = new JTextField();
        txtNombreCompleto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // 🔥 Aquí usamos el enum directamente
        cmbRol = new JComboBox<>(Rol.values());
        cmbRol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Acciones
        btnGuardar.addActionListener(e -> registrarUsuario());
        btnCancelar.addActionListener(e -> dispose());

        // Agregar componentes
        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(lblSubtitulo);
        mainPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(new JLabel("Nombre completo"));
        mainPanel.add(txtNombreCompleto);
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(new JLabel("Nombre de usuario"));
        mainPanel.add(txtUsername);
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(new JLabel("Contraseña"));
        mainPanel.add(txtPassword);
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(new JLabel("Rol"));
        mainPanel.add(cmbRol);
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(btnGuardar);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnCancelar);

        add(mainPanel);
    }

    private void registrarUsuario() {

        String nombre = txtNombreCompleto.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        Rol rol = (Rol) cmbRol.getSelectedItem();

        if (nombre.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean creado = usuarioService.crearUsuario(username, password, nombre, rol);

            if (creado) {
                JOptionPane.showMessageDialog(this,
                        "Usuario registrado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

