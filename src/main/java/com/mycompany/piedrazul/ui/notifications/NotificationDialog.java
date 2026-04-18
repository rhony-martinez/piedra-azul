/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.ui.notifications;

import com.mycompany.piedrazul.domain.model.Notification;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.service.NotificationService;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Window;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author asus
 */
public class NotificationDialog extends JDialog {

    private final NotificationService notificationService;
    private final Usuario usuario;

    private JTable table;
    private DefaultTableModel model;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

    public NotificationDialog(Window parent,
            NotificationService notificationService,
            Usuario usuario) {

        super(parent, "Notificaciones", ModalityType.APPLICATION_MODAL);

        this.notificationService = notificationService;
        this.usuario = usuario;

        initComponents();
        cargarNotificaciones();
    }

    private void initComponents() {

        model = new DefaultTableModel(
                new Object[] { "Fecha", "Mensaje", "Leída" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(200, 230, 240));

        table.getColumnModel().getColumn(0).setPreferredWidth(150); // Fecha
        table.getColumnModel().getColumn(1).setPreferredWidth(280); // Mensaje
        table.getColumnModel().getColumn(2).setPreferredWidth(70); // Leída

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scroll = new JScrollPane(table);

        add(scroll);

        setSize(500, 300);
        setLocationRelativeTo(null);
    }

    private void cargarNotificaciones() {

        List<Notification> lista = notificationService.obtenerNotificaciones(usuario.getId());

        model.setRowCount(0);

        for (Notification n : lista) {
            model.addRow(new Object[] {
                    n.getFechaCreacion().format(formatter),
                    n.getMensaje(),
                    n.isLeida() ? "Sí" : "No"
            });
        }
    }
}
