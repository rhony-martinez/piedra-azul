package com.mycompany.piedrazul.ui.appointments;

import com.mycompany.piedrazul.domain.builder.AppointmentDirector;
import com.mycompany.piedrazul.domain.builder.SelfServiceAppointmentBuilder;
import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.Paciente;
import com.mycompany.piedrazul.domain.model.Rol;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IMedicoRepository;
import com.mycompany.piedrazul.domain.repository.IPacienteRepository;
import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
import com.mycompany.piedrazul.domain.service.UsuarioService;
import com.mycompany.piedrazul.domain.service.AppointmentService;
import com.mycompany.piedrazul.infrastructure.persistence.AppointmentRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.UsuarioRepositoryImpl;
import com.mycompany.piedrazul.main.Piedrazul;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class SelfServiceAppointmentDialog extends JDialog {

    private Usuario pacienteActual;
    private AppointmentService appointmentService;
    private UsuarioService usuarioService;
    private IPacienteRepository pacienteRepository;
    private IMedicoRepository medicoRepository;
    private IUsuarioRepository usuarioRepository;

    private JComboBox<Usuario> cmbMedicos;
    private JDateChooser dateChooser;
    private JComboBox<String> cmbHora;
    private JComboBox<String> cmbTipo;
    private JTextField txtMotivo;
    private JButton btnAgendar;
    private JButton btnCancelar;

    public SelfServiceAppointmentDialog(JFrame parent, Usuario pacienteActual) {
        super(parent, "Agendar Cita - Autoservicio", true);
        this.pacienteActual = pacienteActual;
        this.appointmentService = new AppointmentService(
                new AppointmentRepositoryImpl(usuarioRepository, pacienteRepository, medicoRepository));
        this.usuarioService = Piedrazul.getUsuarioService();
        initComponents();
        cargarMedicos();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(450, 550);
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);
        JLabel lblTitulo = new JLabel("AGENDAR CITA - AUTOSERVICIO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(255, 152, 0));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createVerticalStrut(20));
        JPanel infoPaciente = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPaciente.setBackground(Color.WHITE);
        infoPaciente.add(new JLabel("Paciente: " + pacienteActual));
        mainPanel.add(infoPaciente);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(crearLabel("Seleccione médico:"));
        cmbMedicos = new JComboBox<>();
        cmbMedicos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mainPanel.add(cmbMedicos);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(crearLabel("Fecha:"));
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        dateChooser.setMinSelectableDate(new Date());
        mainPanel.add(dateChooser);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(crearLabel("Hora:"));
        cmbHora = new JComboBox<>(generarHoras());
        cmbHora.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mainPanel.add(cmbHora);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(crearLabel("Tipo de cita:"));
        cmbTipo = new JComboBox<>(new String[] { "CONSULTA", "CONTROL" });
        cmbTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mainPanel.add(cmbTipo);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(crearLabel("Motivo (opcional):"));
        txtMotivo = new JTextField();
        txtMotivo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mainPanel.add(txtMotivo);
        mainPanel.add(Box.createVerticalStrut(20));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        btnAgendar = new JButton("Agendar Cita");
        btnCancelar = new JButton("Cancelar");
        btnAgendar.setBackground(new Color(255, 152, 0));
        btnCancelar.setBackground(new Color(244, 67, 54));
        btnAgendar.setForeground(Color.WHITE);
        btnCancelar.setForeground(Color.WHITE);
        btnAgendar.setFocusPainted(false);
        btnCancelar.setFocusPainted(false);
        btnAgendar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.setPreferredSize(new Dimension(150, 40));
        btnAgendar.addActionListener(e -> agendarCita());
        btnCancelar.addActionListener(e -> dispose());
        buttonPanel.add(btnAgendar);
        buttonPanel.add(btnCancelar);
        mainPanel.add(buttonPanel);
        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
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
        List<Usuario> medicos = usuarioService.obtenerTodosLosMedicos();
        if (medicos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay médicos disponibles en este momento.\nNo puede agendar citas.",
                    "Sin médicos",
                    JOptionPane.ERROR_MESSAGE);
            btnAgendar.setEnabled(false);
            return;
        }
        for (Usuario m : medicos) {
            cmbMedicos.addItem(m);
        }
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
                    pacienteActual.getId());

            // Convertir Usuario → Medico
            Usuario medicoSeleccionado = (Usuario) cmbMedicos.getSelectedItem();

            Medico medico = medicoRepository.findById(
                    medicoSeleccionado.getId());

            if (paciente == null) {
                throw new IllegalStateException("No se encontró el paciente");
            }

            if (medico == null) {
                throw new IllegalStateException("No se encontró el médico");
            }

            // Llamada correcta
            Appointment citaGuardada = appointmentService.crearCitaAutonoma(
                    paciente,
                    medico,
                    dateTime,
                    pacienteActual,
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