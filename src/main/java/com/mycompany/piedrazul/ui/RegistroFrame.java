package com.mycompany.piedrazul.ui;

import com.mycompany.piedrazul.domain.model.Rol;
import com.mycompany.piedrazul.domain.service.UsuarioService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class RegistroFrame extends JFrame {

    private final UsuarioService usuarioService;

    // Datos Personales
    private JTextField txtPrimerNombre;
    private JTextField txtSegundoNombre;
    private JTextField txtPrimerApellido;
    private JTextField txtSegundoApellido;
    private JComboBox<String> cmbGenero;
    private JSpinner spinFechaNac;
    private JTextField txtTelefono;
    private JTextField txtDni;
    private JTextField txtCorreo;

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
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(220, 220, 220));

        // ==============================
        // Barra superior
        // ==============================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(40, 170, 200));
        topBar.setPreferredSize(new Dimension(800, 80));

        JLabel lblTitulo = new JLabel("PIEDRAZUL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 36));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 10));

        // Botón volver
        JButton btnVolver = new JButton("← Volver");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVolver.setBackground(new Color(40, 170, 200));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.setPreferredSize(new Dimension(100, 40));
        btnVolver.addActionListener(e -> {
            new LoginFrame(usuarioService).setVisible(true);
            this.dispose();
        });
        
        topBar.add(lblTitulo, BorderLayout.WEST);
        topBar.add(btnVolver, BorderLayout.EAST);

        // ==============================
        // Panel central con scroll
        // ==============================
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 150, 30, 150));
        centerPanel.setBackground(new Color(220, 220, 220));

        JLabel lblSub = new JLabel("REGISTRAR NUEVO USUARIO");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblSub.setForeground(new Color(70, 170, 200));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(lblSub);
        centerPanel.add(Box.createVerticalStrut(40));

        // ===== SECCIÓN DATOS PERSONALES =====
        JLabel lblSeccionPersonal = crearLabelSeccion("DATOS PERSONALES");
        centerPanel.add(lblSeccionPersonal);
        centerPanel.add(Box.createVerticalStrut(20));

        // Panel de dos columnas para datos personales
        JPanel datosPersonalesPanel = new JPanel(new GridBagLayout());
        datosPersonalesPanel.setBackground(new Color(220, 220, 220));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Primer Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        datosPersonalesPanel.add(crearLabelCampo("Primer Nombre *"), gbc);
        txtPrimerNombre = crearCampoTexto();
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        datosPersonalesPanel.add(txtPrimerNombre, gbc);
        
        // Segundo Nombre
        gbc.gridx = 0;
        gbc.gridy = 1;
        datosPersonalesPanel.add(crearLabelCampo("Segundo Nombre"), gbc);
        txtSegundoNombre = crearCampoTexto();
        gbc.gridx = 1;
        datosPersonalesPanel.add(txtSegundoNombre, gbc);
        
        // Primer Apellido
        gbc.gridx = 0;
        gbc.gridy = 2;
        datosPersonalesPanel.add(crearLabelCampo("Primer Apellido *"), gbc);
        txtPrimerApellido = crearCampoTexto();
        gbc.gridx = 1;
        datosPersonalesPanel.add(txtPrimerApellido, gbc);
        
        // Segundo Apellido
        gbc.gridx = 0;
        gbc.gridy = 3;
        datosPersonalesPanel.add(crearLabelCampo("Segundo Apellido"), gbc);
        txtSegundoApellido = crearCampoTexto();
        gbc.gridx = 1;
        datosPersonalesPanel.add(txtSegundoApellido, gbc);
        
        // Género
        gbc.gridx = 0;
        gbc.gridy = 4;
        datosPersonalesPanel.add(crearLabelCampo("Género *"), gbc);
        cmbGenero = new JComboBox<>(new String[]{"HOMBRE", "MUJER"});
        cmbGenero.setBackground(new Color(180, 210, 220));
        cmbGenero.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        cmbGenero.setPreferredSize(new Dimension(250, 40));
        gbc.gridx = 1;
        datosPersonalesPanel.add(cmbGenero, gbc);
        
        // Fecha Nacimiento
        gbc.gridx = 0;
        gbc.gridy = 5;
        datosPersonalesPanel.add(crearLabelCampo("Fecha Nacimiento (yyyy-MM-dd) *"), gbc);
        spinFechaNac = new JSpinner(new SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.YEAR));
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinFechaNac, "yyyy-MM-dd");
        spinFechaNac.setEditor(editor);
        spinFechaNac.setBackground(new Color(180, 210, 220));
        spinFechaNac.setPreferredSize(new Dimension(250, 40));
        gbc.gridx = 1;
        datosPersonalesPanel.add(spinFechaNac, gbc);
        
        // Teléfono
        gbc.gridx = 0;
        gbc.gridy = 6;
        datosPersonalesPanel.add(crearLabelCampo("Teléfono *"), gbc);
        txtTelefono = crearCampoTexto();
        gbc.gridx = 1;
        datosPersonalesPanel.add(txtTelefono, gbc);
        
        // DNI
        gbc.gridx = 0;
        gbc.gridy = 7;
        datosPersonalesPanel.add(crearLabelCampo("DNI *"), gbc);
        txtDni = crearCampoTexto();
        gbc.gridx = 1;
        datosPersonalesPanel.add(txtDni, gbc);
        
        // CORREO
        gbc.gridx = 0;
        gbc.gridy = 8;
        datosPersonalesPanel.add(crearLabelCampo("Correo Electrónico"), gbc);
        txtCorreo = crearCampoTexto();
        gbc.gridx = 1;
        datosPersonalesPanel.add(txtCorreo, gbc);
        
        centerPanel.add(datosPersonalesPanel);
        centerPanel.add(Box.createVerticalStrut(30));
        
        // ===== SECCIÓN DATOS DE USUARIO =====
        JLabel lblSeccionUsuario = crearLabelSeccion("DATOS DE ACCESO");
        centerPanel.add(lblSeccionUsuario);
        centerPanel.add(Box.createVerticalStrut(20));
        
        JPanel datosUsuarioPanel = new JPanel(new GridBagLayout());
        datosUsuarioPanel.setBackground(new Color(220, 220, 220));
        
        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        datosUsuarioPanel.add(crearLabelCampo("Nombre de Usuario *"), gbc);
        txtUsername = crearCampoTexto();
        gbc.gridx = 1;
        datosUsuarioPanel.add(txtUsername, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        datosUsuarioPanel.add(crearLabelCampo("Contraseña *"), gbc);
        txtPassword = new JPasswordField();
        txtPassword.setBackground(new Color(180, 210, 220));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        txtPassword.setPreferredSize(new Dimension(250, 40));
        gbc.gridx = 1;
        datosUsuarioPanel.add(txtPassword, gbc);
        
        // Rol
        gbc.gridx = 0;
        gbc.gridy = 2;
        datosUsuarioPanel.add(crearLabelCampo("Rol *"), gbc);
        cmbRol = new JComboBox<>(new String[]{"PACIENTE", "MEDICO_TERAPISTA", "AGENDADOR", "ADMINISTRADOR"});
        cmbRol.setBackground(new Color(180, 210, 220));
        cmbRol.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        cmbRol.setPreferredSize(new Dimension(250, 40));
        gbc.gridx = 1;
        datosUsuarioPanel.add(cmbRol, gbc);
        
        centerPanel.add(datosUsuarioPanel);
        centerPanel.add(Box.createVerticalStrut(40));
        
        // ===== Botones =====
        JButton btnGuardar = crearBoton("Registrarse");
        JButton btnCancelar = crearBoton("Cancelar");
        
        btnGuardar.addActionListener(e -> registrarUsuario());
        btnCancelar.addActionListener(e -> {
            new LoginFrame(usuarioService).setVisible(true);
            this.dispose();
        });
        
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        botonesPanel.setBackground(new Color(220, 220, 220));
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnCancelar);
        
        centerPanel.add(botonesPanel);
        centerPanel.add(Box.createVerticalStrut(40));
        
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setEnabled(false);
        
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setBackground(new Color(180, 210, 220));
        campo.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        campo.setPreferredSize(new Dimension(250, 40));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return campo;
    }
    
    private JLabel crearLabelSeccion(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setForeground(new Color(40, 170, 200));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
    
    private JLabel crearLabelCampo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(70, 170, 200));
        return label;
    }
    
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(70, 170, 200));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(200, 45));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
    
    private void registrarUsuario() {
        try {
            // Validaciones
            if (txtPrimerNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Primer nombre es obligatorio");
                return;
            }
            if (txtPrimerApellido.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Primer apellido es obligatorio");
                return;
            }
            if (txtTelefono.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Teléfono es obligatorio");
                return;
            }
            String dniText = txtDni.getText().trim();
            if (dniText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "DNI es obligatorio");
                return;
            }
            if (!dniText.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "DNI debe contener solo números");
                return;
            }
            if (txtUsername.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre de usuario es obligatorio");
                return;
            }
            if (new String(txtPassword.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Contraseña es obligatoria");
                return;
            }
            
            // Obtener datos
            String primerNombre = txtPrimerNombre.getText().trim();
            String segundoNombre = txtSegundoNombre.getText().trim();
            String primerApellido = txtPrimerApellido.getText().trim();
            String segundoApellido = txtSegundoApellido.getText().trim();
            String genero = (String) cmbGenero.getSelectedItem();
            LocalDate fechaNac = ((java.util.Date) spinFechaNac.getValue()).toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
            String telefono = txtTelefono.getText().trim();
            int dni = Integer.parseInt(dniText);
            String correo = txtCorreo.getText().trim();
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            String rol = (String) cmbRol.getSelectedItem();
            
            boolean creado = usuarioService.registrarUsuario(
                    username, password, mapearRol(rol),
                    primerNombre, segundoNombre, primerApellido, segundoApellido,
                    genero, fechaNac, telefono, dni, correo
            );
            
            if (creado) {
                JOptionPane.showMessageDialog(this, "Usuario registrado correctamente");
                new LoginFrame(usuarioService).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar el usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Rol mapearRol(String rolUI) {
        switch (rolUI) {
            case "PACIENTE": return Rol.PACIENTE;
            case "MEDICO_TERAPISTA": return Rol.MEDICO_TERAPISTA;
            case "AGENDADOR": return Rol.AGENDADOR;
            case "ADMINISTRADOR": return Rol.ADMINISTRADOR;
            default: return Rol.PACIENTE;
        }
    }
}