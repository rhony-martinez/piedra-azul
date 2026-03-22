package com.mycompany.piedrazul.ui.panel;

import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.ui.appointments.AppointmentListPanel;
import com.mycompany.piedrazul.ui.appointments.SelfServiceAppointmentDialog;
import javax.swing.*;
import java.awt.*;

public class PacientePanel extends JPanel {
    
    private Usuario usuarioActual;

    public PacientePanel(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 120, 60, 120));
        centerPanel.setBackground(Color.WHITE);

        JButton btnAgendar = crearBoton("AGENDAR CITA");
        JButton btnCitasAgendadas = crearBoton("MIS CITAS");

        btnAgendar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCitasAgendadas.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Acciones
        btnAgendar.addActionListener(e -> {
            SelfServiceAppointmentDialog dialog = new SelfServiceAppointmentDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this), 
                usuarioActual
            );
            dialog.setVisible(true);
        });

        btnCitasAgendadas.addActionListener(e -> {
            AppointmentListPanel listPanel = new AppointmentListPanel(usuarioActual, false);
            JOptionPane.showMessageDialog(this, listPanel, "Mis Citas", JOptionPane.PLAIN_MESSAGE);
        });

        centerPanel.add(btnAgendar);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(btnCitasAgendadas);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(new Color(255, 152, 0));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setMaximumSize(new Dimension(300, 60));
        return boton;
    }
}