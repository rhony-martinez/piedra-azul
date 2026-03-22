/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.ui;

import com.mycompany.piedrazul.domain.model.Rol;
import com.mycompany.piedrazul.domain.service.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

/**
 *
 * @author asus
 */

public class RegistroFrame extends JFrame {

    private final UsuarioService usuarioService;

    // Datos Personales (Persona tabla)
    private JTextField txtPrimerNombre;
    private JTextField txtSegundoNombre;
    private JTextField txtPrimerApellido;
    private JTextField txtSegundoApellido;
    private JComboBox<String> cmbGenero;
    private JSpinner spinFechaNac;
    private JTextField txtTelefono;
    private JTextField txtDni;

    // Datos Usuario
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRol;

    public RegistroFrame(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        initComponents();
    }

    private void initComponents() {

        setTitle("PIEDRAZUL - Registro de Usuario");
        setSize(900, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(220, 220, 220));

        // ==============================
        // Barra superior
        // ==============================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(40, 170, 200));
        topBar.setPreferredSize(new Dimension(900, 70));

        JLabel lblTitulo = new JLabel("PIEDRAZUL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        topBar.add(lblTitulo, BorderLayout.WEST);

        // ==============================
        // Panel central con scroll
        // ==============================
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        centerPanel.setBackground(new Color(220, 220, 220));

        JLabel lblSub = new JLabel("REGISTRAR NUEVO USUARIO");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblSub.setForeground(new Color(70, 170, 200));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(lblSub);
        centerPanel.add(Box.createVerticalStrut(30));

        // ===== SECCIÓN DATOS PERSONALES =====
        JLabel lblSeccionPersonal = crearLabel("DATOS PERSONALES");
        lblSeccionPersonal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        centerPanel.add(lblSeccionPersonal);
        centerPanel.add(Box.createVerticalStrut(15));

        // Primer nombre
        txtPrimerNombre = crearCampoTexto();
        centerPanel.add(crearLabel("Primer Nombre *"));
        centerPanel.add(Box.createVerticalStrut(3));
        centerPanel.add(txtPrimerNombre);
        centerPanel.add(Box.createVerticalStrut(15));

        // Segundo nombre
        txtSegundoNombre = crearCampoTexto();
        centerPanel.add(crearLabel("Segundo Nombre"));
        centerPanel.add(Box.createVerticalStrut(3));
        centerPanel.add(txtSegundoNombre);
        centerPanel.add(Box.createVerticalStrut(15));

        // Primer apellido
        txtPrimerApellido = crearCampoTexto();
        centerPanel.add(crearLabel("Primer Apellido *"));
        centerPanel.add(Box.createVerticalStrut(3));
        centerPanel.add(txtPrimerApellido);
        centerPanel.add(Box.createVerticalStrut(15));

        // Segundo apellido
        txtSegundoApellido = crearCampoTexto();
        centerPanel.add(crearLabel("Segundo Apellido"));
        centerPanel.add(Box.createVerticalStrut(3));
        centerPanel.add(txtSegundoApellido);
        centerPanel.add(Box.createVerticalStrut(15));

        // Género
        cmbGenero = new JComboBox<>(new String[]{"HOMBRE", "MUJER"});
        cmbGenero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        cmbGenero.setBackground(new Color(180, 210, 220));
        cmbGenero.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        centerPanel.add(crearLabel("Género *"));
        centerPanel.add(Box.createVerticalStrut(3));
        centerPanel.add(cmbGenero);
        centerPanel.add(Box.createVerticalStrut(15));

        // Fecha de nacimiento (Date Picker)
        spinFechaNac = new JSpinner(new SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.YEAR));
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinFechaNac, "yyyy-MM-dd");
        spinFechaNac.setEditor(editor);
        spinFechaNac.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        centerPanel.add(crearLabel("Fecha de Nacimiento (yyyy-MM-dd) *"));
        centerPanel.add(Box.createVerticalStrut(3));
        centerPanel.add(spinFechaNac);
        centerPanel.add(Box.createVerticalStrut(15));

        // Teléfono
        txtTelefono = crearCampoTexto();
        centerPanel.add(crearLabel("Teléfono *"));
        centerPanel.add(Box.createVerticalStrut(3));
        centerPanel.add(txtTelefono);
        centerPanel.add(Box.createVerticalStrut(15));

        // DNI
        txtDni = crearCampoTexto();
        txtDni.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtDni.setBackground(new Color(180, 210, 220));
        txtDni.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        centerPanel.add(crearLabel("DNI *"));
        centerPanel.add(Box.createVerticalStrut(3));
        centerPanel.add(txtDni);
        centerPanel.add(Box.createVerticalStrut(25));

        // ===== SECCIÓN DATOS DE USUARIO =====
        JLabel lblSeccionUsuario = crearLabel("DATOS DE ACCESO");
        lblSeccionUsuario.setFont(new Font("Segoe UI", Font.BOLD, 16));
        centerPanel.add(lblSeccionUsuario);
        centerPanel.add(Box.createVerticalStrut(15));

        // Username
        txtUsername = crearCampoTexto();
        centerPanel.add(crearLabel("Nombre de Usuario *"));
        centerPanel.add(Box.createVerticalStrut(3));
        centerPanel.add(txtUsername);
        centerPanel.add(Box.createVerticalStrut(15));

        // Password
        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtPassword.setBackground(new Color(180, 210, 220));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        centerPanel.add(crearLabel("Contraseña *"));
        centerPanel.add(Box.createVerticalStrut(3));
        centerPanel.add(txtPassword);
        centerPanel.add(Box.createVerticalStrut(15));

        // Rol
        cmbRol = new JComboBox<>(new String[]{"PACIENTE", "MEDICO_TERAPISTA", "AGENDADOR", "ADMINISTRADOR"});
        cmbRol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        cmbRol.setBackground(new Color(180, 210, 220));
        cmbRol.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        centerPanel.add(crearLabel("Rol *"));
        centerPanel.add(Box.createVerticalStrut(3));
        centerPanel.add(cmbRol);
        centerPanel.add(Box.createVerticalStrut(30));

        // ===== Botones =====
        JButton btnGuardar = crearBoton("Registrarse");
        JButton btnCancelar = crearBoton("Cancelar");

        btnGuardar.addActionListener(e -> registrarUsuario());
        btnCancelar.addActionListener(e -> dispose());

        JPanel botonesPanel = new JPanel();
        botonesPanel.setBackground(new Color(220, 220, 220));
        botonesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        botonesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnCancelar);

        centerPanel.add(botonesPanel);

        // Scroll pane para el panel central
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

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
        // Validar campos obligatorios
        if (txtPrimerNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primer nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtPrimerApellido.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primer apellido es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtTelefono.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Teléfono es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String dniText = txtDni.getText().trim();
        if (dniText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "DNI es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!dniText.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "DNI debe contener solo dígitos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (dniText.length() < 6 || dniText.length() > 15) {
            JOptionPane.showMessageDialog(this, "DNI debe tener entre 6 y 15 dígitos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtUsername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre de usuario es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (new String(txtPassword.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Contraseña es obligatoria", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Recopilar datos
        String primerNombre = txtPrimerNombre.getText().trim();
        String segundoNombre = txtSegundoNombre.getText().trim();
        String primerApellido = txtPrimerApellido.getText().trim();
        String segundoApellido = txtSegundoApellido.getText().trim();
        String genero = (String) cmbGenero.getSelectedItem();
        LocalDate fechaNac = ((java.util.Date) spinFechaNac.getValue()).toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        String telefono = txtTelefono.getText().trim();
        int dni = Integer.parseInt(dniText);
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String rol = (String) cmbRol.getSelectedItem();

        
        StringBuilder mensaje = new StringBuilder("Datos capturados:\n\n");
        mensaje.append("Nombre: ").append(primerNombre).append(" ").append(segundoNombre).append("\n");
        mensaje.append("Apellido: ").append(primerApellido).append(" ").append(segundoApellido).append("\n");
        mensaje.append("Género: ").append(genero).append("\n");
        mensaje.append("Fecha Nac: ").append(fechaNac).append("\n");
        mensaje.append("Teléfono: ").append(telefono).append("\n");
        mensaje.append("DNI: ").append(dni).append("\n");
        mensaje.append("Username: ").append(username).append("\n");
        mensaje.append("Rol: ").append(rol).append("\n");

        //JOptionPane.showMessageDialog(this, mensaje.toString(), "Validación", JOptionPane.INFORMATION_MESSAGE);
        try {
            //String nombreCompleto = primerNombre + " " + segundoNombre + " " + primerApellido + " " + segundoApellido;

            boolean creado = usuarioService.registrarUsuario(
                    username,
                    password,
                    mapearRol(rol),
                    primerNombre,
                    segundoNombre,
                    primerApellido,
                    segundoApellido,
                    genero,
                    fechaNac,
                    telefono,
                    dni
            );

            if (creado) {
                JOptionPane.showMessageDialog(this, "Usuario registrado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar el usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Rol mapearRol(String rolUI) {
        switch (rolUI) {
            case "PACIENTE":
                return Rol.PACIENTE;
            case "MEDICO_TERAPISTA":
                return Rol.MEDICO_TERAPISTA;
            case "AGENDADOR":
                return Rol.AGENDADOR;
            case "ADMINISTRADOR":
                return Rol.ADMINISTRADOR;
            default:
                throw new IllegalArgumentException("Rol inválido");
        }
    }
    
    private void limpiarFormulario() {
        txtPrimerNombre.setText("");
        txtSegundoNombre.setText("");
        txtPrimerApellido.setText("");
        txtSegundoApellido.setText("");
        txtTelefono.setText("");
        txtDni.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
    }
}

