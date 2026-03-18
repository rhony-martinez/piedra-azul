/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.ui.panel;

import javax.swing.*;
import java.awt.*;

public class PacientePanel extends JPanel {

    public PacientePanel() {
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
        JButton btnCitasAgendadas = crearBoton("CITAS AGENDADAS");

        btnAgendar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCitasAgendadas.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(btnAgendar);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(btnCitasAgendadas);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);

        boton.setFocusPainted(false);
        boton.setBackground(new Color(255, 152, 0)); // Naranja paciente
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setMaximumSize(new Dimension(300, 60));

        return boton;
    }
}

