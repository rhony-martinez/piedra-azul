package com.mycompany.piedrazul.ui.appointments;

import com.mycompany.piedrazul.domain.model.*;
import com.mycompany.piedrazul.domain.repository.*;
import com.mycompany.piedrazul.domain.service.AppointmentService;
import com.mycompany.piedrazul.domain.service.NotificationService;
import com.mycompany.piedrazul.domain.service.facade.AppointmentFacade;
import com.mycompany.piedrazul.domain.service.scheduler.AppointmentScheduler;
import com.mycompany.piedrazul.domain.service.scheduler.ManualAppointmentScheduler;
import com.mycompany.piedrazul.domain.service.scheduler.SelfServiceAppointmentScheduler;
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

public class AgendarCitaDialog extends JFrame {

    private Usuario usuarioActual;
    private AppointmentService appointmentService;
    private NotificationService notificationService;
    private AppointmentFacade appointmentFacade;
    private IPacienteRepository pacienteRepository;
    private IMedicoRepository medicoRepository;
    private IPersonaRepository personaRepository;
    private INotificationRepository notificacionRepository;
    private JFrame parentFrame;

    // Selección de paciente
    private JComboBox<Paciente> cmbPacientes;
    private JButton btnNuevoPaciente;
    private JButton btnCancelarNuevo;

    // Datos del paciente
    private JTextField txtPrimerNombre;
    private JTextField txtSegundoNombre;
    private JTextField txtPrimerApellido;
    private JTextField txtSegundoApellido;
    private JTextField txtDniPaciente;
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

    private boolean modoNuevoPaciente = false;

    public AgendarCitaDialog(Usuario usuarioActual, JFrame parentFrame) {
        this.usuarioActual = usuarioActual;
        this.parentFrame = parentFrame;

        IUsuarioRepository usuarioRepo = new UsuarioRepositoryImpl();
        this.pacienteRepository = new PacienteRepositoryImpl();
        this.medicoRepository = new MedicoRepositoryImpl();
        this.personaRepository = new PersonaRepositoryImpl();
        this.notificacionRepository = new NotificationRepositoryImpl();

        IAppointmentRepository appointmentRepo = new AppointmentRepositoryImpl(
                usuarioRepo, pacienteRepository, medicoRepository);

        this.appointmentService = new AppointmentService(appointmentRepo);
        this.notificationService = new NotificationService(notificacionRepository);

        AppointmentScheduler manualScheduler = new ManualAppointmentScheduler(appointmentRepo);
        AppointmentScheduler selfServiceScheduler = new SelfServiceAppointmentScheduler(appointmentRepo);

        this.appointmentFacade = new AppointmentFacade(
                appointmentService,
                appointmentRepo,
                notificationService,
                usuarioRepo,
                manualScheduler,
                selfServiceScheduler);

        initComponents();
        cargarMedicos();
        cargarPacientes();

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

        // Barra superior
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(40, 170, 200));
        topBar.setPreferredSize(new Dimension(800, 80));
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JLabel lblTitulo = new JLabel("PIEDRAZUL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 32));
        lblTitulo.setForeground(Color.WHITE);

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

        // Panel central
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(220, 220, 220));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 150, 30, 150));

        JLabel lblSeccionTitulo = new JLabel("AGENDAR NUEVA CITA");
        lblSeccionTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblSeccionTitulo.setForeground(new Color(70, 170, 200));
        lblSeccionTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(lblSeccionTitulo);
        centerPanel.add(Box.createVerticalStrut(5));

        JLabel lblSubtitulo = new JLabel("Seleccione un paciente de la lista o registre uno nuevo");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(lblSubtitulo);
        centerPanel.add(Box.createVerticalStrut(40));

        // SECCIÓN: SELECCIONAR PACIENTE
        JPanel panelSeleccion = new JPanel(new BorderLayout());
        panelSeleccion.setBackground(Color.WHITE);
        panelSeleccion.setBorder(crearTitledBorder("SELECCIONAR PACIENTE", new Color(70, 170, 200)));

        JPanel seleccionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        seleccionPanel.setBackground(Color.WHITE);

        JLabel lblPaciente = new JLabel("Paciente:");
        lblPaciente.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPaciente.setForeground(new Color(70, 170, 200));

        cmbPacientes = new JComboBox<>();
        cmbPacientes.setPreferredSize(new Dimension(350, 40));
        cmbPacientes.setBackground(new Color(180, 210, 220));
        cmbPacientes.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        cmbPacientes.addActionListener(e -> {
            if (!modoNuevoPaciente && cmbPacientes.getSelectedItem() != null) {
                cargarDatosPaciente((Paciente) cmbPacientes.getSelectedItem());
            }
        });

        btnNuevoPaciente = new JButton("+ Nuevo Paciente");
        btnNuevoPaciente.setBackground(new Color(70, 170, 200));
        btnNuevoPaciente.setForeground(Color.WHITE);
        btnNuevoPaciente.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNuevoPaciente.setFocusPainted(false);
        btnNuevoPaciente.setPreferredSize(new Dimension(150, 40));
        btnNuevoPaciente.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevoPaciente.addActionListener(e -> activarModoNuevoPaciente());

        seleccionPanel.add(lblPaciente);
        seleccionPanel.add(cmbPacientes);
        seleccionPanel.add(btnNuevoPaciente);

        panelSeleccion.add(seleccionPanel, BorderLayout.NORTH);
        centerPanel.add(panelSeleccion);
        centerPanel.add(Box.createVerticalStrut(20));

        // SECCIÓN: DATOS DEL PACIENTE
        JPanel panelDatosPaciente = new JPanel();
        panelDatosPaciente.setLayout(new BoxLayout(panelDatosPaciente, BoxLayout.Y_AXIS));
        panelDatosPaciente.setBackground(Color.WHITE);
        panelDatosPaciente.setBorder(crearTitledBorder("DATOS DEL PACIENTE", new Color(70, 170, 200)));

        JPanel datosPacienteGrid = new JPanel(new GridBagLayout());
        datosPacienteGrid.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        datosPacienteGrid.add(crearLabelCampo("DNI *"), gbc);
        txtDniPaciente = crearCampoTexto();
        txtDniPaciente.setEnabled(false);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        datosPacienteGrid.add(txtDniPaciente, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Primer Nombre *"), gbc);
        txtPrimerNombre = crearCampoTexto();
        txtPrimerNombre.setEnabled(false);
        gbc.gridx = 1;
        datosPacienteGrid.add(txtPrimerNombre, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Segundo Nombre"), gbc);
        txtSegundoNombre = crearCampoTexto();
        txtSegundoNombre.setEnabled(false);
        gbc.gridx = 1;
        datosPacienteGrid.add(txtSegundoNombre, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Primer Apellido *"), gbc);
        txtPrimerApellido = crearCampoTexto();
        txtPrimerApellido.setEnabled(false);
        gbc.gridx = 1;
        datosPacienteGrid.add(txtPrimerApellido, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Segundo Apellido"), gbc);
        txtSegundoApellido = crearCampoTexto();
        txtSegundoApellido.setEnabled(false);
        gbc.gridx = 1;
        datosPacienteGrid.add(txtSegundoApellido, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Género *"), gbc);
        cmbGenero = new JComboBox<>(new String[] { "HOMBRE", "MUJER" });
        cmbGenero.setBackground(new Color(180, 210, 220));
        cmbGenero.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        cmbGenero.setPreferredSize(new Dimension(250, 40));
        cmbGenero.setEnabled(false);
        gbc.gridx = 1;
        datosPacienteGrid.add(cmbGenero, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Fecha Nacimiento (yyyy-MM-dd) *"), gbc);
        spinFechaNac = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.YEAR));
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinFechaNac, "yyyy-MM-dd");
        spinFechaNac.setEditor(editor);
        spinFechaNac.setBackground(new Color(180, 210, 220));
        spinFechaNac.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        spinFechaNac.setPreferredSize(new Dimension(250, 40));
        spinFechaNac.setEnabled(false);
        gbc.gridx = 1;
        datosPacienteGrid.add(spinFechaNac, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Teléfono *"), gbc);
        txtTelefono = crearCampoTexto();
        txtTelefono.setEnabled(false);
        gbc.gridx = 1;
        datosPacienteGrid.add(txtTelefono, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        datosPacienteGrid.add(crearLabelCampo("Correo Electrónico"), gbc);
        txtCorreo = crearCampoTexto();
        txtCorreo.setEnabled(false);
        gbc.gridx = 1;
        datosPacienteGrid.add(txtCorreo, gbc);

        panelDatosPaciente.add(datosPacienteGrid);

        JPanel btnCancelarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnCancelarPanel.setBackground(Color.WHITE);
        btnCancelarNuevo = new JButton("Cancelar Registro");
        btnCancelarNuevo.setBackground(new Color(150, 150, 150));
        btnCancelarNuevo.setForeground(Color.WHITE);
        btnCancelarNuevo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelarNuevo.setFocusPainted(false);
        btnCancelarNuevo.setPreferredSize(new Dimension(150, 35));
        btnCancelarNuevo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelarNuevo.addActionListener(e -> cancelarModoNuevoPaciente());
        btnCancelarNuevo.setVisible(false);
        btnCancelarPanel.add(btnCancelarNuevo);
        panelDatosPaciente.add(btnCancelarPanel);

        centerPanel.add(panelDatosPaciente);
        centerPanel.add(Box.createVerticalStrut(20));

        // SECCIÓN: DATOS DE LA CITA
        JPanel panelCita = new JPanel();
        panelCita.setLayout(new BoxLayout(panelCita, BoxLayout.Y_AXIS));
        panelCita.setBackground(Color.WHITE);
        panelCita.setBorder(crearTitledBorder("DATOS DE LA CITA", new Color(70, 170, 200)));

        JPanel citaGrid = new JPanel(new GridBagLayout());
        citaGrid.setBackground(Color.WHITE);

        GridBagConstraints gbcCita = new GridBagConstraints();
        gbcCita.insets = new Insets(8, 15, 8, 15);
        gbcCita.fill = GridBagConstraints.HORIZONTAL;

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

        // BOTONES
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

        desactivarModoNuevoPaciente();
    }

    private void cargarPacientes() {
        cmbPacientes.removeAllItems();
        List<Paciente> pacientes = pacienteRepository.findAll();

        if (pacientes.isEmpty()) {
            cmbPacientes.addItem(null);
        } else {
            for (Paciente p : pacientes) {
                cmbPacientes.addItem(p);
            }
        }
    }

    private void cargarDatosPaciente(Paciente paciente) {
        if (paciente == null)
            return;

        txtDniPaciente.setText(String.valueOf(paciente.getDni()));
        txtPrimerNombre.setText(paciente.getPrimerNombre());
        txtSegundoNombre.setText(paciente.getSegundoNombre() != null ? paciente.getSegundoNombre() : "");
        txtPrimerApellido.setText(paciente.getPrimerApellido());
        txtSegundoApellido.setText(paciente.getSegundoApellido() != null ? paciente.getSegundoApellido() : "");
        txtTelefono.setText(paciente.getTelefono());
        txtCorreo.setText(paciente.getCorreo() != null ? paciente.getCorreo() : "");

        if ("HOMBRE".equals(paciente.getGenero())) {
            cmbGenero.setSelectedIndex(0);
        } else {
            cmbGenero.setSelectedIndex(1);
        }

        if (paciente.getFechaNacimiento() != null) {
            Date fecha = Date.from(paciente.getFechaNacimiento()
                    .atStartOfDay(ZoneId.systemDefault()).toInstant());
            spinFechaNac.setValue(fecha);
        }
    }

    private void activarModoNuevoPaciente() {
        modoNuevoPaciente = true;
        cmbPacientes.setEnabled(false);
        btnNuevoPaciente.setEnabled(false);
        btnCancelarNuevo.setVisible(true);

        txtDniPaciente.setText("");
        txtPrimerNombre.setText("");
        txtSegundoNombre.setText("");
        txtPrimerApellido.setText("");
        txtSegundoApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        cmbGenero.setSelectedIndex(0);
        spinFechaNac.setValue(new Date());

        txtDniPaciente.setEnabled(true);
        txtPrimerNombre.setEnabled(true);
        txtSegundoNombre.setEnabled(true);
        txtPrimerApellido.setEnabled(true);
        txtSegundoApellido.setEnabled(true);
        cmbGenero.setEnabled(true);
        spinFechaNac.setEnabled(true);
        txtTelefono.setEnabled(true);
        txtCorreo.setEnabled(true);
    }

    private void cancelarModoNuevoPaciente() {
        modoNuevoPaciente = false;
        cmbPacientes.setEnabled(true);
        btnNuevoPaciente.setEnabled(true);
        btnCancelarNuevo.setVisible(false);

        txtDniPaciente.setEnabled(false);
        txtPrimerNombre.setEnabled(false);
        txtSegundoNombre.setEnabled(false);
        txtPrimerApellido.setEnabled(false);
        txtSegundoApellido.setEnabled(false);
        cmbGenero.setEnabled(false);
        spinFechaNac.setEnabled(false);
        txtTelefono.setEnabled(false);
        txtCorreo.setEnabled(false);

        if (cmbPacientes.getSelectedItem() != null) {
            cargarDatosPaciente((Paciente) cmbPacientes.getSelectedItem());
        } else {
            limpiarCamposPaciente();
        }
    }

    private void desactivarModoNuevoPaciente() {
        modoNuevoPaciente = false;
        cmbPacientes.setEnabled(true);
        btnNuevoPaciente.setEnabled(true);
        btnCancelarNuevo.setVisible(false);

        txtDniPaciente.setEnabled(false);
        txtPrimerNombre.setEnabled(false);
        txtSegundoNombre.setEnabled(false);
        txtPrimerApellido.setEnabled(false);
        txtSegundoApellido.setEnabled(false);
        cmbGenero.setEnabled(false);
        spinFechaNac.setEnabled(false);
        txtTelefono.setEnabled(false);
        txtCorreo.setEnabled(false);

        if (cmbPacientes.getSelectedItem() != null) {
            cargarDatosPaciente((Paciente) cmbPacientes.getSelectedItem());
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

    private TitledBorder crearTitledBorder(String titulo, Color color) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(color, 2),
                titulo,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                color);
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
        List<Medico> medicos = medicoRepository.findAllActivos();

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

    private void guardarCita() {
        try {
            Paciente paciente;

            if (modoNuevoPaciente) {
                if (txtDniPaciente.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "DNI es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (txtPrimerNombre.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Primer nombre es obligatorio", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (txtPrimerApellido.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Primer apellido es obligatorio", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (txtTelefono.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Teléfono es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int dni = Integer.parseInt(txtDniPaciente.getText().trim());

                Persona personaExistente = personaRepository.findByDni(dni);
                if (personaExistente != null) {
                    JOptionPane.showMessageDialog(this, "Ya existe un paciente con este DNI", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Persona persona = new Persona();
                persona.setPrimerNombre(txtPrimerNombre.getText().trim());
                persona.setSegundoNombre(
                        txtSegundoNombre.getText().trim().isEmpty() ? "" : txtSegundoNombre.getText().trim());
                persona.setPrimerApellido(txtPrimerApellido.getText().trim());
                persona.setSegundoApellido(
                        txtSegundoApellido.getText().trim().isEmpty() ? "" : txtSegundoApellido.getText().trim());
                persona.setGenero((String) cmbGenero.getSelectedItem());

                Date fechaNacDate = (Date) spinFechaNac.getValue();
                LocalDate fechaNac = fechaNacDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                persona.setFechaNacimiento(fechaNac);

                persona.setTelefono(txtTelefono.getText().trim());
                persona.setDni(dni);
                persona.setCorreo(txtCorreo.getText().trim().isEmpty() ? "" : txtCorreo.getText().trim());

                persona = personaRepository.create(persona);
                if (persona == null || persona.getId() == 0) {
                    JOptionPane.showMessageDialog(this, "Error al crear la persona", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean pacienteCreado = pacienteRepository.create(persona.getId());
                if (!pacienteCreado) {
                    JOptionPane.showMessageDialog(this, "Error al crear el paciente", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                paciente = pacienteRepository.findById(persona.getId());
                if (paciente == null) {
                    JOptionPane.showMessageDialog(this, "Error al obtener el paciente creado", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                cargarPacientes();

            } else {
                if (cmbPacientes.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Debe seleccionar un paciente", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                paciente = (Paciente) cmbPacientes.getSelectedItem();
            }

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
                    ZoneId.systemDefault()).withHour(Integer.parseInt(hora.split(":")[0]))
                    .withMinute(0);

            // Validar disponibilidad
            boolean disponible = appointmentService.verificarDisponibilidadMedico(medico.getId(), fechaHora);
            System.out.println("Disponible: " + disponible);

            if (!disponible) {
                JOptionPane.showMessageDialog(this,
                        "⚠️ Horario no disponible\n\n" +
                                "El médico " + medico.getPrimerNombre() + " " + medico.getPrimerApellido() +
                                " ya tiene una cita agendada para:\n" +
                                "📅 " + fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                                " a las " + fechaHora.format(DateTimeFormatter.ofPattern("HH:mm")) + ".\n\n" +
                                "Por favor, seleccione otra hora o fecha.",
                        "Horario no disponible",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            appointmentFacade.crearCitaManual(
                    paciente,
                    medico,
                    fechaHora,
                    usuarioActual,
                    txtObservacion.getText().trim());

            JOptionPane.showMessageDialog(this,
                    "Cita agendada correctamente para "
                            + fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
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