/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.ui.panel;

import javax.swing.*;
import java.awt.*;

public class AgendadorPanel extends JPanel {

    public AgendadorPanel() {
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

        gridPanel.add(btnAgendar);
        gridPanel.add(btnCitasAgendadas);
        gridPanel.add(btnHistorial);

        add(gridPanel, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);

        boton.setFocusPainted(false);
        boton.setBackground(new Color(33, 150, 243)); // Azul diferente al admin
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(200, 60));

        return boton;
    }
}

