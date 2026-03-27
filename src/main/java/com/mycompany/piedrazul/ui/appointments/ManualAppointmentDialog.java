package com.mycompany.piedrazul.ui.appointments;

//import com.mycompany.piedrazul.domain.builder.AppointmentDirector;
//import com.mycompany.piedrazul.domain.builder.ManualAppointmentBuilder;
import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.service.AppointmentService;
import com.mycompany.piedrazul.domain.service.UsuarioService;
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

public class ManualAppointmentDialog extends JDialog {
    
    private Usuario usuarioActual;
    private UsuarioService usuarioService;
    private AppointmentService appointmentService;
    
    private JComboBox<Usuario> cmbPacientes;
    private JComboBox<Usuario> cmbMedicos;
    private JDateChooser dateChooser;
    private JComboBox<String> cmbHora;
    private JComboBox<String> cmbTipo;
    private JTextField txtMotivo;
    private JTextArea txtNotas;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public ManualAppointmentDialog(JFrame parent, Usuario usuarioActual) {
        super(parent, "Agendar Cita Manual", true);
        this.usuarioActual = usuarioActual;
        this.usuarioService = Piedrazul.getUsuarioService();
        this.appointmentService = new AppointmentService(new AppointmentRepositoryImpl(new UsuarioRepositoryImpl()));
        initComponents();
        cargarDatos();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(500, 600);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("AGENDAR CITA MANUAL", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(70, 170, 200));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(crearLabel("Paciente:"));
        cmbPacientes = new JComboBox<>();
        cmbPacientes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mainPanel.add(cmbPacientes);
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(crearLabel("Médico/Terapista:"));
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
        cmbTipo = new JComboBox<>(new String[]{"CONSULTA", "TERAPIA", "CONTROL", "URGENCIA"});
        cmbTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mainPanel.add(cmbTipo);
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(crearLabel("Motivo:"));
        txtMotivo = new JTextField();
        txtMotivo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mainPanel.add(txtMotivo);
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(crearLabel("Notas adicionales:"));
        txtNotas = new JTextArea(3, 20);
        txtNotas.setLineWrap(true);
        txtNotas.setWrapStyleWord(true);
        JScrollPane scrollNotas = new JScrollPane(txtNotas);
        scrollNotas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        scrollNotas.setPreferredSize(new Dimension(400, 80));
        mainPanel.add(scrollNotas);
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        btnGuardar = new JButton("Guardar Cita");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.setBackground(new Color(76, 175, 80));
        btnCancelar.setBackground(new Color(244, 67, 54));
        btnGuardar.setForeground(Color.WHITE);
        btnCancelar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnCancelar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.setPreferredSize(new Dimension(150, 40));

        //btnGuardar.addActionListener(e -> guardarCita());
        btnCancelar.addActionListener(e -> dispose());

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);

        mainPanel.add(buttonPanel);

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
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

    private void cargarDatos() {
        cmbPacientes.removeAllItems();
        cmbMedicos.removeAllItems();
    
        List<Usuario> pacientes = usuarioService.obtenerTodosLosPacientes();
        if (pacientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No hay pacientes registrados. Primero debe registrar pacientes.", 
                "Sin pacientes", 
                JOptionPane.WARNING_MESSAGE);
        } else {
            for (Usuario p : pacientes) {
                cmbPacientes.addItem(p);
            }
        }
    
        List<Usuario> medicos = usuarioService.obtenerTodosLosMedicos();
        if (medicos.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No hay médicos registrados. No se puede agendar citas sin médicos.", 
                "Sin médicos", 
                JOptionPane.WARNING_MESSAGE);
            btnGuardar.setEnabled(false);
        } else {
            for (Usuario m : medicos) {
                cmbMedicos.addItem(m);
            }
            btnGuardar.setEnabled(true);
        }
    }

    /*private void guardarCita() {
        try {
            if (cmbPacientes.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un paciente", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (cmbMedicos.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un médico", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (dateChooser.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una fecha", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtMotivo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el motivo de la cita", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date selectedDate = dateChooser.getDate();
            String hora = (String) cmbHora.getSelectedItem();
            LocalDateTime dateTime = LocalDateTime.ofInstant(
                selectedDate.toInstant(), 
                ZoneId.systemDefault()
            ).withHour(Integer.parseInt(hora.split(":")[0])).withMinute(0);

            AppointmentDirector director = new AppointmentDirector();
            ManualAppointmentBuilder builder = new ManualAppointmentBuilder();
            director.setAppointmentBuilder(builder);

            Appointment cita = director.buildManualAppointment(
                (Usuario) cmbPacientes.getSelectedItem(),
                (Usuario) cmbMedicos.getSelectedItem(),
                dateTime,
                (String) cmbTipo.getSelectedItem(),
                txtMotivo.getText().trim(),
                txtNotas.getText().trim(),
                usuarioActual
            );

            Appointment citaGuardada = appointmentService.crearCita(cita);
            
            JOptionPane.showMessageDialog(this, 
                "Cita agendada exitosamente.\nNúmero de cita: " + citaGuardada.getId(),
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al agendar cita: " + ex.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }*/
}