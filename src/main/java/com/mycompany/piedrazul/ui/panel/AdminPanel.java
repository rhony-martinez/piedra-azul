/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.ui.panel;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {

    public AdminPanel() {
        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 🔹 Panel central tipo dashboard
        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        gridPanel.setBackground(Color.WHITE);

        // 🔹 Botones
        JButton btnCrear = crearBoton("CREAR USUARIOS");
        JButton btnEditar = crearBoton("EDITAR USUARIOS");
        JButton btnAuditoria = crearBoton("AUDITORÍA");
        JButton btnDesactivar = crearBoton("DESACTIVAR USUARIOS");
        JButton btnConsultar = crearBoton("CONSULTAR USUARIOS DEL SISTEMA");

        gridPanel.add(btnCrear);
        gridPanel.add(btnEditar);
        gridPanel.add(btnAuditoria);
        gridPanel.add(btnDesactivar);
        gridPanel.add(btnConsultar);

        // Espacio vacío para balance visual
        gridPanel.add(new JLabel());

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
