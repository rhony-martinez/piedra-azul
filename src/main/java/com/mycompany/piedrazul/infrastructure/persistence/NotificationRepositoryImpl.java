/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.infrastructure.persistence;

import com.mycompany.piedrazul.domain.model.Notification;
import com.mycompany.piedrazul.domain.repository.INotificationRepository;
import com.mycompany.piedrazul.infrastructure.persistence.connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author asus
 */
public class NotificationRepositoryImpl implements INotificationRepository {

    @Override
    public Notification save(Notification notification) {

        String sql = "INSERT INTO Notificacion (usu_id, not_mensaje, not_fecha, not_leida) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, notification.getUsuario().getId());
            ps.setString(2, notification.getMensaje());
            ps.setTimestamp(3, Timestamp.valueOf(notification.getFechaCreacion()));
            ps.setBoolean(4, notification.isLeida());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar notificación", e);
        }

        return notification;
    }

    @Override
    public List<Notification> findByUsuario(int usuarioId) {

        List<Notification> lista = new ArrayList<>();

        String sql = "SELECT * FROM Notificacion WHERE usu_id = ? ORDER BY not_fecha DESC";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Notification n = new Notification();
                n.setId(rs.getInt("not_id"));
                n.setMensaje(rs.getString("not_mensaje"));
                n.setFechaCreacion(rs.getTimestamp("not_fecha").toLocalDateTime());
                n.setLeida(rs.getBoolean("not_leida"));

                lista.add(n);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener notificaciones", e);
        }

        return lista;
    }

    @Override
    public void markAsRead(int notificationId) {

        String sql = "UPDATE Notificacion SET not_leida = TRUE WHERE not_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al marcar notificación", e);
        }
    }
}
