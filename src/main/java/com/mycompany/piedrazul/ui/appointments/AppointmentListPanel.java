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
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentListPanel extends JPanel {
    
    private Usuario usuario;
    private boolean historial;
    private JTable table;
    private DefaultTableModel tableModel;
    private AppointmentService appointmentService;
    private JButton btnReprogramar;
    private JButton btnCancelar;
    private JButton btnConfirmar;

    public AppointmentListPanel(Usuario usuario, boolean historial) {
        this.usuario = usuario;
        this.historial = historial;
        this.appointmentService = new AppointmentService(new AppointmentRepositoryImpl(new UsuarioRepositoryImpl()));
        initComponents();
        cargarCitas();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(700, 400));

        // Título
        String titulo = historial ? "HISTORIAL DE CITAS" : "MIS CITAS";
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(70, 170, 200));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Columnas de la tabla - CORREGIDO
        String[] columnas = {"ID", "Fecha", "Paciente", "Médico", "Tipo", "Estado", "Motivo"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Ajustar ancho de columnas
        table.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(120);  // Fecha
        table.getColumnModel().getColumn(2).setPreferredWidth(150);  // Paciente
        table.getColumnModel().getColumn(3).setPreferredWidth(150);  // Médico
        table.getColumnModel().getColumn(4).setPreferredWidth(80);   // Tipo
        table.getColumnModel().getColumn(5).setPreferredWidth(100);  // Estado
        table.getColumnModel().getColumn(6).setPreferredWidth(200);  // Motivo
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 170, 200)));
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones (solo para citas activas)
        if (!historial) {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            buttonPanel.setBackground(Color.WHITE);

            btnReprogramar = new JButton("Reprogramar");
            btnCancelar = new JButton("Cancelar Cita");
            btnConfirmar = new JButton("Confirmar Cita");

            btnReprogramar.setBackground(new Color(255, 152, 0));
            btnCancelar.setBackground(new Color(244, 67, 54));
            btnConfirmar.setBackground(new Color(76, 175, 80));
            
            btnReprogramar.setForeground(Color.WHITE);
            btnCancelar.setForeground(Color.WHITE);
            btnConfirmar.setForeground(Color.WHITE);

            btnReprogramar.setFocusPainted(false);
            btnCancelar.setFocusPainted(false);
            btnConfirmar.setFocusPainted(false);

            btnReprogramar.addActionListener(e -> reprogramarCita());
            btnCancelar.addActionListener(e -> cancelarCita());
            btnConfirmar.addActionListener(e -> confirmarCita());

            buttonPanel.add(btnReprogramar);
            buttonPanel.add(btnConfirmar);
            buttonPanel.add(btnCancelar);
            
            add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    private void cargarCitas() {
        tableModel.setRowCount(0);
        
        List<Appointment> citas;
        if (historial) {
            citas = appointmentService.obtenerHistorial(usuario);
        } else {
            citas = appointmentService.obtenerProximasCitas(usuario);
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Appointment cita : citas) {
            String estadoEnEspanol = traducirEstado(cita.getStatus());
            
            Object[] row = {
                cita.getId(),
                cita.getDateTime().format(formatter),
                cita.getPatient() != null ? cita.getPatient().getNombreCompleto() : "N/A",
                cita.getProfessional() != null ? cita.getProfessional().getNombreCompleto() : "N/A", // <-- ESTO ES LO IMPORTANTE
                cita.getAppointmentType(),
                estadoEnEspanol,  // <-- ESTADO TRADUCIDO
                cita.getReason() != null ? cita.getReason() : ""
            };
            tableModel.addRow(row);
        }
    }
    
    private String traducirEstado(AppointmentStatus status) {
        if (status == null) return "DESCONOCIDO";
        
        switch (status) {
            case SCHEDULED: return "AGENDADA";
            case CONFIRMED: return "CONFIRMADA";
            case IN_PROGRESS: return "EN CURSO";
            case COMPLETED: return "COMPLETADA";
            case CANCELLED: return "CANCELADA";
            case RESCHEDULED: return "REPROGRAMADA";
            case NO_SHOW: return "NO ASISTIÓ";
            default: return status.name();
        }
    }

    private void reprogramarCita() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una cita para reprogramar", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        
        Appointment citaOriginal = appointmentService.obtenerProximasCitas(usuario)
                .stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
        
        if (citaOriginal == null) {
            JOptionPane.showMessageDialog(this, 
                "No se pudo encontrar la cita", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        RescheduleAppointmentDialog dialog = new RescheduleAppointmentDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            citaOriginal,
            usuario
        );
        dialog.setVisible(true);
        
        // Recargar citas después de reprogramar
        cargarCitas();
    }

    private void cancelarCita() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una cita para cancelar", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de cancelar esta cita?",
            "Confirmar cancelación",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean cancelada = appointmentService.cancelarCita(id);
            if (cancelada) {
                JOptionPane.showMessageDialog(this,
                    "Cita cancelada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                cargarCitas(); // Recargar la tabla
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al cancelar la cita",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void confirmarCita() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, 
            "Seleccione una cita para confirmar", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    int id = (int) tableModel.getValueAt(selectedRow, 0);
    String estadoActual = (String) tableModel.getValueAt(selectedRow, 5);
    
    // Validar que no esté ya confirmada
    if ("CONFIRMADA".equals(estadoActual)) {
        JOptionPane.showMessageDialog(this,
            "La cita ya está confirmada",
            "Información",
            JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    
    int confirm = JOptionPane.showConfirmDialog(this,
        "¿Confirmar esta cita?",
        "Confirmar",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        boolean confirmada = appointmentService.confirmarCita(id);
        if (confirmada) {
            JOptionPane.showMessageDialog(this,
                "Cita confirmada exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            cargarCitas(); // Recargar la tabla
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al confirmar la cita",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
}