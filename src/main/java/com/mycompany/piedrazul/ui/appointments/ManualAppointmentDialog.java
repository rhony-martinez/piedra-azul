package com.mycompany.piedrazul.ui.appointments;

import com.mycompany.piedrazul.domain.model.*;
import com.mycompany.piedrazul.domain.repository.*;
import com.mycompany.piedrazul.domain.service.AppointmentService;
import com.mycompany.piedrazul.infrastructure.persistence.*;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class ManualAppointmentDialog extends JFrame {

    private Usuario usuarioActual;
    private AppointmentService appointmentService;
    private IPacienteRepository pacienteRepository;
    private IMedicoRepository medicoRepository;
    private IPersonaRepository personaRepository;
    private JFrame parentFrame;

    // Panel de búsqueda de paciente
    private JTextField txtDni;
    private JButton btnBuscar;

    // Datos del paciente
    private JTextField txtPrimerNombre;
    private JTextField txtSegundoNombre;
    private JTextField txtPrimerApellido;
    private JTextField txtSegundoApellido;
    private JTextField txtDniPaciente;  // Campo DNI para registro
    private JComboBox<String> cmbGenero;
    private JSpinner spinFechaNac;
    private JTextField txtTelefono;
    private JTextField txtCorreo;

    // Datos de la cita
    private JComboBox<Medico> cmbMedicos;
    private JDateChooser dateChooser;
    private JComboBox<String> cmbHora;
    private JTextArea txtObservacion;

    private JButton btnGuardar;
    private JButton btnCancelar;

    // Variables de estado
    private boolean pacienteExistente = false;
    private int pacienteId = -1;

    public ManualAppointmentDialog(Usuario usuarioActual, JFrame parentFrame) {
        this.usuarioActual = usuarioActual;
        this.parentFrame = parentFrame;
        
        // Inicializar repositorios y servicios
        IUsuarioRepository usuarioRepo = new UsuarioRepositoryImpl();
        this.pacienteRepository = new PacienteRepositoryImpl();
        this.medicoRepository = new MedicoRepositoryImpl();
        this.personaRepository = new PersonaRepositoryImpl();
        
        IAppointmentRepository appointmentRepo = new AppointmentRepositoryImpl(
            usuarioRepo, pacienteRepository, medicoRepository
        );
        this.appointmentService = new AppointmentService(appointmentRepo);
        
        initComponents();
        cargarMedicos();
        
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("PIEDRAZUL - Agendar Cita");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(220, 220, 220));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(220, 220, 220));

        // ==============================
        // Barra superior turquesa con logo y botón volver
        // ==============================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(40, 170, 200));
        topBar.setPreferredSize(new Dimension(800, 80));
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JLabel lblTitulo = new JLabel("PIEDRAZUL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 32));
        lblTitulo.setForeground(Color.WHITE);
        
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
            parentFrame.setVisible(true);
            dispose();
        });
        
        topBar.add(lblTitulo, BorderLayout.WEST);
        topBar.add(btnVolver, BorderLayout.EAST);
        
        // ==============================
        // Panel central con scroll
        // ==============================
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(220, 220, 220));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 150, 30, 150));

        // Título
        JLabel lblSeccionTitulo = new JLabel("AGENDAR NUEVA CITA");
        lblSeccionTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblSeccionTitulo.setForeground(new Color(70, 170, 200));
        lblSeccionTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(lblSeccionTitulo);
        centerPanel.add(Box.createVerticalStrut(5));
        
        JLabel lblSubtitulo = new JLabel("Complete los siguientes datos para agendar una cita");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(lblSubtitulo);
        centerPanel.add(Box.createVerticalStrut(40));

        // ===================== SECCIÓN: BUSCAR PACIENTE =====================
        JPanel panelBuscar = new JPanel(new BorderLayout());
        panelBuscar.setBackground(Color.WHITE);
        panelBuscar.setBorder(crearTitledBorder("BUSCAR PACIENTE", new Color(70, 170, 200)));
        
        JPanel busquedaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        busquedaPanel.setBackground(Color.WHITE);
        
        JLabel lblDni = new JLabel("DNI del paciente:");
        lblDni.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDni.setForeground(new Color(70, 170, 200));
        
        txtDni = new JTextField(15);
        txtDni.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDni.setPreferredSize(new Dimension(200, 40));
        txtDni.setBackground(new Color(180, 210, 220));
        txtDni.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(70, 170, 200));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setPreferredSize(new Dimension(100, 40));
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnBuscar.addActionListener(e -> buscarPaciente());
        
        busquedaPanel.add(lblDni);
        busquedaPanel.add(txtDni);
        busquedaPanel.add(btnBuscar);
        
        panelBuscar.add(busquedaPanel, BorderLayout.NORTH);
        centerPanel.add(panelBuscar);
        centerPanel.add(Box.createVerticalStrut(20));

        // ===================== SECCIÓN: DATOS DEL PACIENTE =====================
        JPanel panelDatosPaciente = new JPanel();
        panelDatosPaciente.setLayout(new BoxLayout(panelDatosPaciente, BoxLayout.Y_AXIS));
        panelDatosPaciente.setBackground(Color.WHITE);
        panelDatosPaciente.setBorder(crearTitledBorder("DATOS DEL PACIENTE", new Color(70, 170, 200)));
        
        // Panel de dos columnas para datos del paciente
        JPanel datosPacienteGrid = new JPanel(new GridBagLayout());
        datosPacienteGrid.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // DNI (para registro)
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        datosPacienteGrid.add(crearLabelCampo("DNI *"), gbc);
        txtDniPaciente = crearCampoTexto();
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        datosPacienteGrid.add(txtDniPaciente, gbc);
        row++;
        
        // Primer Nombre
        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Primer Nombre *"), gbc);
        txtPrimerNombre = crearCampoTexto();
        gbc.gridx = 1;
        datosPacienteGrid.add(txtPrimerNombre, gbc);
        row++;
        
        // Segundo Nombre
        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Segundo Nombre"), gbc);
        txtSegundoNombre = crearCampoTexto();
        gbc.gridx = 1;
        datosPacienteGrid.add(txtSegundoNombre, gbc);
        row++;
        
        // Primer Apellido
        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Primer Apellido *"), gbc);
        txtPrimerApellido = crearCampoTexto();
        gbc.gridx = 1;
        datosPacienteGrid.add(txtPrimerApellido, gbc);
        row++;
        
        // Segundo Apellido
        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Segundo Apellido"), gbc);
        txtSegundoApellido = crearCampoTexto();
        gbc.gridx = 1;
        datosPacienteGrid.add(txtSegundoApellido, gbc);
        row++;
        
        // Género
        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Género *"), gbc);
        cmbGenero = new JComboBox<>(new String[]{"HOMBRE", "MUJER"});
        cmbGenero.setBackground(new Color(180, 210, 220));
        cmbGenero.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        cmbGenero.setPreferredSize(new Dimension(250, 40));
        gbc.gridx = 1;
        datosPacienteGrid.add(cmbGenero, gbc);
        row++;
        
        // Fecha Nacimiento
        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Fecha Nacimiento (yyyy-MM-dd) *"), gbc);
        spinFechaNac = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.YEAR));
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinFechaNac, "yyyy-MM-dd");
        spinFechaNac.setEditor(editor);
        spinFechaNac.setBackground(new Color(180, 210, 220));
        spinFechaNac.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        spinFechaNac.setPreferredSize(new Dimension(250, 40));
        gbc.gridx = 1;
        datosPacienteGrid.add(spinFechaNac, gbc);
        row++;
        
        // Teléfono
        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Teléfono *"), gbc);
        txtTelefono = crearCampoTexto();
        gbc.gridx = 1;
        datosPacienteGrid.add(txtTelefono, gbc);
        row++;
        
        // Correo
        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Correo Electrónico"), gbc);
        txtCorreo = crearCampoTexto();
        gbc.gridx = 1;
        datosPacienteGrid.add(txtCorreo, gbc);
        
        panelDatosPaciente.add(datosPacienteGrid);
        centerPanel.add(panelDatosPaciente);
        centerPanel.add(Box.createVerticalStrut(20));

        // ===================== SECCIÓN: DATOS DE LA CITA =====================
        JPanel panelCita = new JPanel();
        panelCita.setLayout(new BoxLayout(panelCita, BoxLayout.Y_AXIS));
        panelCita.setBackground(Color.WHITE);
        panelCita.setBorder(crearTitledBorder("DATOS DE LA CITA", new Color(70, 170, 200)));
        
        JPanel citaGrid = new JPanel(new GridBagLayout());
        citaGrid.setBackground(Color.WHITE);
        
        GridBagConstraints gbcCita = new GridBagConstraints();
        gbcCita.insets = new Insets(8, 15, 8, 15);
        gbcCita.fill = GridBagConstraints.HORIZONTAL;
        
        // Médico
        gbcCita.gridx = 0;
        gbcCita.gridy = 0;
        gbcCita.weightx = 0.2;
        citaGrid.add(crearLabelCampo("Médico/Terapista *"), gbcCita);
        cmbMedicos = new JComboBox<>();
        cmbMedicos.setPreferredSize(new Dimension(250, 40));
        cmbMedicos.setBackground(new Color(180, 210, 220));
        cmbMedicos.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        gbcCita.gridx = 1;
        gbcCita.weightx = 0.8;
        citaGrid.add(cmbMedicos, gbcCita);
        
        // Fecha
        gbcCita.gridx = 0;
        gbcCita.gridy = 1;
        gbcCita.weightx = 0.2;
        citaGrid.add(crearLabelCampo("Fecha *"), gbcCita);
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setMinSelectableDate(new Date());
        dateChooser.setPreferredSize(new Dimension(200, 40));
        gbcCita.gridx = 1;
        gbcCita.weightx = 0.3;
        citaGrid.add(dateChooser, gbcCita);
        
        // Hora
        gbcCita.gridx = 2;
        gbcCita.weightx = 0.2;
        citaGrid.add(crearLabelCampo("Hora *"), gbcCita);
        cmbHora = new JComboBox<>(generarHoras());
        cmbHora.setPreferredSize(new Dimension(120, 40));
        cmbHora.setBackground(new Color(180, 210, 220));
        cmbHora.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        gbcCita.gridx = 3;
        gbcCita.weightx = 0.3;
        citaGrid.add(cmbHora, gbcCita);
        
        // Observación
        gbcCita.gridx = 0;
        gbcCita.gridy = 2;
        gbcCita.weightx = 0.2;
        citaGrid.add(crearLabelCampo("Observación"), gbcCita);
        txtObservacion = new JTextArea(3, 30);
        txtObservacion.setLineWrap(true);
        txtObservacion.setWrapStyleWord(true);
        txtObservacion.setBackground(new Color(180, 210, 220));
        txtObservacion.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        txtObservacion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane scrollObs = new JScrollPane(txtObservacion);
        scrollObs.setPreferredSize(new Dimension(400, 70));
        gbcCita.gridx = 1;
        gbcCita.gridwidth = 3;
        citaGrid.add(scrollObs, gbcCita);
        
        panelCita.add(citaGrid);
        centerPanel.add(panelCita);
        centerPanel.add(Box.createVerticalStrut(30));

        // ===================== BOTONES =====================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setBackground(new Color(220, 220, 220));
        
        btnGuardar = new JButton("Agendar Cita");
        btnGuardar.setBackground(new Color(70, 170, 200));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(200, 45));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(244, 67, 54));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(150, 45));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnGuardar.addActionListener(e -> guardarCita());
        btnCancelar.addActionListener(e -> {
            parentFrame.setVisible(true);
            dispose();
        });
        
        // Hover effects
        btnGuardar.addChangeListener(e -> {
            if (btnGuardar.getModel().isRollover()) {
                btnGuardar.setBackground(new Color(50, 140, 170));
            } else {
                btnGuardar.setBackground(new Color(70, 170, 200));
            }
        });
        
        btnCancelar.addChangeListener(e -> {
            if (btnCancelar.getModel().isRollover()) {
                btnCancelar.setBackground(new Color(200, 50, 40));
            } else {
                btnCancelar.setBackground(new Color(244, 67, 54));
            }
        });
        
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        centerPanel.add(buttonPanel);
        centerPanel.add(Box.createVerticalStrut(40));
        
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setEnabled(false);
        
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private TitledBorder crearTitledBorder(String titulo, Color color) {
        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(color, 2),
            titulo,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            color
        );
        border.setTitleColor(color);
        return border;
    }
    
    private JLabel crearLabelCampo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(70, 170, 200));
        return label;
    }
    
    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setBackground(new Color(180, 210, 220));
        campo.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        campo.setPreferredSize(new Dimension(250, 40));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return campo;
    }
    
    private String[] generarHoras() {
        String[] horas = new String[13];
        for (int i = 0; i < 13; i++) {
            int hora = 8 + i;
            horas[i] = String.format("%02d:00", hora);
        }
        return horas;
    }
    
    private void cargarMedicos() {
        cmbMedicos.removeAllItems();
        List<Medico> medicos = medicoRepository.findAll();
        
        if (medicos.isEmpty()) {
            cmbMedicos.addItem(null);
            btnGuardar.setEnabled(false);
            JOptionPane.showMessageDialog(this,
                "No hay médicos registrados. No se puede agendar citas.",
                "Sin médicos",
                JOptionPane.WARNING_MESSAGE);
        } else {
            for (Medico m : medicos) {
                cmbMedicos.addItem(m);
            }
            btnGuardar.setEnabled(true);
        }
    }
    
    private void buscarPaciente() {
        try {
            String dniText = txtDni.getText().trim();
            if (dniText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un DNI para buscar", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            int dni = Integer.parseInt(dniText);
            System.out.println("Buscando DNI: " + dni);
            
            Persona persona = personaRepository.findByDni(dni);
            
            if (persona != null) {
                System.out.println("Persona encontrada: " + persona.getPrimerNombre());
                System.out.println("PrimerNombre: " + persona.getPrimerNombre());
                System.out.println("PrimerApellido: " + persona.getPrimerApellido());
                System.out.println("Telefono: " + persona.getTelefono());
                
                pacienteExistente = true;
                pacienteId = persona.getId();
                
                // LLENAR CAMPOS
                txtDniPaciente.setText(String.valueOf(persona.getDni()));
                txtPrimerNombre.setText(persona.getPrimerNombre());
                txtSegundoNombre.setText(persona.getSegundoNombre());
                txtPrimerApellido.setText(persona.getPrimerApellido());
                txtSegundoApellido.setText(persona.getSegundoApellido());
                txtTelefono.setText(persona.getTelefono());
                txtCorreo.setText(persona.getCorreo());
                
                // Género
                if ("HOMBRE".equals(persona.getGenero())) {
                    cmbGenero.setSelectedIndex(0);
                } else if ("MUJER".equals(persona.getGenero())) {
                    cmbGenero.setSelectedIndex(1);
                }
                
                // Fecha de nacimiento
                if (persona.getFechaNacimiento() != null) {
                    LocalDate fechaLocal = persona.getFechaNacimiento();
                    Date fecha = Date.from(fechaLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    spinFechaNac.setValue(fecha);
                    System.out.println("Fecha configurada: " + fecha);
                }
                
                // Mostrar mensaje con los datos cargados
                JOptionPane.showMessageDialog(this, 
                    "Paciente encontrado:\n" +
                    "Nombre: " + persona.getPrimerNombre() + " " + persona.getPrimerApellido() + "\n" +
                    "DNI: " + persona.getDni() + "\n" +
                    "Teléfono: " + persona.getTelefono(),
                    "Paciente encontrado",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } else {
                System.out.println("Persona NO encontrada para DNI: " + dni);
                
                pacienteExistente = false;
                pacienteId = -1;
                limpiarCamposPaciente();
                
                JOptionPane.showMessageDialog(this, 
                    "Paciente no encontrado. Complete los datos para registrarlo.",
                    "Nuevo paciente",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "DNI inválido. Ingrese solo números.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void limpiarCamposPaciente() {
        txtDniPaciente.setText("");
        txtPrimerNombre.setText("");
        txtSegundoNombre.setText("");
        txtPrimerApellido.setText("");
        txtSegundoApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        cmbGenero.setSelectedIndex(0);
        spinFechaNac.setValue(new Date());
    }
    
    private void guardarCita() {
        try {
            String dniText = txtDni.getText().trim();
            if (dniText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el DNI del paciente", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int dni = Integer.parseInt(dniText);
            
            Paciente paciente;
            
            if (pacienteExistente) {
                // Primero obtenemos la persona
                Persona persona = personaRepository.findByDni(dni);
                if (persona == null) {
                    JOptionPane.showMessageDialog(this, "Error: No se encontró la persona con DNI: " + dni, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                System.out.println("Persona encontrada - ID: " + persona.getId());
                
                // Buscar si existe como paciente
                paciente = pacienteRepository.findById(persona.getId());
                
                if (paciente == null) {
                    System.out.println("Creando registro de paciente para persona ID: " + persona.getId());
                    boolean creado = pacienteRepository.create(persona.getId());
                    if (!creado) {
                        JOptionPane.showMessageDialog(this, "Error al crear el registro de paciente", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    paciente = pacienteRepository.findById(persona.getId());
                    if (paciente == null) {
                        JOptionPane.showMessageDialog(this, "Error al obtener el paciente creado", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    System.out.println("Paciente creado correctamente con ID: " + paciente.getId());
                } else {
                    System.out.println("Paciente encontrado con ID: " + paciente.getId());
                }
            } else {
                // Validar datos del nuevo paciente
                if (txtDniPaciente.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "DNI del paciente es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (txtPrimerNombre.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Primer nombre del paciente es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (txtPrimerApellido.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Primer apellido del paciente es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (txtTelefono.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Teléfono del paciente es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int dniNuevo = Integer.parseInt(txtDniPaciente.getText().trim());
                
                // Verificar si ya existe una persona con ese DNI
                Persona personaExistente = personaRepository.findByDni(dniNuevo);
                if (personaExistente != null) {
                    JOptionPane.showMessageDialog(this, "Ya existe una persona con este DNI. Por favor, busque primero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                System.out.println("Creando nueva persona para DNI: " + dniNuevo);
                
                // Crear nueva persona - ASEGURANDO QUE NO HAY NULLS
                Persona persona = new Persona();
                persona.setPrimerNombre(txtPrimerNombre.getText().trim());
                
                // Segundo nombre: si está vacío, poner string vacío en lugar de null
                String segundoNombre = txtSegundoNombre.getText().trim();
                persona.setSegundoNombre(segundoNombre.isEmpty() ? "" : segundoNombre);
                
                persona.setPrimerApellido(txtPrimerApellido.getText().trim());
                
                // Segundo apellido: si está vacío, poner string vacío en lugar de null
                String segundoApellido = txtSegundoApellido.getText().trim();
                persona.setSegundoApellido(segundoApellido.isEmpty() ? "" : segundoApellido);
                
                persona.setGenero((String) cmbGenero.getSelectedItem());
                
                Date fechaNacDate = (Date) spinFechaNac.getValue();
                LocalDate fechaNac = fechaNacDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                persona.setFechaNacimiento(fechaNac);
                
                persona.setTelefono(txtTelefono.getText().trim());
                persona.setDni(dniNuevo);
                
                // Correo: si está vacío, poner string vacío en lugar de null
                String correo = txtCorreo.getText().trim();
                persona.setCorreo(correo.isEmpty() ? "" : correo);
                
                System.out.println("Datos de persona a crear:");
                System.out.println("  DNI: " + persona.getDni());
                System.out.println("  PrimerNombre: " + persona.getPrimerNombre());
                System.out.println("  SegundoNombre: '" + persona.getSegundoNombre() + "'");
                System.out.println("  PrimerApellido: " + persona.getPrimerApellido());
                System.out.println("  SegundoApellido: '" + persona.getSegundoApellido() + "'");
                System.out.println("  Teléfono: " + persona.getTelefono());
                System.out.println("  Correo: '" + persona.getCorreo() + "'");
                
                try {
                    persona = personaRepository.create(persona);
                    System.out.println("Persona creada - ID: " + (persona != null ? persona.getId() : "null"));
                } catch (Exception ex) {
                    System.out.println("Excepción al crear persona: " + ex.getMessage());
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error en base de datos al crear persona: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (persona == null || persona.getId() == 0) {
                    JOptionPane.showMessageDialog(this, "Error al crear la persona (ID no generado)", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                System.out.println("Creando registro de paciente para persona ID: " + persona.getId());
                boolean pacienteCreado = pacienteRepository.create(persona.getId());
                if (!pacienteCreado) {
                    JOptionPane.showMessageDialog(this, "Error al crear el paciente", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                paciente = pacienteRepository.findById(persona.getId());
                if (paciente == null) {
                    JOptionPane.showMessageDialog(this, "Error al obtener el paciente creado", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                System.out.println("Paciente creado correctamente con ID: " + paciente.getId());
            }
            
            // Validar datos de la cita
            Medico medico = (Medico) cmbMedicos.getSelectedItem();
            if (medico == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un médico", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Date selectedDate = dateChooser.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String hora = (String) cmbHora.getSelectedItem();
            if (hora == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una hora", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            LocalDateTime fechaHora = LocalDateTime.ofInstant(
                selectedDate.toInstant(),
                ZoneId.systemDefault()
            ).withHour(Integer.parseInt(hora.split(":")[0]))
             .withMinute(0);
            
            System.out.println("Creando cita para paciente ID: " + paciente.getId() + ", médico: " + medico.getPrimerNombre());
            
            // Crear la cita
            Appointment cita = appointmentService.crearCitaManual(
                paciente,
                medico,
                fechaHora,
                usuarioActual,
                txtObservacion.getText().trim()
            );
            
            JOptionPane.showMessageDialog(this, 
                "Cita agendada correctamente para " + fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            
            parentFrame.setVisible(true);
            dispose();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "DNI inválido", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}