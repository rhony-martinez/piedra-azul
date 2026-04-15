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

    private JComboBox<Medico> cmbMedicos;
    private JDateChooser dateChooser;
    private JButton btnBuscar;
    private JButton btnLimpiar;

    public HistorialCitasFrame(Usuario usuarioActual, JFrame parentFrame) {
        this.usuarioActual = usuarioActual;
        this.parentFrame = parentFrame;
        
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
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(220, 220, 220));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

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
        
        JPanel botonesEstadoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        botonesEstadoPanel.setBackground(Color.WHITE);
        botonesEstadoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnConfirmar = new JButton("✅ Confirmar");
        JButton btnAtender = new JButton("🏥 Atender");
        JButton btnCancelar = new JButton("❌ Cancelar");
        JButton btnNoAsistio = new JButton("⚠️ No Asistió");
        JButton btnReagendar = new JButton("📅 Reagendar");
        
        Color btnColor = new Color(70, 170, 200);
        btnConfirmar.setBackground(btnColor);
        btnAtender.setBackground(btnColor);
        btnCancelar.setBackground(new Color(244, 67, 54));
        btnNoAsistio.setBackground(new Color(255, 152, 0));
        btnReagendar.setBackground(new Color(156, 39, 176));
        
        btnConfirmar.setForeground(Color.WHITE);
        btnAtender.setForeground(Color.WHITE);
        btnCancelar.setForeground(Color.WHITE);
        btnNoAsistio.setForeground(Color.WHITE);
        btnReagendar.setForeground(Color.WHITE);
        
        btnConfirmar.setFocusPainted(false);
        btnAtender.setFocusPainted(false);
        btnCancelar.setFocusPainted(false);
        btnNoAsistio.setFocusPainted(false);
        btnReagendar.setFocusPainted(false);
        
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAtender.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNoAsistio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReagendar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnConfirmar.addActionListener(e -> cambiarEstadoCita("confirmar"));
        btnAtender.addActionListener(e -> cambiarEstadoCita("atender"));
        btnCancelar.addActionListener(e -> cambiarEstadoCita("cancelar"));
        btnNoAsistio.addActionListener(e -> cambiarEstadoCita("noAsistir"));
        btnReagendar.addActionListener(e -> cambiarEstadoCita("reagendar"));
        
        botonesEstadoPanel.add(btnConfirmar);
        botonesEstadoPanel.add(btnAtender);
        botonesEstadoPanel.add(btnCancelar);
        botonesEstadoPanel.add(btnNoAsistio);
        botonesEstadoPanel.add(btnReagendar);
        
        resultadosPanel.add(botonesEstadoPanel, BorderLayout.NORTH);
        
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblTotalCitas = new JLabel("Total de citas: 0");
        lblTotalCitas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalCitas.setForeground(new Color(70, 170, 200));
        totalPanel.add(lblTotalCitas);
        
        resultadosPanel.add(totalPanel, BorderLayout.SOUTH);
        
        centerPanel.add(resultadosPanel, BorderLayout.CENTER);
        
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
    
    private void cambiarEstadoCita(String accion) {
        int filaSeleccionada = table.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int citaId = (int) tableModel.getValueAt(filaSeleccionada, 0);
        
        try {
            Appointment cita = appointmentService.obtenerCitaPorId(citaId);
            if (cita == null) {
                JOptionPane.showMessageDialog(this, "No se encontró la cita", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String mensajeExito = "";
            String estadoAnterior = cita.getEstado().toString();
            
            switch (accion) {
                case "confirmar":
                    System.out.println("=== CONFIRMAR ===");
                    System.out.println("Cita ID: " + cita.getId());
                    System.out.println("Estado antes: " + cita.getEstado());
                    cita.confirmar();
                    System.out.println("Estado después: " + cita.getEstado());
                    mensajeExito = "Cita confirmada correctamente";
                    break;
                    
                case "atender":
                    System.out.println("=== ATENDER ===");
                    System.out.println("Cita ID: " + cita.getId());
                    System.out.println("Estado antes: " + cita.getEstado());
                    
                    if (cita.getEstado() != AppointmentStatus.CONFIRMADA) {
                        JOptionPane.showMessageDialog(this, 
                            "Solo se pueden atender citas CONFIRMADAS.\nEstado actual: " + cita.getEstado(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    System.out.println("Llamando a cita.atender()...");
                    cita.atender();
                    System.out.println("Estado después: " + cita.getEstado());
                    mensajeExito = "Cita atendida correctamente";
                    break;
                    
                case "cancelar":
                    int confirm = JOptionPane.showConfirmDialog(this, "¿Cancelar esta cita?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) return;
                    cita.cancelar();
                    mensajeExito = "Cita cancelada";
                    break;
                    
                case "noAsistir":
                    cita.noAsistir();
                    mensajeExito = "Paciente marcado como No Asistió";
                    break;
                    
                case "reagendar":
                    String nuevaHoraStr = JOptionPane.showInputDialog(this, 
                        "Ingrese nueva hora (HH:MM):", 
                        "Reagendar cita", 
                        JOptionPane.QUESTION_MESSAGE);
                    if (nuevaHoraStr == null || nuevaHoraStr.trim().isEmpty()) return;
                    
                    String[] partes = nuevaHoraStr.split(":");
                    int nuevaHora = Integer.parseInt(partes[0]);
                    int nuevoMinuto = Integer.parseInt(partes[1]);
                    
                    LocalDateTime nuevaFechaHora = cita.getFechaHora()
                        .withHour(nuevaHora)
                        .withMinute(nuevoMinuto);
                    cita.setFechaHora(nuevaFechaHora);
                    cita.reagendar();
                    mensajeExito = "Cita reagendada para las " + nuevaHoraStr;
                    break;
            }
            
            boolean actualizado = appointmentService.actualizarCita(cita);
            if (!actualizado) {
                JOptionPane.showMessageDialog(this, "Error al guardar", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(this, 
                mensajeExito + "\nEstado: " + estadoAnterior + " → " + cita.getEstado(),
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            buscarCitas();
            
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, "Transición no válida: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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
        
        try {
            List<Appointment> todasLasCitas = appointmentService.obtenerTodasLasCitas();
            
            if (todasLasCitas == null || todasLasCitas.isEmpty()) {
                lblTotalCitas.setText("Total de citas: 0");
                return;
            }
            
            List<Appointment> citasFiltradas = new ArrayList<>();
            
            for (Appointment cita : todasLasCitas) {
                if (cita.getFechaHora() == null) continue;
                
                LocalDate fechaCita = cita.getFechaHora().toLocalDate();
                boolean coincideFecha = fechaCita.equals(fecha);
                boolean coincideMedico = (medicoId == 0) || 
                                         (cita.getMedico() != null && cita.getMedico().getId() == medicoId);
                
                if (coincideFecha && coincideMedico) {
                    citasFiltradas.add(cita);
                }
            }
            
            citasFiltradas.sort((a, b) -> a.getFechaHora().compareTo(b.getFechaHora()));
            
            DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");
            
            for (Appointment cita : citasFiltradas) {
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
                    traducirEstado(cita.getEstado()),
                    cita.getObservacion() != null ? cita.getObservacion() : ""
                };
                tableModel.addRow(row);
            }
            
            lblTotalCitas.setText("Total de citas: " + citasFiltradas.size());
            
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
    
    private String traducirEstado(AppointmentStatus estado) {
        if (estado == null) return "DESCONOCIDO";
        
        switch (estado) {
            case PROGRAMADA: return "PROGRAMADA";
            case CONFIRMADA: return "CONFIRMADA";
            case CANCELADA: return "CANCELADA";
            case ATENDIDA: return "ATENDIDA";
            case NO_ASISTIDA: return "NO ASISTIDA";
            case REAGENDADA: return "REAGENDADA";
            default: return estado.toString();
        }
    }
}