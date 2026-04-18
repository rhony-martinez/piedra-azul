/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.service;

import com.mycompany.piedrazul.domain.model.Notification;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.INotificationRepository;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author asus
 */
public class NotificationService {

    private final INotificationRepository repository;

    public NotificationService(INotificationRepository repository) {
        this.repository = repository;
    }

    public void notifyUser(Usuario usuario, String mensaje) {

        if (usuario == null) return;

        Notification n = new Notification();
        n.setUsuario(usuario);
        n.setMensaje(mensaje);
        n.setFechaCreacion(LocalDateTime.now());
        n.setLeida(false);

        repository.save(n);
    }

    public List<Notification> obtenerNotificaciones(int usuarioId) {
        return repository.findByUsuario(usuarioId);
    }
}
