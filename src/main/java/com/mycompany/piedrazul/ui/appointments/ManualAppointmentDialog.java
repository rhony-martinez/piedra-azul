package com.mycompany.piedrazul.ui.appointments;

import com.mycompany.piedrazul.domain.builder.AppointmentDirector;
import com.mycompany.piedrazul.domain.builder.ManualAppointmentBuilder;
import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.Paciente;
import com.mycompany.piedrazul.domain.model.Persona;
import com.mycompany.piedrazul.domain.model.Rol;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;
import com.mycompany.piedrazul.domain.repository.IMedicoRepository;
import com.mycompany.piedrazul.domain.repository.IPacienteRepository;
import com.mycompany.piedrazul.domain.repository.IPersonaRepository;
import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
import com.mycompany.piedrazul.domain.service.AppointmentService;
import com.mycompany.piedrazul.domain.service.UsuarioService;
import com.mycompany.piedrazul.infrastructure.persistence.AppointmentRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.UsuarioRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.PacienteRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.MedicoRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.PersonaRepositoryImpl;
import com.mycompany.piedrazul.main.Piedrazul;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ManualAppointmentDialog extends JDialog {

    private Usuario usuarioActual;
    private UsuarioService usuarioService;
    private AppointmentService appointmentService;
    private IPacienteRepository pacienteRepository;
    private IMedicoRepository medicoRepository;
    private IPersonaRepository personaRepository;

    // Datos del Paciente
    private JTextField txtDni;
    private JTextField txtPrimerNombre;
    private JTextField txtSegundoNombre;
    private JTextField txtPrimerApellido;
    private JTextField txtSegundoApellido;
    private JComboBox<String> cmbGenero;
    private JDateChooser dateNacimiento;
    private JTextField txtTelefono;
    private JTextField txtCorreo;

    // Datos de Cita
    private JComboBox<Medico> cmbMedicos;
    private JDateChooser dateChooser;
    private JComboBox<String> cmbHora;
    private JTextArea txtObservacion;

    private JButton btnGuardar;
    private JButton btnCancelar;

    public ManualAppointmentDialog(JFrame parent, Usuario usuarioActual) {
        super(parent, "Agendar Cita Manual", true);
        this.usuarioActual = usuarioActual;
        this.usuarioService = Piedrazul.getUsuarioService();
        IUsuarioRepository usuarioRepo = new UsuarioRepositoryImpl();
        this.pacienteRepository = new PacienteRepositoryImpl();
        this.medicoRepository = new MedicoRepositoryImpl();
        this.personaRepository = new PersonaRepositoryImpl();

        IAppointmentRepository appointmentRepo = new AppointmentRepositoryImpl(usuarioRepo, pacienteRepository,
                medicoRepository);

        this.appointmentService = new AppointmentService(appointmentRepo);
        initComponents();
        cargarDatos();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {

        setSize(650, 700);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        // ===================== TÍTULO =====================
        JLabel lblTitulo = new JLabel("AGENDAMIENTO DE CITA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(70, 170, 200));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitulo);

        mainPanel.add(Box.createVerticalStrut(20));

        // ===================== DATOS PACIENTE =====================
        mainPanel.add(crearLabel("DATOS DEL PACIENTE"));

        txtDni = new JTextField();
        mainPanel.add(crearCampo("Identificación*", txtDni));

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarPacientePorDni());
        mainPanel.add(btnBuscar);

        txtPrimerNombre = new JTextField();
        txtSegundoNombre = new JTextField();
        txtPrimerApellido = new JTextField();
        txtSegundoApellido = new JTextField();

        mainPanel.add(crearCampo("Primer Nombre*", txtPrimerNombre));
        mainPanel.add(crearCampo("Segundo Nombre", txtSegundoNombre));
        mainPanel.add(crearCampo("Primer Apellido*", txtPrimerApellido));
        mainPanel.add(crearCampo("Segundo Apellido", txtSegundoApellido));

        cmbGenero = new JComboBox<>(new String[] { "HOMBRE", "MUJER", "OTRO" });
        mainPanel.add(crearCampo("Género*", cmbGenero));

        dateNacimiento = new JDateChooser();
        dateNacimiento.setDateFormatString("dd/MM/yyyy");
        mainPanel.add(crearCampo("Fecha Nacimiento*", dateNacimiento));

        txtTelefono = new JTextField();
        txtCorreo = new JTextField();

        mainPanel.add(crearCampo("Celular*", txtTelefono));
        mainPanel.add(crearCampo("Correo", txtCorreo));

        mainPanel.add(Box.createVerticalStrut(20));

        // ===================== DATOS CITA =====================
        mainPanel.add(crearLabel("DATOS DE LA CITA"));

        cmbMedicos = new JComboBox<>();
        mainPanel.add(crearCampo("Médico/Terapista*", cmbMedicos));

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setMinSelectableDate(new Date());
        mainPanel.add(crearCampo("Fecha*", dateChooser));

        cmbHora = new JComboBox<>(generarHoras());
        mainPanel.add(crearCampo("Hora*", cmbHora));

        txtObservacion = new JTextArea(3, 20);
        txtObservacion.setLineWrap(true);
        txtObservacion.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(txtObservacion);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        mainPanel.add(crearCampo("Observación", scroll));

        mainPanel.add(Box.createVerticalStrut(20));

        // ===================== BOTONES =====================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        btnGuardar = new JButton("Agendar");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.setBackground(new Color(70, 170, 200));
        btnGuardar.setForeground(Color.WHITE);

        btnCancelar.setBackground(new Color(244, 67, 54));
        btnCancelar.setForeground(Color.WHITE);

        btnGuardar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.setPreferredSize(new Dimension(150, 40));

        btnGuardar.addActionListener(e -> guardarCita());
        btnCancelar.addActionListener(e -> dispose());

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);

        mainPanel.add(buttonPanel);

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
    }

    private JPanel crearCampo(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(70, 170, 200));

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        panel.add(label);
        panel.add(field);
        panel.add(Box.createVerticalStrut(10));

        return panel;
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(70, 170, 200));
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

    // En el método cargarDatos(), agrega IDs a los usuarios:
    private void cargarDatos() {

        // SOLO médicos (pacientes ya no van en combo)
        cmbMedicos.removeAllItems();

        List<Medico> medicos = medicoRepository.findAll();

        if (medicos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay médicos registrados. No se puede agendar citas.",
                    "Sin médicos",
                    JOptionPane.WARNING_MESSAGE);

            btnGuardar.setEnabled(false);
        } else {
            for (Medico m : medicos) {
                cmbMedicos.addItem(m);
            }
            btnGuardar.setEnabled(true);
        }

        // Inicializar formulario paciente limpio
        limpiarCamposPaciente();
        bloquearCamposPaciente(false);
    }

    private void guardarCita() {
        try {

            int dni = Integer.parseInt(txtDni.getText().trim());

            Persona persona = personaRepository.findByDni(dni);
            Paciente paciente;

            // CASO 1: NO EXISTE
            if (persona == null) {

                persona = new Persona();
                persona.setPrimerNombre(txtPrimerNombre.getText());
                persona.setSegundoNombre(txtSegundoNombre.getText());
                persona.setPrimerApellido(txtPrimerApellido.getText());
                persona.setSegundoApellido(txtSegundoApellido.getText());
                persona.setGenero((String) cmbGenero.getSelectedItem());
                persona.setFechaNacimiento(dateNacimiento.getDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate());
                persona.setTelefono(txtTelefono.getText());
                persona.setCorreo(txtCorreo.getText());
                persona.setDni(dni);

                persona = personaRepository.create(persona);

                // crear paciente
                pacienteRepository.create(persona.getId());

                paciente = pacienteRepository.findById(persona.getId());

            } else {
                // YA EXISTE
                paciente = pacienteRepository.findById(persona.getId());

                // si no existe como paciente → crearlo
                if (paciente == null) {
                    pacienteRepository.create(persona.getId());
                    paciente = pacienteRepository.findById(persona.getId());
                }
            }

            // DATOS CITA
            Medico medico = (Medico) cmbMedicos.getSelectedItem();

            Date selectedDate = dateChooser.getDate();
            String hora = (String) cmbHora.getSelectedItem();

            LocalDateTime fechaHora = LocalDateTime.ofInstant(
                    selectedDate.toInstant(),
                    ZoneId.systemDefault()).withHour(Integer.parseInt(hora.split(":")[0]))
                    .withMinute(0);

            Appointment cita = appointmentService.crearCitaManual(
                    paciente,
                    medico,
                    fechaHora,
                    usuarioActual,
                    txtObservacion.getText());

            JOptionPane.showMessageDialog(this, "Cita agendada correctamente");

            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPacientePorDni() {
        try {
            int dni = Integer.parseInt(txtDni.getText().trim());

            Persona persona = personaRepository.findByDni(dni);

            if (persona != null) {
                // AUTOCOMPLETAR
                txtPrimerNombre.setText(persona.getPrimerNombre());
                txtSegundoNombre.setText(persona.getSegundoNombre());
                txtPrimerApellido.setText(persona.getPrimerApellido());
                txtSegundoApellido.setText(persona.getSegundoApellido());
                txtTelefono.setText(persona.getTelefono());
                txtCorreo.setText(persona.getCorreo());
                cmbGenero.setSelectedItem(persona.getGenero());
                dateNacimiento.setDate(
                        java.util.Date.from(
                                persona.getFechaNacimiento()
                                        .atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()));

                // Bloquear edición (opcional)
                bloquearCamposPaciente(true);

            } else {
                limpiarCamposPaciente();
                bloquearCamposPaciente(false);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DNI inválido");
        }
    }

    private void limpiarCamposPaciente() {
        txtPrimerNombre.setText("");
        txtSegundoNombre.setText("");
        txtPrimerApellido.setText("");
        txtSegundoApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        cmbGenero.setSelectedIndex(0);
        dateNacimiento.setDate(null);
    }

    private void bloquearCamposPaciente(boolean bloquear) {

        txtPrimerNombre.setEnabled(!bloquear);
        txtSegundoNombre.setEnabled(!bloquear);
        txtPrimerApellido.setEnabled(!bloquear);
        txtSegundoApellido.setEnabled(!bloquear);
        txtTelefono.setEnabled(!bloquear);
        txtCorreo.setEnabled(!bloquear);
        cmbGenero.setEnabled(!bloquear);
        dateNacimiento.setEnabled(!bloquear);
    }
}