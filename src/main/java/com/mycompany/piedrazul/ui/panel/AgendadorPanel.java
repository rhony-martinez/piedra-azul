package com.mycompany.piedrazul.ui.panel;

import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.ui.appointments.ManualAppointmentDialog;
import com.mycompany.piedrazul.ui.MenuPrincipalFrame;
import com.mycompany.piedrazul.ui.appointments.AppointmentListPanel;
//import com.mycompany.piedrazul.ui.appointments.ManualAppointmentDialog;
import javax.swing.*;
import java.awt.*;

public class AgendadorPanel extends JPanel {

    private Usuario usuarioActual;
    private MenuPrincipalFrame frame;

    public AgendadorPanel(Usuario usuario, MenuPrincipalFrame frame) {
        this.usuarioActual = usuario;
        this.frame = frame;
        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        gridPanel.setBackground(Color.WHITE);

        JButton btnAgendar = crearBoton("AGENDAR CITA");
        JButton btnCitasAgendadas = crearBoton("CITAS AGENDADAS");
        JButton btnHistorial = crearBoton("HISTORIAL DE CITAS");

        //Acciones
        btnAgendar.addActionListener(e -> {
            ManualAppointmentDialog dialog = new ManualAppointmentDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this), 
                usuarioActual
            );
            dialog.setVisible(true);
        });

        // Abrir ventana para agendar
        btnAgendar.addActionListener(e -> {
            ManualAppointmentDialog dialog = new ManualAppointmentDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    usuarioActual
            );
            dialog.setVisible(true);
        });

        // Mostrar citas próximas
        btnCitasAgendadas.addActionListener(e -> {
            frame.mostrarPanel(new AppointmentListPanel(usuarioActual, false));
        });

        // Mostrar historial
        btnHistorial.addActionListener(e -> {
            frame.mostrarPanel(new AppointmentListPanel(usuarioActual, true));
        });

        gridPanel.add(btnAgendar);
        gridPanel.add(btnCitasAgendadas);
        gridPanel.add(btnHistorial);

        add(gridPanel, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(new Color(33, 150, 243));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(200, 60));
        return boton;
    }
}