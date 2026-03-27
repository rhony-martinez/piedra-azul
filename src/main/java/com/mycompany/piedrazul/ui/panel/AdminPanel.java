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
        
        // ===================== BARRA SUPERIOR =====================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        JLabel lblBienvenido = new JLabel("Bienvenido: Administrador");
        lblBienvenido.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBienvenido.setForeground(new Color(70, 170, 200));
        
        JLabel lblRol = new JLabel("Administrador");
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRol.setForeground(new Color(70, 170, 200));
        
        topBar.add(lblBienvenido, BorderLayout.WEST);
        topBar.add(lblRol, BorderLayout.EAST);
        
        // ===================== BOTONES DE ACCIÓN =====================
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        
        JButton btnCrear = crearBoton("CREAR USUARIOS");
        JButton btnEditar = crearBoton("EDITAR USUARIOS");
        JButton btnAuditoria = crearBoton("AUDITORÍA");
        JButton btnDesactivar = crearBoton("DESACTIVAR USUARIOS");
        JButton btnConsultar = crearBoton("CONSULTAR USUARIOS");
        
        buttonsPanel.add(btnCrear);
        buttonsPanel.add(btnEditar);
        buttonsPanel.add(btnAuditoria);
        buttonsPanel.add(btnDesactivar);
        buttonsPanel.add(btnConsultar);
        buttonsPanel.add(new JLabel());
        
        centerPanel.add(buttonsPanel);
        
        // ===================== TEXTO INFERIOR DERECHA =====================
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 40));
        
        JLabel lblMenuPrincipal = new JLabel("Menú principal");
        lblMenuPrincipal.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblMenuPrincipal.setForeground(new Color(70, 170, 200));
        
        bottomPanel.add(lblMenuPrincipal, BorderLayout.EAST);
        
        add(topBar, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(new Color(70, 170, 200));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setPreferredSize(new Dimension(250, 90));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        
        // Hover effect con ChangeListener (CORREGIDO)
        boton.addChangeListener(e -> {
            if (boton.getModel().isRollover()) {
                boton.setBackground(new Color(50, 140, 170));
            } else {
                boton.setBackground(new Color(70, 170, 200));
            }
        });
        
        return boton;
    }
}