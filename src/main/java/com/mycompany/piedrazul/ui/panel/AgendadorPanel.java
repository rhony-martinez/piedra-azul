package com.mycompany.piedrazul.ui.panel;

import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.ui.appointments.ManualAppointmentDialog;
import javax.swing.*;
import java.awt.*;

public class AgendadorPanel extends JPanel {
    
    private Usuario usuarioActual;
    private JFrame parentFrame;

    public AgendadorPanel(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        initComponents();
    }
    
    public void setParentFrame(JFrame parent) {
        this.parentFrame = parent;
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // ===================== BARRA SUPERIOR =====================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        JLabel lblBienvenido = new JLabel("Bienvenido: " + usuarioActual.getUsername());
        lblBienvenido.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBienvenido.setForeground(new Color(70, 170, 200));
        
        JLabel lblRol = new JLabel(usuarioActual.getRol().toString());
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRol.setForeground(new Color(70, 170, 200));
        
        topBar.add(lblBienvenido, BorderLayout.WEST);
        topBar.add(lblRol, BorderLayout.EAST);
        
        // ===================== BOTONES DE ACCIÓN =====================
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(40, 150, 40, 150));
        
        JButton btnAgendar = crearBoton("AGENDAR CITA");
        JButton btnHistorial = crearBoton("HISTORIAL DE CITAS");
        
        btnAgendar.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.setVisible(false);
                new ManualAppointmentDialog(usuarioActual, parentFrame).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error: No se pudo obtener la ventana principal", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnHistorial.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Funcionalidad en desarrollo - Ver historial de citas", 
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        buttonsPanel.add(btnAgendar);
        buttonsPanel.add(btnHistorial);
        
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
        boton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        boton.setPreferredSize(new Dimension(280, 100));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        
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