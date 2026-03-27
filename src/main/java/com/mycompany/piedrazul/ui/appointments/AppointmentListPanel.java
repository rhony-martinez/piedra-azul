package com.mycompany.piedrazul.ui.appointments;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.service.AppointmentService;
import com.mycompany.piedrazul.infrastructure.persistence.AppointmentRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.UsuarioRepositoryImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentListPanel extends JPanel {

    private Usuario usuario;
    private JTable table;
    private DefaultTableModel tableModel;
    private AppointmentService appointmentService;
    private boolean esHistorial;

    private JTextField txtMedico;
    private JTextField txtFecha;
    private JButton btnListar;

    public AppointmentListPanel(Usuario usuario, boolean esHistorial) {
        this.usuario = usuario;
        this.esHistorial = esHistorial;

        this.appointmentService = new AppointmentService(
                new AppointmentRepositoryImpl(new UsuarioRepositoryImpl())
        );

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelFiltros = new JPanel();
        txtMedico = new JTextField(10);
        txtFecha = new JTextField(10);
        btnListar = new JButton("LISTAR");

        panelFiltros.add(new JLabel("Médico:"));
        panelFiltros.add(txtMedico);
        panelFiltros.add(new JLabel("Fecha (YYYY-MM-DD):"));
        panelFiltros.add(txtFecha);
        panelFiltros.add(btnListar);

        add(panelFiltros, BorderLayout.NORTH);

        String titulo = esHistorial ? "HISTORIAL DE CITAS" : "CITAS AGENDADAS";
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.SOUTH);

        String[] columnas = {"ID", "Fecha", "Paciente", "Médico", "Tipo", "Estado", "Motivo"};
        tableModel = new DefaultTableModel(columnas, 0);
        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);

        btnListar.addActionListener(e -> listarPorFiltro());
    }

    private void listarPorFiltro() {
        String medico = txtMedico.getText();
        String fecha = txtFecha.getText();

        tableModel.setRowCount(0);

        if (fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar una fecha");
            return;
        }

        List<Appointment> citas = appointmentService.listarPorMedicoYFecha(medico, fecha);

        if (citas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay citas registradas para el médico y fecha seleccionados");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Appointment cita : citas) {

            // 🔥 FILTRO SEGÚN HISTORIAL O AGENDADAS
            if (!filtrarPorTipo(cita)) {
                continue;
            }

            String paciente = "N/A";
            if (cita.getPatient() != null) {
                paciente = cita.getPatient().getNombreCompleto();
            }

            String nombreMedico = "N/A";
            if (cita.getProfessional() != null) {
                nombreMedico = cita.getProfessional().getNombreCompleto();
            }

            Object[] row = {
                cita.getId(),
                cita.getDateTime().format(formatter),
                paciente,
                nombreMedico,
                cita.getAppointmentType(),
                traducirEstado(cita.getStatus()),
                cita.getReason()
            };

            tableModel.addRow(row);
        }
    }

    // 🔥 ESTE MÉTODO ES CLAVE
    private boolean filtrarPorTipo(Appointment cita) {
        if (cita.getDateTime() == null) {
            return false;
        }

        LocalDateTime ahora = LocalDateTime.now();

        if (esHistorial) {
            // historial → citas pasadas o completadas
            return cita.getDateTime().isBefore(ahora) ||
                    cita.getStatus() == AppointmentStatus.COMPLETED;
        } else {
            // agendadas → citas futuras
            return cita.getDateTime().isAfter(ahora);
        }
    }

    private String traducirEstado(AppointmentStatus status) {
        if (status == null) {
            return "DESCONOCIDO";
        }

        switch (status) {
            case SCHEDULED:
                return "AGENDADA";
            case CONFIRMED:
                return "CONFIRMADA";
            case IN_PROGRESS:
                return "EN CURSO";
            case COMPLETED:
                return "COMPLETADA";
            case CANCELLED:
                return "CANCELADA";
            case RESCHEDULED:
                return "REPROGRAMADA";
            case NO_SHOW:
                return "NO ASISTIÓ";
            default:
                return status.name();
        }
    }
}