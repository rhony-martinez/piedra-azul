package com.mycompany.piedrazul.ui;

import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.service.UsuarioService;
import com.mycompany.piedrazul.ui.panel.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuPrincipalFrame extends JFrame {

    private Usuario usuario;
    private UsuarioService usuarioService;
    private JButton btnCerrar;

    public MenuPrincipalFrame(Usuario usuario, UsuarioService usuarioService) {
        this.usuario = usuario;
        this.usuarioService = usuarioService;
        initComponents();
    }

    private void initComponents() {
        setTitle("PIEDRAZUL - Menú principal");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ==============================
        // Barra superior turquesa con logo y botón cerrar
        // ==============================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(40, 170, 200));
        topBar.setPreferredSize(new Dimension(800, 80));
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JLabel lblTitulo = new JLabel("PIEDRAZUL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 32));
        lblTitulo.setForeground(Color.WHITE);
        
        btnCerrar = new JButton("Cerrar sesión");
        btnCerrar.setBackground(new Color(244, 67, 54));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setPreferredSize(new Dimension(130, 40));
        btnCerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        btnCerrar.addActionListener(e -> {
            new LoginFrame(usuarioService).setVisible(true);
            this.dispose();
        });
        
        topBar.add(lblTitulo, BorderLayout.WEST);
        topBar.add(btnCerrar, BorderLayout.EAST);
        
        // ==============================
        // Panel central con el contenido según rol
        // ==============================
        JPanel contentPanel = obtenerPanelPorRol();
        
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }

    private JPanel obtenerPanelPorRol() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        
        JPanel content;
        switch (usuario.getRol()) {
            case ADMINISTRADOR:
                content = new AdminPanel();
                break;
            case MEDICO_TERAPISTA:
                content = new MedicoPanel(usuario);
                break;
            case PACIENTE:
                content = new PacientePanel(usuario);
                break;
            case AGENDADOR:
                AgendadorPanel agendadorPanel = new AgendadorPanel(usuario);
                agendadorPanel.setParentFrame(this);
                content = agendadorPanel;
                break;
            default:
                content = new JPanel();
                content.setBackground(Color.WHITE);
        }
        
        wrapper.add(content, BorderLayout.CENTER);
        return wrapper;
    }
}