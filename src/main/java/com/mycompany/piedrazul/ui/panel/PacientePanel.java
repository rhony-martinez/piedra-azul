package com.mycompany.piedrazul.ui.panel;

import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.INotificationRepository;
import com.mycompany.piedrazul.domain.service.NotificationService;
import com.mycompany.piedrazul.infrastructure.persistence.NotificationRepositoryImpl;
import com.mycompany.piedrazul.ui.appointments.SelfServiceAppointmentDialog;
import com.mycompany.piedrazul.ui.notifications.NotificationDialog;
import javax.swing.*;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.event.MouseAdapter;

public class PacientePanel extends JPanel {

    private Usuario usuarioActual;
    private NotificationService notificationService;
    private INotificationRepository notificationRepository;

    public PacientePanel(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;

        this.notificationRepository = new NotificationRepositoryImpl();
        this.notificationService = new NotificationService(notificationRepository);
        initComponents();
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

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(Color.WHITE);

        // Botón notificaciones
        JButton btnNotificaciones = new JButton("🔔");
        btnNotificaciones.setFocusPainted(false);
        btnNotificaciones.setBackground(Color.WHITE);
        btnNotificaciones.setBorder(BorderFactory.createEmptyBorder());
        btnNotificaciones.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNotificaciones.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));

        // Acción
        btnNotificaciones.addActionListener(e -> {
            Window parent = SwingUtilities.getWindowAncestor(this);

            new NotificationDialog(
                    parent,
                    notificationService,
                    usuarioActual).setVisible(true);
        });

        btnNotificaciones.addMouseListener(new MouseAdapter() {
    public void mouseEntered(MouseEvent evt) {
        btnNotificaciones.setBackground(new Color(230, 230, 230));
    }

    public void mouseExited(MouseEvent evt) {
        btnNotificaciones.setBackground(Color.WHITE);
    }
});

        // Label rol
        JLabel lblRol = new JLabel(usuarioActual.getRol().toString());
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRol.setForeground(new Color(70, 170, 200));

        // Agregar al panel derecho
        rightPanel.add(btnNotificaciones);
        rightPanel.add(lblRol);

        topBar.add(lblBienvenido, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);

        // ===================== BOTONES DE ACCIÓN =====================
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(40, 150, 40, 150));

        JButton btnAgendar = crearBoton("AGENDAR CITA");
        JButton btnCitasAgendadas = crearBoton("MIS CITAS");

        btnAgendar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCitasAgendadas.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Acciones
        btnAgendar.addActionListener(e -> {
            abrirDialogoAgendamiento();
        });

        btnCitasAgendadas.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Funcionalidad en desarrollo - Ver mis citas",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        buttonsPanel.add(btnAgendar);
        buttonsPanel.add(Box.createVerticalStrut(40));
        buttonsPanel.add(btnCitasAgendadas);

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
        boton.setPreferredSize(new Dimension(320, 80));
        boton.setMaximumSize(new Dimension(320, 80));
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

    private void abrirDialogoAgendamiento() {
        Window window = SwingUtilities.getWindowAncestor(this);

        if (window instanceof JFrame frame) {
            SelfServiceAppointmentDialog dialog = new SelfServiceAppointmentDialog(frame, usuarioActual);

            dialog.setVisible(true);
        }
    }
}