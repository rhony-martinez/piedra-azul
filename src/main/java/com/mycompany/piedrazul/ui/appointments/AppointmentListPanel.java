package com.mycompany.piedrazul.ui.appointments;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.service.AppointmentService;
import com.mycompany.piedrazul.infrastructure.persistence.AppointmentRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.MedicoRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.PacienteRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.UsuarioRepositoryImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentListPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private AppointmentService appointmentService;
    private boolean esHistorial;

    private JTextField txtMedico;
    private JTextField txtFecha;
    private JButton btnListar;

    public AppointmentListPanel(Usuario usuario, boolean esHistorial) {
        this.esHistorial = esHistorial;

        this.appointmentService = new AppointmentService(
                new AppointmentRepositoryImpl(
                        new UsuarioRepositoryImpl(),
                        new PacienteRepositoryImpl(),
                        new MedicoRepositoryImpl()));

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

        String[] columnas = { "ID", "Fecha", "Paciente", "Médico", "Tipo", "Estado", "Motivo" };
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

            //  FILTRO SEGÚN HISTORIAL O AGENDADAS
            if (!filtrarPorTipo(cita)) {
                continue;
            }

            String paciente = "N/A";
            if (cita.getPaciente() != null) {
                paciente = cita.getPaciente().getPrimerNombre() + " " +
                        cita.getPaciente().getPrimerApellido();
            }

            String nombreMedico = "N/A";
            if (cita.getMedico() != null) {
                nombreMedico = cita.getMedico().getPrimerNombre() + " " +
                        cita.getMedico().getPrimerApellido();
            }

            Object[] row = {
                    cita.getId(),
                    cita.getFechaHora().format(formatter),
                    paciente,
                    nombreMedico,
                    "-", // tipo (no lo estás manejando)
                    cita.getEstado(),
                    cita.getObservacion()
            };

            tableModel.addRow(row);
        }
    }

    //  ESTE MÉTODO ES CLAVE
    private boolean filtrarPorTipo(Appointment cita) {
        if (cita.getFechaHora() == null) {
            return false;
        }

        LocalDateTime ahora = LocalDateTime.now();

        if (esHistorial) {
            // historial → citas pasadas o completadas
            return cita.getFechaHora().isBefore(ahora) ||
                    cita.getEstado() == AppointmentStatus.ATENDIDA || 
                    cita.getEstado() == AppointmentStatus.CANCELADA ||
                    cita.getEstado() == AppointmentStatus.NO_ASISTIDA;
        } else {
            // agendadas → citas futuras
            return cita.getFechaHora().isAfter(ahora);
        }
    }

    

}