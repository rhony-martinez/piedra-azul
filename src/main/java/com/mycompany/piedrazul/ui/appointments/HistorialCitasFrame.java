package com.mycompany.piedrazul.ui.appointments;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IMedicoRepository;
import com.mycompany.piedrazul.domain.service.AppointmentService;
import com.mycompany.piedrazul.infrastructure.persistence.AppointmentRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.UsuarioRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.PacienteRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.MedicoRepositoryImpl;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistorialCitasFrame extends JFrame {

    private Usuario usuarioActual;
    private AppointmentService appointmentService;
    private IMedicoRepository medicoRepository;
    private JTable table;
    private DefaultTableModel tableModel;
    private JFrame parentFrame;
    private JLabel lblTotalCitas;

    // Componentes de búsqueda
    private JComboBox<Medico> cmbMedicos;
    private JDateChooser dateChooser;
    private JButton btnBuscar;
    private JButton btnLimpiar;

    public HistorialCitasFrame(Usuario usuarioActual, JFrame parentFrame) {
        this.usuarioActual = usuarioActual;
        this.parentFrame = parentFrame;
        
        // Inicializar servicios y repositorios
        this.appointmentService = new AppointmentService(
            new AppointmentRepositoryImpl(
                new UsuarioRepositoryImpl(),
                new PacienteRepositoryImpl(),
                new MedicoRepositoryImpl()
            )
        );
        this.medicoRepository = new MedicoRepositoryImpl();
        
        initComponents();
        cargarMedicos();
        
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("PIEDRAZUL - Buscador de Citas");
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
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(220, 220, 220));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Panel de búsqueda
        JPanel busquedaPanel = new JPanel();
        busquedaPanel.setBackground(Color.WHITE);
        busquedaPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 170, 200), 2),
            "BUSCAR CITAS",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(70, 170, 200)
        ));
        busquedaPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        JLabel lblMedico = new JLabel("Médico/Terapista:");
        lblMedico.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMedico.setForeground(new Color(70, 170, 200));
        
        cmbMedicos = new JComboBox<>();
        cmbMedicos.setPreferredSize(new Dimension(250, 35));
        cmbMedicos.setBackground(new Color(180, 210, 220));
        cmbMedicos.setBorder(BorderFactory.createLineBorder(new Color(40, 170, 200), 2));
        
        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFecha.setForeground(new Color(70, 170, 200));
        
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setPreferredSize(new Dimension(150, 35));
        dateChooser.getCalendarButton().setBackground(new Color(70, 170, 200));
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(70, 170, 200));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setPreferredSize(new Dimension(120, 40));
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(e -> buscarCitas());
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBackground(new Color(150, 150, 150));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setPreferredSize(new Dimension(120, 40));
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(e -> limpiarBusqueda());
        
        busquedaPanel.add(lblMedico);
        busquedaPanel.add(cmbMedicos);
        busquedaPanel.add(lblFecha);
        busquedaPanel.add(dateChooser);
        busquedaPanel.add(btnBuscar);
        busquedaPanel.add(btnLimpiar);
        
        centerPanel.add(busquedaPanel, BorderLayout.NORTH);
        
        // Panel de resultados
        JPanel resultadosPanel = new JPanel(new BorderLayout());
        resultadosPanel.setBackground(Color.WHITE);
        resultadosPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 170, 200), 2),
            "RESULTADOS",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(70, 170, 200)
        ));
        
        String[] columnas = {"ID", "Hora", "Paciente", "DNI", "Teléfono", "Estado", "Observación"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(70, 170, 200));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionBackground(new Color(180, 210, 220));
        
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(250);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 170, 200), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        resultadosPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblTotalCitas = new JLabel("Total de citas: 0");
        lblTotalCitas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalCitas.setForeground(new Color(70, 170, 200));
        totalPanel.add(lblTotalCitas);
        
        resultadosPanel.add(totalPanel, BorderLayout.SOUTH);
        
        centerPanel.add(resultadosPanel, BorderLayout.CENTER);
        
        // Panel inferior
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(220, 220, 220));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 40));
        
        JLabel lblMenuPrincipal = new JLabel("Historial de Citas Por medico");
        lblMenuPrincipal.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblMenuPrincipal.setForeground(new Color(70, 170, 200));
        
        bottomPanel.add(lblMenuPrincipal, BorderLayout.EAST);
        
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void cargarMedicos() {
        cmbMedicos.removeAllItems();
        List<Medico> medicos = medicoRepository.findAll();
        
        Medico todos = new Medico();
        todos.setId(0);
        todos.setPrimerNombre("TODOS");
        todos.setPrimerApellido("LOS MÉDICOS");
        cmbMedicos.addItem(todos);
        
        for (Medico m : medicos) {
            cmbMedicos.addItem(m);
        }
        
        cmbMedicos.setSelectedIndex(0);
    }
    
    private void buscarCitas() {
        tableModel.setRowCount(0);
        
        Medico medicoSeleccionado = (Medico) cmbMedicos.getSelectedItem();
        Date fechaSeleccionada = dateChooser.getDate();
        
        System.out.println("=== BUSCAR CITAS ===");
        System.out.println("Médico seleccionado: " + (medicoSeleccionado != null ? medicoSeleccionado.getPrimerNombre() : "null"));
        System.out.println("Fecha seleccionada: " + fechaSeleccionada);
        
        if (medicoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un médico", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (fechaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una fecha", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        LocalDate fecha = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int medicoId = medicoSeleccionado.getId();
        
        System.out.println("Fecha LocalDate: " + fecha);
        System.out.println("Médico ID: " + medicoId);
        
        try {
            // Obtener todas las citas
            List<Appointment> todasLasCitas = appointmentService.obtenerTodasLasCitas();
            
            System.out.println("Total de citas en BD: " + (todasLasCitas != null ? todasLasCitas.size() : 0));
            
            if (todasLasCitas == null || todasLasCitas.isEmpty()) {
                System.out.println("No hay citas en la base de datos");
                lblTotalCitas.setText("Total de citas: 0");
                tableModel.addRow(new Object[]{"", "No hay citas registradas", "", "", "", "", ""});
                return;
            }
            
            // Mostrar todas las citas para debug
            System.out.println("=== LISTADO DE TODAS LAS CITAS ===");
            for (Appointment c : todasLasCitas) {
                System.out.println("Cita ID: " + c.getId() + 
                                   ", Fecha: " + c.getFechaHora() +
                                   ", Médico ID: " + (c.getMedico() != null ? c.getMedico().getId() : "null") +
                                   ", Médico Nombre: " + (c.getMedico() != null ? c.getMedico().getPrimerNombre() : "null") +
                                   ", Paciente: " + (c.getPaciente() != null ? c.getPaciente().getPrimerNombre() : "null"));
            }
            
            List<Appointment> citasFiltradas = new ArrayList<>();
            
            for (Appointment cita : todasLasCitas) {
                if (cita.getFechaHora() == null) {
                    System.out.println("Cita " + cita.getId() + " tiene fecha null");
                    continue;
                }
                
                LocalDate fechaCita = cita.getFechaHora().toLocalDate();
                boolean coincideFecha = fechaCita.equals(fecha);
                
                boolean coincideMedico = (medicoId == 0) || 
                                         (cita.getMedico() != null && cita.getMedico().getId() == medicoId);
                
                System.out.println("Cita " + cita.getId() + 
                                   " - Fecha cita: " + fechaCita + 
                                   " - Coincide fecha: " + coincideFecha +
                                   " - Coincide médico: " + coincideMedico);
                
                if (coincideFecha && coincideMedico) {
                    citasFiltradas.add(cita);
                    System.out.println(">>> CITA AGREGADA: " + cita.getId());
                }
            }
            
            System.out.println("Citas filtradas: " + citasFiltradas.size());
            
            citasFiltradas.sort((a, b) -> a.getFechaHora().compareTo(b.getFechaHora()));
            
            DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");
            
            for (Appointment cita : citasFiltradas) {
                String estadoEnEspanol = traducirEstado(cita.getEstado());
                
                String nombrePaciente = "N/A";
                String dniPaciente = "";
                String telefonoPaciente = "";
                
                if (cita.getPaciente() != null) {
                    nombrePaciente = (cita.getPaciente().getPrimerNombre() != null ? cita.getPaciente().getPrimerNombre() : "") + " " + 
                                    (cita.getPaciente().getPrimerApellido() != null ? cita.getPaciente().getPrimerApellido() : "");
                    dniPaciente = String.valueOf(cita.getPaciente().getDni());
                    telefonoPaciente = cita.getPaciente().getTelefono() != null ? cita.getPaciente().getTelefono() : "";
                }
                
                Object[] row = {
                    cita.getId(),
                    cita.getFechaHora() != null ? cita.getFechaHora().format(horaFormatter) : "N/A",
                    nombrePaciente,
                    dniPaciente,
                    telefonoPaciente,
                    estadoEnEspanol,
                    cita.getObservacion() != null ? cita.getObservacion() : ""
                };
                tableModel.addRow(row);
            }
            
            lblTotalCitas.setText("Total de citas: " + citasFiltradas.size());
            
            if (citasFiltradas.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No se encontraron citas para " + 
                    (medicoId == 0 ? "todos los médicos" : medicoSeleccionado.getPrimerNombre() + " " + medicoSeleccionado.getPrimerApellido()) +
                    " en la fecha " + fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    "Sin resultados",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            System.err.println("Error en buscarCitas: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarBusqueda() {
        dateChooser.setDate(null);
        cmbMedicos.setSelectedIndex(0);
        tableModel.setRowCount(0);
        lblTotalCitas.setText("Total de citas: 0");
    }
    
    private String traducirEstado(Object estadoObj) {
        if (estadoObj == null) return "DESCONOCIDO";
        
        String estado = estadoObj.toString();
        
        if (estado.equals("SCHEDULED")) return "AGENDADA";
        if (estado.equals("CONFIRMED")) return "CONFIRMADA";
        if (estado.equals("IN_PROGRESS")) return "EN CURSO";
        if (estado.equals("COMPLETED")) return "COMPLETADA";
        if (estado.equals("CANCELLED")) return "CANCELADA";
        if (estado.equals("RESCHEDULED")) return "REPROGRAMADA";
        if (estado.equals("NO_SHOW")) return "NO ASISTIÓ";
        
        return estado;
    }
}