package com.mycompany.piedrazul.ui.appointments;

import com.mycompany.piedrazul.domain.builder.AppointmentDirector;
import com.mycompany.piedrazul.domain.builder.RescheduledAppointmentBuilder;
import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.Rol;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.service.AppointmentService;
import com.mycompany.piedrazul.infrastructure.persistence.AppointmentRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.UsuarioRepositoryImpl;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class RescheduleAppointmentDialog extends JDialog {
    
    private Appointment citaOriginal;
    private Usuario usuarioActual;
    private AppointmentService appointmentService;
    
    private JDateChooser dateChooser;
    private JComboBox<String> cmbHora;
    private JComboBox<Usuario> cmbMedicos;
    private JButton btnReprogramar;
    private JButton btnCancelar;

    public RescheduleAppointmentDialog(JFrame parent, Appointment citaOriginal, Usuario usuarioActual) {
        super(parent, "Reprogramar Cita", true);
        this.citaOriginal = citaOriginal;
        this.usuarioActual = usuarioActual;
        this.appointmentService = new AppointmentService(new AppointmentRepositoryImpl(new UsuarioRepositoryImpl()));
        initComponents();
        cargarMedicos();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(400, 450);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("REPROGRAMAR CITA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(255, 152, 0));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createVerticalStrut(20));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        JLabel lblInfo = new JLabel("<html>Fecha original: " + 
            citaOriginal.getDateTime().format(formatter) + 
            "<br>Médico: " + citaOriginal.getProfessional().getNombreCompleto() +
            "<br>Paciente: " + citaOriginal.getPatient().getNombreCompleto() +
            "</html>");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mainPanel.add(lblInfo);
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(crearLabel("Cambiar médico (opcional):"));
        cmbMedicos = new JComboBox<>();
        cmbMedicos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mainPanel.add(cmbMedicos);
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(crearLabel("Nueva fecha:"));
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        dateChooser.setMinSelectableDate(new Date());
        mainPanel.add(dateChooser);
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(crearLabel("Nueva hora:"));
        cmbHora = new JComboBox<>(generarHoras());
        cmbHora.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mainPanel.add(cmbHora);
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        btnReprogramar = new JButton("Reprogramar");
        btnCancelar = new JButton("Cancelar");

        btnReprogramar.setBackground(new Color(255, 152, 0));
        btnCancelar.setBackground(new Color(244, 67, 54));
        btnReprogramar.setForeground(Color.WHITE);
        btnCancelar.setForeground(Color.WHITE);
        btnReprogramar.setFocusPainted(false);
        btnCancelar.setFocusPainted(false);
        btnReprogramar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.setPreferredSize(new Dimension(150, 40));

        btnReprogramar.addActionListener(e -> reprogramar());
        btnCancelar.addActionListener(e -> dispose());

        buttonPanel.add(btnReprogramar);
        buttonPanel.add(btnCancelar);
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);
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
        cmbMedicos.addItem(citaOriginal.getProfessional());
        
        Usuario medico2 = new Usuario();
        medico2.setId(4);
        medico2.setNombreCompleto("Dr. Carlos Ruiz");
        medico2.setRol(Rol.MEDICO_TERAPISTA);
        cmbMedicos.addItem(medico2);
    }

    private void reprogramar() {
        try {
            if (dateChooser.getDate() == null) {
                JOptionPane.showMessageDialog(this, 
                    "Seleccione la nueva fecha", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date selectedDate = dateChooser.getDate();
            String hora = (String) cmbHora.getSelectedItem();
            LocalDateTime newDateTime = LocalDateTime.ofInstant(
                selectedDate.toInstant(), 
                ZoneId.systemDefault()
            ).withHour(Integer.parseInt(hora.split(":")[0])).withMinute(0);

            AppointmentDirector director = new AppointmentDirector();
            RescheduledAppointmentBuilder builder = new RescheduledAppointmentBuilder(citaOriginal);
            director.setAppointmentBuilder(builder);

            Appointment nuevaCita;
            if (cmbMedicos.getSelectedItem() != citaOriginal.getProfessional()) {
                nuevaCita = director.buildRescheduledAppointment(
                    citaOriginal,
                    (Usuario) cmbMedicos.getSelectedItem(),
                    newDateTime,
                    usuarioActual
                );
            } else {
                nuevaCita = director.buildRescheduledAppointmentSameProfessional(
                    citaOriginal,
                    newDateTime,
                    usuarioActual
                );
            }

            Appointment citaGuardada = appointmentService.crearCita(nuevaCita);

            JOptionPane.showMessageDialog(this, 
                "Cita reprogramada exitosamente.\nNuevo número de cita: " + citaGuardada.getId(),
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al reprogramar: " + ex.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}