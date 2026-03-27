package com.mycompany.piedrazul.ui;

import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.service.UsuarioService;
import com.mycompany.piedrazul.ui.panel.*;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuPrincipalFrame extends JFrame {

    private Usuario usuario;
    private UsuarioService usuarioService;
    private JButton btnCerrar;
    private JPanel panelContenido;

    public MenuPrincipalFrame(Usuario usuario, UsuarioService usuarioService) {
        this.usuario = usuario;
        this.usuarioService = usuarioService;
        initComponents();
    }

    private void initComponents() {

        setTitle("PIEDRAZUL - Menú principal");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header
        JLabel lblHeader = new JLabel(
                "Bienvenido: " + usuario.getUsername()
                + " - " + usuario.getRol(),
                SwingConstants.CENTER
        );

        lblHeader.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // Panel dinámico por rol (ahora pasamos el usuario)
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.add(obtenerPanelPorRol(), BorderLayout.CENTER);

        mainPanel.add(panelContenido, BorderLayout.CENTER);

        // Botón cerrar sesión
        btnCerrar = new JButton("Cerrar sesión");

        btnCerrar.addActionListener(e -> {
            new LoginFrame(usuarioService).setVisible(true);
            this.dispose();
        });

        JPanel southPanel = new JPanel();
        southPanel.add(btnCerrar);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel obtenerPanelPorRol() {
        return switch (usuario.getRol()) {
            case ADMINISTRADOR ->
                new AdminPanel();
            case MEDICO_TERAPISTA ->
                new MedicoPanel(usuario);
            case PACIENTE ->
                new PacientePanel(usuario);
            case AGENDADOR ->
                new AgendadorPanel(usuario, this);
        };
    }

    public void mostrarPanel(JPanel panel) {
        panelContenido.removeAll();
        panelContenido.add(panel, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
}
