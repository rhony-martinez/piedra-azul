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
        setSize(700, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(220, 220, 220));

        // ==============================
        // Barra superior
        // ==============================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(40, 170, 200));
        topBar.setPreferredSize(new Dimension(700, 70));

        JLabel lblTitulo = new JLabel("PIEDRAZUL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        topBar.add(lblTitulo, BorderLayout.WEST);

        // ==============================
        // Panel central
        // ==============================
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));
        centerPanel.setBackground(new Color(220, 220, 220));

        JLabel lblSub = new JLabel("REGISTRAR USUARIO");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblSub.setForeground(new Color(70, 170, 200));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== Campos =====

        txtNombreCompleto = crearCampoTexto();
        txtUsername = crearCampoTexto();
        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtPassword.setBackground(new Color(180, 210, 220));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(40,170,200), 2));

        cmbRol = new JComboBox<>(Rol.values());
        cmbRol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        cmbRol.setBackground(new Color(180, 210, 220));
        cmbRol.setBorder(BorderFactory.createLineBorder(new Color(40,170,200), 2));

        JLabel lblNombre = crearLabel("Nombre completo");
        JLabel lblUsername = crearLabel("Nombre de usuario");
        JLabel lblPass = crearLabel("Contraseña");
        JLabel lblRol = crearLabel("Rol");

        // ===== Botones =====
        JButton btnGuardar = crearBoton("Guardar");
        JButton btnCancelar = crearBoton("Cancelar");

        btnGuardar.addActionListener(e -> registrarUsuario());
        btnCancelar.addActionListener(e -> dispose());

        // ===== Agregar componentes =====

        centerPanel.add(lblSub);
        centerPanel.add(Box.createVerticalStrut(40));

        centerPanel.add(lblNombre);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(txtNombreCompleto);
        centerPanel.add(Box.createVerticalStrut(20));

        centerPanel.add(lblUsername);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(txtUsername);
        centerPanel.add(Box.createVerticalStrut(20));

        centerPanel.add(lblPass);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(txtPassword);
        centerPanel.add(Box.createVerticalStrut(20));

        centerPanel.add(lblRol);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(cmbRol);
        centerPanel.add(Box.createVerticalStrut(40));

        JPanel botonesPanel = new JPanel();
        botonesPanel.setBackground(new Color(220, 220, 220));
        botonesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        botonesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));

        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnCancelar);

        centerPanel.add(botonesPanel);
        centerPanel.add(Box.createVerticalStrut(40));

        JLabel lblRegistro = new JLabel("Registro");
        lblRegistro.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        lblRegistro.setForeground(new Color(70, 170, 200));
        lblRegistro.setAlignmentX(Component.RIGHT_ALIGNMENT);

        centerPanel.add(lblRegistro);


        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);


        add(mainPanel);
        }
    
    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        campo.setBackground(new Color(180, 210, 220));
        campo.setBorder(BorderFactory.createLineBorder(new Color(40,170,200), 2));
        return campo;
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(70, 170, 200));
        return label;
    }

    private JButton crearBoton(String texto) {
       JButton boton = new JButton(texto);
        boton.setBackground(new Color(70, 170, 200));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(180, 45));
        return boton;
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

