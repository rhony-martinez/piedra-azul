package com.mycompany.piedrazul.ui.appointments;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.Paciente;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;
import com.mycompany.piedrazul.domain.repository.IMedicoRepository;
import com.mycompany.piedrazul.domain.repository.IPacienteRepository;
import com.mycompany.piedrazul.domain.repository.IPersonaRepository;
import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
import com.mycompany.piedrazul.domain.service.AppointmentService;
import com.mycompany.piedrazul.infrastructure.persistence.AppointmentRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.MedicoRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.PacienteRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.PersonaRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.UsuarioRepositoryImpl;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class SelfServiceAppointmentDialog extends JFrame {

    private Usuario usuarioActual;
    private AppointmentService appointmentService;
    private IPacienteRepository pacienteRepository;
    private IMedicoRepository medicoRepository;
    private IPersonaRepository personaRepository;
    private JFrame parentFrame;

    private JComboBox<Medico> cmbMedicos;
    private JDateChooser dateChooser;
    private JComboBox<String> cmbHora;
    private JTextField txtMotivo;
    private JButton btnAgendar;
    private JButton btnCancelar;

    public SelfServiceAppointmentDialog(JFrame parentFrame, Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.parentFrame = parentFrame;

        // Inicializar repositorios y servicios
        IUsuarioRepository usuarioRepo = new UsuarioRepositoryImpl();
        this.pacienteRepository = new PacienteRepositoryImpl();
        this.medicoRepository = new MedicoRepositoryImpl();
        this.personaRepository = new PersonaRepositoryImpl();

        IAppointmentRepository appointmentRepo = new AppointmentRepositoryImpl(
                usuarioRepo, pacienteRepository, medicoRepository);
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

        // ===================== FORMULARIO =====================

        JPanel panelCita = new JPanel();
        panelCita.setLayout(new BoxLayout(panelCita, BoxLayout.Y_AXIS));
        panelCita.setBackground(Color.WHITE);
        panelCita.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Médico
        panelCita.add(crearLabel("Médico:"));
        cmbMedicos = new JComboBox<>();
        cmbMedicos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelCita.add(cmbMedicos);

        panelCita.add(Box.createVerticalStrut(15));

        // Fecha
        panelCita.add(crearLabel("Fecha:"));
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setMinSelectableDate(new Date());
        dateChooser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelCita.add(dateChooser);

        panelCita.add(Box.createVerticalStrut(15));

        // Hora
        panelCita.add(crearLabel("Hora:"));
        cmbHora = new JComboBox<>(generarHoras());
        cmbHora.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelCita.add(cmbHora);

        panelCita.add(Box.createVerticalStrut(15));

        panelCita.add(Box.createVerticalStrut(15));

        // Motivo
        panelCita.add(crearLabel("Motivo:"));
        txtMotivo = new JTextField();
        txtMotivo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelCita.add(txtMotivo);

        panelCita.add(Box.createVerticalStrut(20));

        // BOTONES
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        btnAgendar = new JButton("Agendar");
        btnCancelar = new JButton("Cancelar");

        btnAgendar.addActionListener(e -> agendarCita());
        btnCancelar.addActionListener(e -> {
            parentFrame.setVisible(true);
            dispose();
        });

        buttonPanel.add(btnAgendar);
        buttonPanel.add(btnCancelar);

        panelCita.add(buttonPanel);

        // agregar al center
        centerPanel.add(panelCita);

        // SCROLL
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(255, 152, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private String[] generarHoras() {
        String[] horas = new String[11];
        for (int i = 0; i < 11; i++) {
            int hora = 8 + i;
            horas[i] = String.format("%02d:00", hora);
        }
        return horas;
    }

    private void cargarMedicos() {
        cmbMedicos.removeAllItems();

        List<Medico> medicos = medicoRepository.findAllActivos();

        if (medicos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay médicos disponibles en este momento.\nNo puede agendar citas.",
                    "Sin médicos",
                    JOptionPane.ERROR_MESSAGE);

            btnAgendar.setEnabled(false);
            return;
        }

        for (Medico m : medicos) {
            cmbMedicos.addItem(m);
        }

        System.out.println("Médicos cargados: " + medicos.size());

        btnAgendar.setEnabled(true);
    }

    private void agendarCita() {
        try {
            if (cmbMedicos.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un médico",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (dateChooser.getDate() == null) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione una fecha",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date selectedDate = dateChooser.getDate();
            String hora = (String) cmbHora.getSelectedItem();

            LocalDateTime dateTime = LocalDateTime.ofInstant(
                    selectedDate.toInstant(),
                    ZoneId.systemDefault()).withHour(Integer.parseInt(hora.split(":")[0]))
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);

            // Convertir Usuario → Paciente
            Paciente paciente = pacienteRepository.findById(
                    usuarioActual.getPersonaId());

            Medico medico = (Medico) cmbMedicos.getSelectedItem();

            if (paciente == null) {
                throw new IllegalStateException("El usuario no está registrado como paciente");
            }

            if (medico == null) {
                throw new IllegalStateException("No se encontró el médico");
            }

            // Llamada correcta
            Appointment citaGuardada = appointmentService.crearCitaAutonoma(
                    paciente,
                    medico,
                    dateTime,
                    usuarioActual,
                    txtMotivo.getText().trim());

            JOptionPane.showMessageDialog(this,
                    "Cita agendada exitosamente el día " + dateTime.toLocalDate() +
                            " a la hora " + dateTime.toLocalTime() +
                            " con el médico " + cmbMedicos.getSelectedItem(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (IllegalStateException ex) {
            // errores de negocio (Template Method)
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.WARNING_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}